package org.rathena.minerva.commons.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.rathena.minerva.commons.Minerva;
import org.rathena.minerva.commons.net.packet.Packet;
import org.rathena.minerva.commons.net.packet.PacketHandler;
import org.rathena.minerva.commons.net.packet.PacketListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameServer implements PacketDatabase {

    private static boolean EPOLL = Epoll.isAvailable();

    private Map<Class<Packet>, List<PacketHandlerInfo>> packetListeners = new HashMap<>();
    private Map<Integer, PacketInfo> packetDb = new HashMap<>();

    /**
     * Port this server binds on.
     */
    private int port;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup bossGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        EventLoopGroup workerGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new MinervaChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(port).sync();

            LogManager.getLogger().info(Minerva.getServerType() + " is now listening on port " + Integer.toString(port));

            future.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void registerParsablePacket(int id, Class<? extends Packet> packet, int length) {
        try {
            packet.getMethod("fromByteBuf", ByteBuf.class);
        } catch (NoSuchMethodException e) {
            Minerva.getLogger().error("Packet class registered without fromByteBuf(ByteBuf) static method. Aborting..");
            return;
        }

        packetDb.put(id, new PacketInfo(packet, length));
    }

    @Override
    public void registerPacketListener(String owner, PacketListener listener) {
        Method[] methods = listener.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(PacketHandler.class) && method.getParameterCount() == 1) {
                Class clazz = method.getParameterTypes()[0];
                if (Packet.class.isAssignableFrom(clazz)) {
                    if (!packetListeners.containsKey(clazz))
                        packetListeners.put(clazz, new ArrayList<>());
                    PacketHandlerInfo packetHandlerInfo = new PacketHandlerInfo(listener, method);
                    packetListeners.get(clazz).add(packetHandlerInfo);
                }
            }
        }
    }

    @Override
    public int getPacketLength(int packetId) {
        if(packetDb.containsKey(packetId)) {
            return packetDb.get(packetId).length;
        } else {
            return 0;
        }
    }

    @Override
    public Class<? extends Packet> getPacketClass(int packetId) {
        return packetDb.get(packetId).clazz;
    }

    //@Override
    public List<PacketListener> getPacketHandlersFor(Class<? extends Packet> packet) {
        return packetListeners.get(packet).stream().map((e) -> e.listener).collect(Collectors.toList());
    }

    @Override
    public void invokeHandlers(Packet packet) {
        packetListeners.get(packet.getClass()).stream().forEach((e) -> {
            try {
                e.method.invoke(e.listener, packet);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
        });
    }

    class PacketHandlerInfo {
        PacketListener listener;
        Method method;

        public PacketHandlerInfo(PacketListener listener, Method method) {
            this.listener = listener;
            this.method = method;
        }
    }

    class PacketInfo {
        public Class<? extends Packet> clazz;
        public int length;

        public PacketInfo(Class<? extends Packet> clazz, int length) {
            this.clazz = clazz;
            this.length = length;
        }
    }
}

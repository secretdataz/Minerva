package org.rathena.minerva.commons.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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

@ChannelHandler.Sharable
public class GameServerHandler extends ChannelInboundHandlerAdapter implements InboundPacketSource {

    private Map<Class<Packet>, List<PacketHandlerInfo>> packetListeners = new HashMap<>();
    private Map<Integer, PacketInfo> packetDb = new HashMap<>();

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        if (((ByteBuf) msg).readableBytes() < 2) {
            Minerva.getLogger().error("Packet too short.");
            in.release();
            return;
        }

        int packetType = in.getUnsignedShortLE(0);
        PacketInfo packetInfo = packetDb.get(packetType);
        if (packetInfo != null && in.readableBytes() == packetInfo.length) {
            System.out.println("Packet received: " + packetInfo.clazz.getName());
            List<PacketHandlerInfo> handlers = packetListeners.get(packetInfo.clazz);
            if (handlers != null)
                for (PacketHandlerInfo info : handlers) {
                    try {
                        info.method.invoke(info.listener, packetInfo.clazz.getConstructor(ByteBuf.class).newInstance(in));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
        }
        System.out.println("Bytes length: " + in.readableBytes());
        for (int i = 0; i < in.readableBytes(); i++) {
            System.out.print(bytesToHex(new byte[]{in.getByte(i)}) + " ");
        }
        System.out.println();

        in.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void registerParsablePacket(int id, Class<? extends Packet> packet, int length, int[] pos) {
        packetDb.put(id, new PacketInfo(packet, length, pos));
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
        public int[] pos;

        public PacketInfo(Class<? extends Packet> clazz, int length, int[] pos) {
            this.clazz = clazz;
            this.length = length;
            this.pos = pos;
        }
    }
}

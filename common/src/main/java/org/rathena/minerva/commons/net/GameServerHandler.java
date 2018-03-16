package org.rathena.minerva.commons.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.rathena.minerva.commons.Minerva;
import org.rathena.minerva.commons.net.packet.Packet;
import org.rathena.minerva.commons.net.packet.PacketListener;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@ChannelHandler.Sharable
public class GameServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Minerva.getLogger().info(msg.getClass().getName() + " received from client.");
        PacketDatabase packetDatabase = Minerva.getMinerva().getPacketDatabase();

        if(msg instanceof Packet) {
            packetDatabase.invokeHandlers((Packet) msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        cause.printStackTrace();
        ctx.close();
    }
}

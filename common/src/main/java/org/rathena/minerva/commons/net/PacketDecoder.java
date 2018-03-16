package org.rathena.minerva.commons.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.rathena.minerva.commons.Minerva;
import org.rathena.minerva.commons.net.packet.Packet;
import org.rathena.minerva.commons.util.StringUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        PacketDatabase packetDatabase = Minerva.getMinerva().getPacketDatabase();
        if(in.readableBytes() < 2)
            return;

        int packetId = in.getUnsignedShortLE(0);
        int packetLen = packetDatabase.getPacketLength(packetId);

        if(packetLen == -1 && in.readableBytes() < 4) // Variable-length packet
            packetLen = in.getUnsignedShortLE(2);
        if(in.readableBytes() >= packetLen) {
            Class packetClass = packetDatabase.getPacketClass(packetId);
            if(packetClass != null) {
                Method fromByteBuf = packetClass.getMethod("fromByteBuf", ByteBuf.class);
                Object packet = fromByteBuf.invoke(null, in.readBytes(packetLen));
                if(packet instanceof Packet)
                    out.add(packet);
            }
        }
    }
}

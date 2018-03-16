package org.rathena.minerva.account.net.packet;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;
import org.rathena.minerva.commons.net.packet.Packet;
import org.rathena.minerva.commons.util.StringUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Packet_AC_LOGIN_V1 extends Packet implements LoginPacket {

    byte clientType;
    int clientVersion;
    String username;
    String password;

    @Override
    public int getClientVersion() {
        return clientVersion;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public byte getClientType() {
        return clientType;
    }

    @Override
    public byte[] toByteArray() {
        throw new IllegalStateException("This packet is not supposed to be constructed.");
    }

    public static Packet_AC_LOGIN_V1 fromByteBuf(ByteBuf buf) {
        Packet_AC_LOGIN_V1 packet = new Packet_AC_LOGIN_V1();
        byte[] ba = new byte[24];
        packet.setPacketId(buf.readUnsignedShortLE());
        packet.clientVersion = buf.readIntLE();
        packet.username = StringUtil.readCStringFromByteBuf(buf.readBytes(24), 24);
        packet.password = StringUtil.readCStringFromByteBuf(buf.readBytes(24), 24);
        packet.clientType = buf.readByte();

        return packet;
    }
}

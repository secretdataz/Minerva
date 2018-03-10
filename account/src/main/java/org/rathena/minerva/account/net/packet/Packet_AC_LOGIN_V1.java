package org.rathena.minerva.account.net.packet;

import org.rathena.minerva.commons.net.packet.Packet;
import org.rathena.minerva.commons.util.StringUtil;

public class Packet_AC_LOGIN_V1 extends Packet implements LoginPacket{

    public Packet_AC_LOGIN_V1(io.netty.buffer.ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    public int getClientVersion() {
        return getByteBuf().getIntLE(2);
    }

    @Override
    public String getUsername() {
        return StringUtil.getCStringFromByteBuf(getByteBuf(), 6, 24);
    }

    @Override
    public String getPassword() {
        return StringUtil.getCStringFromByteBuf(getByteBuf(), 30, 24);
    }

    @Override
    public byte getClientType() {
        return getByteBuf().getByte(55);
    }
}

package org.rathena.minerva.commons.net.packet;

import io.netty.buffer.ByteBuf;

public abstract class Packet {
    private ByteBuf byteBuf;

    public Packet(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public ByteBuf getByteBuf() {
        return byteBuf;
    }

    public int getPacketId() {
        return byteBuf.getUnsignedShortLE(0);
    }
}

package org.rathena.minerva.commons.net.packet;

import io.netty.buffer.ByteBuf;

public abstract class Packet {

    protected int packetId;

    public Packet() {}

    public int getPacketId() {
        return packetId;
    }

    protected void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public abstract byte[] toByteArray();
}

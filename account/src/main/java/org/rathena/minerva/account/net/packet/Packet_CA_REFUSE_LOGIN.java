package org.rathena.minerva.account.net.packet;

import org.rathena.minerva.commons.net.packet.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Packet_CA_REFUSE_LOGIN extends Packet {

    private byte errorCode;
    private byte[] blockDate;

    public Packet_CA_REFUSE_LOGIN(byte errorCode, byte[] blockDate) {
        this.errorCode = errorCode;
        this.blockDate = blockDate;
    }

    @Override
    public byte[] toByteArray() {
        ByteBuffer buf = ByteBuffer.allocate(34);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putShort((short)0x6a);
        buf.put(errorCode);
        buf.put(blockDate, 0, 20);
        return buf.array();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = (byte)errorCode;
    }

    public byte[] getBlockDate() {
        return blockDate;
    }

    public void setBlockDate(byte[] blockDate) {
        this.blockDate = blockDate;
    }
}

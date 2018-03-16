package org.rathena.minerva.account.net.packet;

import org.rathena.minerva.commons.net.packet.Packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Packet_CA_REFUSE_LOGIN_R2 extends Packet {

    private int errorCode;
    private byte[] blockDate;

    public Packet_CA_REFUSE_LOGIN_R2(int errorCode, byte[] blockDate) {
        this.errorCode = errorCode;
        this.blockDate = blockDate;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public byte[] getBlockDate() {
        return blockDate;
    }

    public void setBlockDate(byte[] blockDate) {
        this.blockDate = blockDate;
    }

    @Override
    public byte[] toByteArray() {
        ByteBuffer buf = ByteBuffer.allocate(68);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        buf.putShort((short)0x6a);
        buf.putInt(errorCode);
        buf.put(blockDate, 0, 20);
        return buf.array();
    }
}

package org.rathena.minerva.commons.net;

import org.rathena.minerva.commons.net.packet.Packet;
import org.rathena.minerva.commons.net.packet.PacketListener;

public interface InboundPacketSource {
    void registerParsablePacket(int id, Class<? extends Packet> packet, int length, int[] pos);
    void registerPacketListener(String owner, PacketListener listener);
}

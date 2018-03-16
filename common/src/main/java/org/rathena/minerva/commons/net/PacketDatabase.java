package org.rathena.minerva.commons.net;

import org.rathena.minerva.commons.net.packet.Packet;
import org.rathena.minerva.commons.net.packet.PacketListener;

import java.util.List;

public interface PacketDatabase {
    void registerParsablePacket(int id, Class<? extends Packet> packet, int length);
    void registerPacketListener(String owner, PacketListener listener);
    int getPacketLength(int packetId);
    Class<? extends Packet> getPacketClass(int packetId);
    //List<PacketListener> getPacketHandlersFor(Class<? extends Packet> packet);
    void invokeHandlers(Packet packet);
}

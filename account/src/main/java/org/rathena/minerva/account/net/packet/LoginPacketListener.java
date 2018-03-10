package org.rathena.minerva.account.net.packet;

import org.rathena.minerva.commons.Minerva;
import org.rathena.minerva.commons.net.InboundPacketSource;
import org.rathena.minerva.commons.net.packet.PacketHandler;
import org.rathena.minerva.commons.net.packet.PacketListener;

public class LoginPacketListener implements PacketListener {

    public LoginPacketListener(InboundPacketSource packetSource) {
        packetSource.registerParsablePacket(0x64, Packet_AC_LOGIN_V1.class, 55, null);
    }

    @PacketHandler
    public void onLoginPacket(Packet_AC_LOGIN_V1 packet) {
        Minerva.getLogger().info("User " + packet.getUsername() + " logged in.");
        Minerva.getLogger().info("Password: " + packet.getPassword() + " Client version: " + packet.getClientVersion() + " Client Type: " + packet.getClientType());
    }
}

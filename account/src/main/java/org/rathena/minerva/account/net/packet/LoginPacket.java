package org.rathena.minerva.account.net.packet;

public interface LoginPacket {
    int getClientVersion();
    String getUsername();
    String getPassword();
    byte getClientType();
}

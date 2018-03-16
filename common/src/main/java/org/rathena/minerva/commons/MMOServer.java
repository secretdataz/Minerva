package org.rathena.minerva.commons;

import org.rathena.minerva.commons.net.PacketDatabase;

public interface MMOServer {
    void start();
    void stop();
    PacketDatabase getPacketDatabase();
}

package org.rathena.minerva.commons;

import org.rathena.minerva.commons.net.InboundPacketSource;

public interface MMOServer {
    void start();
    void stop();
    InboundPacketSource getInboundPacketSource();
}

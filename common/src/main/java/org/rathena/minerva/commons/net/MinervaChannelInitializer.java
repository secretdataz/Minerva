package org.rathena.minerva.commons.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class MinervaChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new PacketEncoder());
        ch.pipeline().addLast(new PacketDecoder());
        ch.pipeline().addLast(new GameServerHandler());
    }
}

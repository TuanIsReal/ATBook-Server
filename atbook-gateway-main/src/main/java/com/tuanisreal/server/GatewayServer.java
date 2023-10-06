package com.tuanisreal.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
public class GatewayServer {
    private Channel channel;

    private ServerBootstrap serverBootstrap;

    private InetSocketAddress port;

    public GatewayServer(ServerBootstrap serverBootstrap, InetSocketAddress port) {
        this.serverBootstrap = serverBootstrap;
        this.port = port;
    }

    public void start() throws Exception {
        channel =  serverBootstrap.bind(port).sync().channel().closeFuture().sync().channel();
    }

    @PreDestroy
    public void stop() throws Exception {
        channel.close();
        channel.parent().close();
    }
}

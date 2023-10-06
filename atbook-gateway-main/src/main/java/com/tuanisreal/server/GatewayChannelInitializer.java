package com.tuanisreal.server;

import com.tuanisreal.config.Config;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GatewayChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final int ONE_MEGABYTE = 1024 * 1024;

    private Config config;

    private GatewayHandler gatewayHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin()
                .allowedRequestMethods(HttpMethod.OPTIONS, HttpMethod.POST, HttpMethod.GET, HttpMethod.HEAD)
                .allowedRequestHeaders("Access-Control-Request-Method", "Access-Control-Allow-Credentials",
                        "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Content-Type")
                .allowNullOrigin().allowCredentials().build();
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(ONE_MEGABYTE));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast(new CorsHandler(corsConfig));
        pipeline.addLast(gatewayHandler);
    }
}

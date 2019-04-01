package com.rolbel.web_socket_tools.handler;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public interface WebSocketChannelHandler {
    ChannelInboundHandlerAdapter getWebSocketInboundHandler();

    ChannelOutboundHandlerAdapter getWebSocketOutboundHandler();
}

package com.yetoce.web_socket_tools.handler;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public abstract class AbstractWebSocketChannelHandler implements WebSocketChannelHandler {
    private WebSocketHandler webSocketHandler;

    public AbstractWebSocketChannelHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public ChannelInboundHandlerAdapter getWebSocketInboundHandler() {
        return new WebSocketInboundHandler(webSocketHandler);
    }

    @Override
    public ChannelOutboundHandlerAdapter getWebSocketOutboundHandler() {
        return new WebSocketOutboundHandler();
    }
}

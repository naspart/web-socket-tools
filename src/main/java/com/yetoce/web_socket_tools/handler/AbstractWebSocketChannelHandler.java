package com.yetoce.web_socket_tools.handler;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public abstract class AbstractWebSocketChannelHandler implements WebSocketChannelHandler {
    private WebSocketInboundHandler webSocketInboundHandler;
    private WebSocketOutboundHandler webSocketOutboundHandler;

    public AbstractWebSocketChannelHandler(WebSocketUpgradeResolverHandler webSocketUpgradeResolverHandler, WebSocketHandler webSocketHandler) {
        webSocketInboundHandler = new WebSocketInboundHandler(webSocketHandler, webSocketUpgradeResolverHandler);
        webSocketOutboundHandler = new WebSocketOutboundHandler();
    }

    @Override
    public ChannelInboundHandlerAdapter getWebSocketInboundHandler() {
        return this.webSocketInboundHandler;
    }

    @Override
    public ChannelOutboundHandlerAdapter getWebSocketOutboundHandler() {
        return this.webSocketOutboundHandler;
    }
}

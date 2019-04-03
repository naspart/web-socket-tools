package com.yetoce.web_socket_tools.handler;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public abstract class AbstractWebSocketChannelHandler implements WebSocketChannelHandler {
    private WebSocketHandler webSocketHandler;
    private WebSocketUpgradeResolverHandler webSocketUpgradeResolverHandler;

    public AbstractWebSocketChannelHandler(WebSocketUpgradeResolverHandler webSocketUpgradeResolverHandler, WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
        this.webSocketUpgradeResolverHandler = webSocketUpgradeResolverHandler;
    }

    @Override
    public ChannelInboundHandlerAdapter getWebSocketInboundHandler() {
        return new WebSocketInboundHandler(webSocketHandler, webSocketUpgradeResolverHandler);
    }

    @Override
    public ChannelOutboundHandlerAdapter getWebSocketOutboundHandler() {
        return new WebSocketOutboundHandler();
    }
}

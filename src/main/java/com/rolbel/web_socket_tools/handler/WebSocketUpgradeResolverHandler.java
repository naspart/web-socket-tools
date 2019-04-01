package com.rolbel.web_socket_tools.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public interface WebSocketUpgradeResolverHandler {

    WebSocketServerHandshaker handleRequest(ChannelHandlerContext ctx, FullHttpRequest request);

    void handleRequestError(ChannelHandlerContext ctx, FullHttpRequest request, Throwable throwable);
}

package com.yetoce.web_socket_tools.session;

import com.yetoce.web_socket_tools.handler.WebSocketHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public interface WebSocketSession {
    String getId();

    ChannelHandlerContext getChannelHandlerContext();

    void sendMessage(WebSocketFrame webSocketFrame);

    WebSocketHandler getWebSocketHandler();

    WebSocketServerHandshaker getWebSocketServerHandshaker();

    void close();
}

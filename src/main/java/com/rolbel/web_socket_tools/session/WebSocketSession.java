package com.rolbel.web_socket_tools.session;

import com.rolbel.web_socket_tools.handler.WebSocketHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.List;
import java.util.Map;

public interface WebSocketSession {
    Channel getChannel();

    String getId();

    Map<String, List<String>> getParameters();

    HttpHeaders getHandShakeHeaders();

    void sendMessage(WebSocketFrame webSocketFrame);

    void close();

    WebSocketHandler getWebSocketHandler();

    WebSocketServerHandshaker getWebSocketHandshaker();
}

package com.yetoce.web_socket_tools.handler;

import com.yetoce.web_socket_tools.CloseStatus;
import com.yetoce.web_socket_tools.session.WebSocketSession;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface WebSocketHandler {
    void beforeConnectionUpgraded(WebSocketSession webSocketSession) throws Exception;

    void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception;

    void handleMessage(WebSocketSession webSocketSession, WebSocketFrame webSocketFrame) throws Exception;

    void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception;

    void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception;
}

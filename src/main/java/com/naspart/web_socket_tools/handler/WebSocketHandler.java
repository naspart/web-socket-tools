package com.naspart.web_socket_tools.handler;

import com.naspart.web_socket_tools.CloseStatus;
import com.naspart.web_socket_tools.exception.WebSocketException;
import com.naspart.web_socket_tools.session.WebSocketSession;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public interface WebSocketHandler {
    void beforeConnectionUpgraded(WebSocketSession webSocketSession) throws WebSocketException;

    void afterConnectionEstablished(WebSocketSession webSocketSession) throws WebSocketException;

    void handleMessage(WebSocketSession webSocketSession, WebSocketFrame webSocketFrame) throws WebSocketException;

    void handleTimeout(WebSocketSession webSocketSession) throws WebSocketException;

    void handleTransportError(WebSocketSession webSocketSession, Throwable throwable);

    void beforeConnectionClose(WebSocketSession webSocketSession) throws WebSocketException;

    void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws WebSocketException;
}

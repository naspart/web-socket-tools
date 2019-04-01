package com.rolbel.web_socket_tools.handler;

import com.rolbel.web_socket_tools.CloseStatus;
import com.rolbel.web_socket_tools.session.WebSocketSession;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWebSocketHandler implements WebSocketHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void beforeConnectionUpgraded(WebSocketSession webSocketSession) throws Exception {
        log.debug(webSocketSession.getId() + "开始连接");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        log.debug(webSocketSession.getId() + "连接成功");
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketFrame webSocketFrame) throws Exception {
        String msg = ((TextWebSocketFrame) webSocketFrame).text();
        log.debug(msg);
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        log.debug(webSocketSession.getId() + "断开连接");
    }
}

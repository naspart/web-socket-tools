package com.yetoce.web_socket_tools.handler;

import com.yetoce.web_socket_tools.exception.WebSocketException;
import com.yetoce.web_socket_tools.session.WebSocketSession;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public interface WebSocketUpgradeResolverHandler {
    WebSocketServerHandshaker handleRequest(WebSocketSession session, FullHttpRequest request);

    void handleRequestError(WebSocketSession session, FullHttpRequest request, WebSocketException ex);
}

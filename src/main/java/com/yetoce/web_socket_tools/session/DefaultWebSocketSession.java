package com.yetoce.web_socket_tools.session;

import com.yetoce.web_socket_tools.CloseStatus;
import com.yetoce.web_socket_tools.handler.WebSocketHandler;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

import java.util.List;
import java.util.Map;

public class DefaultWebSocketSession implements WebSocketSession {
    private Channel channel;
    private String id;
    private HttpHeaders handShakeHeaders;
    private Map<String, List<String>> parameters;
    private WebSocketHandler webSocketHandler;
    private WebSocketServerHandshaker webSocketHandshaker;

    @Override
    public Map<String, List<String>> getParameters() {
        return this.parameters;
    }

    @Override
    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public HttpHeaders getHandShakeHeaders() {
        return this.handShakeHeaders;
    }

    @Override
    public void sendMessage(WebSocketFrame webSocketFrame) {
        if (this.channel.isOpen() && this.channel.isActive()) {
            this.channel.writeAndFlush(webSocketFrame);
        }
    }

    @Override
    public void close() {
        if (this.webSocketHandshaker != null && this.channel != null && this.channel.isOpen()) {
            CloseWebSocketFrame closeWebSocketFrame = new CloseWebSocketFrame(CloseStatus.NORMAL.getCode(), CloseStatus.NORMAL.getReason());
            this.webSocketHandshaker.close(this.channel, closeWebSocketFrame);
        }
    }

    @Override
    public WebSocketHandler getWebSocketHandler() {
        return this.webSocketHandler;
    }

    @Override
    public WebSocketServerHandshaker getWebSocketHandshaker() {
        return this.webSocketHandshaker;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHandShakeHeaders(HttpHeaders handShakeHeaders) {
        this.handShakeHeaders = handShakeHeaders;
    }

    public void setParameters(Map<String, List<String>> parameters) {
        this.parameters = parameters;
    }

    public void setWebSocketHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    public void setWebSocketHandshaker(WebSocketServerHandshaker webSocketHandshaker) {
        this.webSocketHandshaker = webSocketHandshaker;
    }
}

package com.yetoce.web_socket_tools.session;

import com.yetoce.web_socket_tools.CloseStatus;
import com.yetoce.web_socket_tools.handler.WebSocketHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;

public class DefaultWebSocketSession implements WebSocketSession {
    private String id;
    private ChannelHandlerContext channelHandlerContext;
    private WebSocketHandler webSocketHandler;
    private WebSocketServerHandshaker webSocketServerHandshaker;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public ChannelHandlerContext getChannelHandlerContext() {
        return this.channelHandlerContext;
    }

    @Override
    public void sendMessage(WebSocketFrame webSocketFrame) {
        if (this.channelHandlerContext.channel().isOpen() && this.channelHandlerContext.channel().isActive()) {
            this.channelHandlerContext.writeAndFlush(webSocketFrame);
        }
    }

    @Override
    public WebSocketHandler getWebSocketHandler() {
        return this.webSocketHandler;
    }

    @Override
    public WebSocketServerHandshaker getWebSocketServerHandshaker() {
        return this.webSocketServerHandshaker;
    }

    @Override
    public void close() {
        if (this.channelHandlerContext != null && this.channelHandlerContext.channel().isOpen()) {
            if (this.webSocketServerHandshaker != null) {
                CloseWebSocketFrame closeWebSocketFrame = new CloseWebSocketFrame(CloseStatus.NORMAL.getCode(), CloseStatus.NORMAL.getReason());
                this.webSocketServerHandshaker.close(this.channelHandlerContext.channel(), closeWebSocketFrame);
            }
        }
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public void setWebSocketHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    public void setWebSocketServerHandshaker(WebSocketServerHandshaker webSocketServerHandshaker) {
        this.webSocketServerHandshaker = webSocketServerHandshaker;
    }
}

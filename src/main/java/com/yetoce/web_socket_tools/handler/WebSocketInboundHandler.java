package com.yetoce.web_socket_tools.handler;

import com.yetoce.web_socket_tools.session.WebSocketSession;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.rtsp.RtspResponseStatuses.INTERNAL_SERVER_ERROR;

public class WebSocketInboundHandler extends AbstractWebSocketInboundHandler {
    private WebSocketUpgradeResolverHandler webSocketUpgradeResolverHandler;
    private WebSocketHandler webSocketHandler;

    public WebSocketInboundHandler(WebSocketHandler webSocketHandler, WebSocketUpgradeResolverHandler webSocketUpgradeResolverHandler) {
        this.webSocketHandler = webSocketHandler;
        this.webSocketUpgradeResolverHandler = webSocketUpgradeResolverHandler;
    }

    @Override
    public WebSocketUpgradeResolverHandler getWebSocketUpgradeResolverHandler() {
        return webSocketUpgradeResolverHandler;
    }

    @Override
    public WebSocketHandler getSocketHandler(String path) {
        return webSocketHandler;
    }

    @Override
    public void doChannelRead(WebSocketSession session, WebSocketFrame msg) throws Exception {
        WebSocketHandler handler = session.getWebSocketHandler();
        handler.handleMessage(session, msg);
    }

    @Override
    public void doExceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR, Unpooled.copiedBuffer("Failed : " + INTERNAL_SERVER_ERROR.toString() + "\r\n", CharsetUtil.UTF_8));
            response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

            ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
}

package com.yetoce.web_socket_tools.handler;

import com.yetoce.web_socket_tools.exception.WebSocketException;
import com.yetoce.web_socket_tools.session.WebSocketSession;
import com.yetoce.web_socket_tools.util.ChannelUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public abstract class AbstractWebSocketOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws WebSocketException {
        WebSocketSession session = ChannelUtils.getSessionByChannel(ctx.channel());
        if (session != null) {
            this.doDisconnect(session);
        }

        ctx.disconnect(promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws WebSocketException {
        WebSocketSession session = ChannelUtils.getSessionByChannel(ctx.channel());
        if (session != null) {
            this.doClose(session);
        }

        ctx.close(promise);
    }

    public abstract void doDisconnect(WebSocketSession session) throws WebSocketException;

    public abstract void doClose(WebSocketSession session) throws WebSocketException;
}

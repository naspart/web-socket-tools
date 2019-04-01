package com.yetoce.web_socket_tools.handler;

import com.yetoce.web_socket_tools.session.WebSocketSession;
import com.yetoce.web_socket_tools.util.ChannelUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public abstract class AbstractWebSocketOutboundHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        this.doDisconnect(ChannelUtils.getSessionByChannel(ctx.channel()), promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        this.doClose(ChannelUtils.getSessionByChannel(ctx.channel()), promise);
        super.close(ctx, promise);
    }

    public abstract void doDisconnect(WebSocketSession session, ChannelPromise promise) throws Exception;

    public abstract void doClose(WebSocketSession session, ChannelPromise promise) throws Exception;
}

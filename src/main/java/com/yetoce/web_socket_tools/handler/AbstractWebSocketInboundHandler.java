package com.yetoce.web_socket_tools.handler;

import com.yetoce.web_socket_tools.exception.WebSocketException;
import com.yetoce.web_socket_tools.session.DefaultWebSocketSession;
import com.yetoce.web_socket_tools.session.WebSocketSession;
import com.yetoce.web_socket_tools.session.WebSocketSessionGroup;
import com.yetoce.web_socket_tools.util.ChannelUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public abstract class AbstractWebSocketInboundHandler extends ChannelInboundHandlerAdapter {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            QueryStringDecoder uriDecoder = new QueryStringDecoder(uri);
            Map<String, List<String>> parameters = uriDecoder.parameters();

            if (parameters.get("uid") == null || parameters.get("uid").size() == 0) {
                ctx.close();
                return;
            }

            String path = uriDecoder.path();
            if (path.startsWith("//")) {
                path = path.substring(1);
            }

            String uid = parameters.get("uid").get(0);
            log.debug("A client start establishing a connection, uid: " + uid);

            DefaultWebSocketSession webSocketSession = new DefaultWebSocketSession();
            webSocketSession.setId(uid);
            webSocketSession.setChannelHandlerContext(ctx);

            WebSocketHandler socketHandler = this.getSocketHandler(path);
            webSocketSession.setWebSocketHandler(socketHandler);

            try {
                socketHandler.beforeConnectionUpgraded(webSocketSession);
            } catch (Exception e) {
                socketHandler.handleTransportError(webSocketSession, e);
                return;
            }

            WebSocketServerHandshaker webSocketServerHandshaker = null;
            try {
                webSocketServerHandshaker = this.doHandshake(webSocketSession, request);
            } catch (Exception ex) {
                log.warn(ex.getMessage(), ex);
            }

            if (webSocketServerHandshaker != null) {
                webSocketSession.setWebSocketServerHandshaker(webSocketServerHandshaker);

                if (!ChannelUtils.addChannelSession(ctx.channel(), webSocketSession)) {
                    ctx.close();
                }

                socketHandler.afterConnectionEstablished(webSocketSession);
                WebSocketSessionGroup.add(uid, webSocketSession);
            } else {
                socketHandler.handleTransportError(webSocketSession, new WebSocketException("upgrade failed.!"));
            }
        } else if (msg instanceof PingWebSocketFrame) {
            ctx.write(new PongWebSocketFrame(((WebSocketFrame) msg).content().retain()));
        } else if (msg instanceof CloseWebSocketFrame) {
            ctx.close();
        } else if (msg instanceof TextWebSocketFrame) {
            WebSocketSession session = ChannelUtils.getSessionByChannel(ctx.channel());
            if (session == null) {
                ctx.close();
            } else {
                this.doChannelRead(session, (WebSocketFrame) msg);
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (IdleState.READER_IDLE.equals(event.state())) {
                log.debug("Packets have not been received for too long");
                WebSocketSession session = ChannelUtils.getSessionByChannel(ctx.channel());
                if (session != null) {
                    this.doChannelTimeout(session);
                    log.debug("Close this inactive channel: " + session.getId());
                }
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws WebSocketException {
        this.doExceptionCaught(ctx, cause);
    }

    public abstract WebSocketServerHandshaker doHandshake(WebSocketSession session, FullHttpRequest request);

    public abstract WebSocketHandler getSocketHandler(String path);

    public abstract void doChannelRead(WebSocketSession session, WebSocketFrame msg) throws WebSocketException;

    public abstract void doChannelTimeout(WebSocketSession session) throws WebSocketException;

    public abstract void doExceptionCaught(ChannelHandlerContext ctx, Throwable cause);
}

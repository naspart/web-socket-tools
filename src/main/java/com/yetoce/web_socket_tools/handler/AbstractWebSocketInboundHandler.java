package com.yetoce.web_socket_tools.handler;

import com.yetoce.web_socket_tools.CloseStatus;
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
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractWebSocketInboundHandler extends ChannelInboundHandlerAdapter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private AtomicInteger idle_count = new AtomicInteger(1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            QueryStringDecoder uriDecoder = new QueryStringDecoder(uri);
            Map<String, List<String>> parameters = uriDecoder.parameters();

            if (parameters.get("uid") == null || parameters.get("uid").size() == 0) {
                ctx.channel().close();
                return;
            }

            String path = uriDecoder.path();
            if (path.startsWith("//")) {
                path = path.substring(1);
            }

            String uid = parameters.get("uid").get(0);
            log.debug("A client start establishing a connection, uid: " + uid);

            DefaultWebSocketSession webSocketSession = new DefaultWebSocketSession();
            webSocketSession.setParameters(parameters);
            webSocketSession.setHandShakeHeaders(request.headers());
            webSocketSession.setChannel(ctx.channel());
            webSocketSession.setId(uid);

            WebSocketHandler socketHandler = this.getSocketHandler(path);
            webSocketSession.setWebSocketHandler(socketHandler);

            try {
                socketHandler.beforeConnectionUpgraded(webSocketSession);
            } catch (Exception e) {
                socketHandler.handleTransportError(webSocketSession, e);
                return;
            }

            WebSocketServerHandshaker handShaker = this.getWebSocketUpgradeResolverHandler().handleRequest(ctx, request);
            if (handShaker != null) {
                webSocketSession.setWebSocketHandshaker(handShaker);

                if (!ChannelUtils.addChannelSession(ctx.channel(), webSocketSession)) {
                    ctx.channel().close();
                }

                socketHandler.afterConnectionEstablished(webSocketSession);
                WebSocketSessionGroup.add(uid, webSocketSession);
            } else {
                socketHandler.handleTransportError(webSocketSession, new WebSocketException("upgrade failed.!"));
            }
        } else if (msg instanceof CloseWebSocketFrame) {
            WebSocketSession session = ChannelUtils.getSessionByChannel(ctx.channel());
            if (session != null) {
                WebSocketSessionGroup.remove(session);
                session.close();
                session.getWebSocketHandler().afterConnectionClosed(session, CloseStatus.NORMAL);
            }
        } else if (msg instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(((WebSocketFrame) msg).content().retain()));
        } else if (msg instanceof TextWebSocketFrame) {
            this.doChannelRead(ChannelUtils.getSessionByChannel(ctx.channel()), (WebSocketFrame) msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            // 如果读通道处于空闲状态，说明没有接收到心跳命令
            if (IdleState.READER_IDLE.equals(event.state())) {
                log.info("已经5秒没有接收到客户端的信息了");
                if (idle_count.get() > 1) {
                    log.info("关闭这个不活跃的channel");
                    ctx.channel().close();
                }
                idle_count.getAndIncrement();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        this.doExceptionCaught(ctx, cause);
    }

    public abstract WebSocketUpgradeResolverHandler getWebSocketUpgradeResolverHandler();

    public abstract WebSocketHandler getSocketHandler(String path);

    public abstract void doChannelRead(WebSocketSession session, WebSocketFrame msg) throws Exception;

    public abstract void doExceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception;
}

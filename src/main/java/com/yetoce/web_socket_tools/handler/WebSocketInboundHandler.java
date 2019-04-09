package com.yetoce.web_socket_tools.handler;

import com.sun.javafx.binding.StringFormatter;
import com.yetoce.web_socket_tools.constant.WebSocketConstant;
import com.yetoce.web_socket_tools.exception.WebSocketException;
import com.yetoce.web_socket_tools.session.WebSocketSession;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.rtsp.RtspHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.rtsp.RtspResponseStatuses.INTERNAL_SERVER_ERROR;

@ChannelHandler.Sharable
public class WebSocketInboundHandler extends AbstractWebSocketInboundHandler {
    private WebSocketHandler webSocketHandler;

    public WebSocketInboundHandler(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    public WebSocketServerHandshaker doHandshake(WebSocketSession session, FullHttpRequest request) {
        HttpHeaders httpHeaders = request.headers();
        if (request.decoderResult().isSuccess() && WebSocketConstant.WEBSOCKET.equals(httpHeaders.get(WebSocketConstant.UPGRADE).toLowerCase())) {
            String protocols = httpHeaders.get(WebSocketConstant.SEC_WEBSOCKET_PROTOCOL);
            String host = httpHeaders.get("Host");
            String uri = request.uri();
            String webAddress = StringFormatter.format(WebSocketConstant.DEFAULT_WEBSOCKET_ADDRESS_FORMAT, host).getValueSafe() + uri;

            //设置最大帧长度，保证安全
            int frameLength = 10 * 1024 * 1024;
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(webAddress, protocols, true, frameLength);

            WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(request);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(session.getChannelHandlerContext().channel());
            } else {
                handshaker.handshake(session.getChannelHandlerContext().channel(), request);
            }

            return handshaker;
        }

        return null;
    }

    @Override
    public WebSocketHandler getSocketHandler(String path) {
        return webSocketHandler;
    }

    @Override
    public void doChannelRead(WebSocketSession session, WebSocketFrame msg) throws WebSocketException {
        WebSocketHandler handler = session.getWebSocketHandler();
        handler.handleMessage(session, msg);
    }

    @Override
    public void doChannelTimeout(WebSocketSession session) throws WebSocketException {
        WebSocketHandler handler = session.getWebSocketHandler();
        handler.handleTimeout(session);
    }

    @Override
    public void doExceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.warn(cause.getMessage(), cause);

        if (ctx.channel().isActive()) {
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, INTERNAL_SERVER_ERROR, Unpooled.copiedBuffer("Failed : " + INTERNAL_SERVER_ERROR.toString() + "\r\n", CharsetUtil.UTF_8));
            response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }
}

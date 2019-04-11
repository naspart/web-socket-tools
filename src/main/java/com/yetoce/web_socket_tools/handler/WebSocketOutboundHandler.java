package com.yetoce.web_socket_tools.handler;

import com.yetoce.web_socket_tools.CloseStatus;
import com.yetoce.web_socket_tools.exception.WebSocketException;
import com.yetoce.web_socket_tools.session.WebSocketSession;
import com.yetoce.web_socket_tools.session.WebSocketSessionGroup;
import io.netty.channel.ChannelHandler;

@ChannelHandler.Sharable
public class WebSocketOutboundHandler extends AbstractWebSocketOutboundHandler {
    @Override
    public void doDisconnect(WebSocketSession session) throws WebSocketException {
    }

    @Override
    public void doClose(WebSocketSession session) throws WebSocketException {
        if (WebSocketSessionGroup.getById(session.getId()) != null) {
            session.getWebSocketHandler().beforeConnectionClose(session);
            WebSocketSessionGroup.remove(session);
            session.close();
            session.getWebSocketHandler().afterConnectionClosed(session, CloseStatus.NORMAL);
        }
    }
}

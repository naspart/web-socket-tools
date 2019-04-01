package com.rolbel.web_socket_tools.handler;

import com.rolbel.web_socket_tools.CloseStatus;
import com.rolbel.web_socket_tools.session.WebSocketSession;
import com.rolbel.web_socket_tools.session.WebSocketSessionGroup;
import io.netty.channel.ChannelPromise;

public class WebSocketOutboundHandler extends AbstractWebSocketOutboundHandler {
    @Override
    public void doDisconnect(WebSocketSession session, ChannelPromise promise) throws Exception {
    }

    @Override
    public void doClose(WebSocketSession session, ChannelPromise promise) throws Exception {
        WebSocketSessionGroup.remove(session);
        session.close();
        session.getWebSocketHandler().afterConnectionClosed(session, CloseStatus.NORMAL);
    }
}

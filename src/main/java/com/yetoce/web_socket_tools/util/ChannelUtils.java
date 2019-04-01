package com.yetoce.web_socket_tools.util;

import com.yetoce.web_socket_tools.session.WebSocketSession;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class ChannelUtils {
    private static AttributeKey<WebSocketSession> SESSION_KEY = AttributeKey.valueOf("session");

    public static boolean addChannelSession(Channel channel, WebSocketSession session) {
        Attribute<WebSocketSession> sessionAttr = channel.attr(SESSION_KEY);

        return sessionAttr.compareAndSet(null, session);
    }

    public static WebSocketSession getSessionByChannel(Channel channel) {
        Attribute<WebSocketSession> socketSessionAttribute = channel.attr(SESSION_KEY);

        return socketSessionAttribute.get();
    }
}

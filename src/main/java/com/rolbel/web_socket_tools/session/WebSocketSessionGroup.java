package com.rolbel.web_socket_tools.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessionGroup {
    private static Map<String, WebSocketSession> id2Sessions = new ConcurrentHashMap<>();

    public static WebSocketSession getById(String id) {
        return id2Sessions.get(id);
    }

    public static void add(String id, WebSocketSession session) {
        id2Sessions.put(id, session);
    }

    public static void remove(WebSocketSession session) {
        id2Sessions.remove(session.getId());
    }
}

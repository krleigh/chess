package websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {

        private final WebSocketSessions connections = new WebSocketSessions();

        @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {

        }

}

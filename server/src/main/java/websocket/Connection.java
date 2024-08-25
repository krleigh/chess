package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String username;
    public Session session;

    public Connection(String username, Session session) {
        this.username = username;
        this.session = session;
    }

    public void send(String message) throws IOException {
        // Gets objects that allow you to send messages back
        session.getRemote().sendString(message);
    }
}

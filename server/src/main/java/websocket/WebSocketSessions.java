package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {

    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }


    public void broadcast(String excludedUser, ServerMessage message) throws IOException {
//        System.out.println("Sending message: " + message.getMessage());
        var removeList = new ArrayList<Connection>();
        for (var connection : connections.values()) {
            if (connection.session.isOpen()) {
                if (!connection.username.equals(excludedUser)) {
                    connection.send(new Gson().toJson(message));
                }
            } else {
                removeList.add(connection);
            }
        }

        for (var connection : removeList) {
            connections.remove(connection.username);
        }
    }

    public void send(String user, ServerMessage message) throws IOException {
        var connection = connections.get(user);
        if (connection.session.isOpen()) {
            connection.send(new Gson().toJson(message));
        } else {
            connections.remove(connection.username);
            var msg = String.format("%s is not connected", user);
            System.out.println(msg);
        }
    }
}

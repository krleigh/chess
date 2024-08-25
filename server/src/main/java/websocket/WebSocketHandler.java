package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {

        private final WebSocketSessions connections = new WebSocketSessions();

        @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
            UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
            switch (userGameCommand.getCommandType()){
                case CONNECT -> connect();
                case MAKE_MOVE -> makeMove();
                case LEAVE -> leave();
                case RESIGN -> resign();
            }
        }

    private void connect() {

    }
    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }

    public void load_game() {

    }

    public void error() {

    }

    public void notification() {

    }
}

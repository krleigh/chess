package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerNotification;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final WebSocketSessions connections = new WebSocketSessions();

    private final UserService userService;
    private final GameService gameService;

    public WebSocketHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;

    }

        @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
            UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
            switch (userGameCommand.getCommandType()){
                case CONNECT -> connect(userGameCommand.getAuthToken(), userGameCommand.getGameID(), session);
                case MAKE_MOVE -> makeMove();
                case LEAVE -> leave(userGameCommand.getAuthToken());
                case RESIGN -> resign();
            }
        }

    private void connect(String authToken, Integer gameID, Session session) throws IOException {
        userService.
        connections.add(username, session);
        var message = String.format("%s joined the %s", username, gameID.toString());
        var serverMessage = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, serverMessage);
    }

    private void makeMove() {

    }

    private void leave(String username) throws IOException {
        connections.remove(username);
        var message = String.format("%s left the game.", username);
        var serverMessage = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(username, serverMessage);
    }

    private void resign() {

    }

    public void load_game() {
        try {

        } catch (Exception e) {

        }
    }

    public void error() {

    }

    public void notification() {

    }
}

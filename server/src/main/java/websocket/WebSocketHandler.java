package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.OnMessage;
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
    public void onMessage(Session session, String message) throws IOException, ResponseException {
        System.out.println("Received Message: " + message);
            UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
            switch (userGameCommand.getCommandType()){
                case CONNECT -> connect(userGameCommand.getAuthToken(), userGameCommand.getGameID(), session);
                case MAKE_MOVE -> makeMove();
                case LEAVE -> leave(userGameCommand.getAuthToken(), userGameCommand.getGameID());
                case RESIGN -> resign();
            }
        }

    private void connect(String authToken, Integer gameID, Session session) throws IOException, ResponseException {
        var username = userService.getAuth(authToken).username();
        connections.add(username, session);
        var msg = String.format("%s joined the %s", username, gameID.toString());
        var serverMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, msg);
        connections.broadcast(username, serverMessage);

        var loadMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, gameService.getGame(gameID));
        connections.send(username, loadMessage);
    }

    private void makeMove() {

    }

    private void leave(String authToken, Integer gameID) throws IOException, ResponseException {
        var username = userService.getAuth(authToken).username();
        connections.remove(username);
        gameService.leaveGame(username, gameID);
        var message = String.format("%s left the game.", username);
        var serverMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
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

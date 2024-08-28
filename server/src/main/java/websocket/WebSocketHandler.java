package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

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
        try {
//            System.out.println("Received Message: " + message);
            UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect(userGameCommand.getAuthToken(), userGameCommand.getGameID(), session);
                case MAKE_MOVE -> makeMove(userGameCommand.getAuthToken(), userGameCommand.getGameID(), userGameCommand.getMove(), session);
                case LEAVE -> leave(userGameCommand.getAuthToken(), userGameCommand.getGameID(), session);
                case RESIGN -> resign(userGameCommand.getAuthToken(), userGameCommand.getGameID(), session);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

    @OnWebSocketError
    public void onError(Throwable error) {
        System.err.println("Error: " + error.getMessage());
    }

    private void connect(String authToken, Integer gameID, Session session) throws IOException, ResponseException {

        String username = validateAuth(authToken);
        if (Objects.equals(username, "badAuth")){
            connections.add(username, session);
            errorMessage(username, "Error: bad auth");
            connections.remove(username);
            return;
        }

        connections.add(username, session);

        GameData game = validateGameID(username, gameID);
        if (game == null){return;}

        var msg = String.format("%s joined the %s", username, gameID.toString());
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, msg);
        notification(username, notification);

        loadGame(username, gameID, false);
    }

    private void makeMove(String authToken, Integer gameID, ChessMove move, Session session) throws IOException, ResponseException {
        var username = validateAuth(authToken);
        if (Objects.equals(username, "badAuth")) { connections.newSend(session ,
                new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"Error: Bad auth")); return;}

        GameData gameData = validateGameID(username, gameID);
        if (gameData == null) {return;}

        ChessGame.TeamColor teamColor = null;

        if (Objects.equals(username, gameData.blackUsername())) {
            teamColor = ChessGame.TeamColor.BLACK;
        } else if (Objects.equals(username, gameData.whiteUsername())){
            teamColor = ChessGame.TeamColor.WHITE;
        }

        if (teamColor == null) { errorMessage(username, "Error: Not a player. Cannot make moves."); return;}

        ChessGame newGame = gameData.game();

        try {newGame.makeMove(move);} catch (InvalidMoveException e) {
            errorMessage(username, "Error: Invalid move");
            return;
        }

        if (teamColor != newGame.getBoard().getPiece(move.getEndPosition()).getTeamColor()) {
            errorMessage(username, "Error: Cannot move opponent's pieces");
            return;
        }


        GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), newGame);
        try { gameService.updateGame(gameID, newGameData);} catch (Exception e) { System.out.println(e.getMessage());}

        loadGame(null, gameID, true);

        notification(username, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s moved %s", username, move)));

        checkGameState(newGame, newGameData);

    }

    private void leave(String authToken, Integer gameID, Session session) throws IOException, ResponseException {
        var username = validateAuth(authToken);
        if (Objects.equals(username, "badAuth")) {connections.newSend(session ,
                new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"Error: Bad auth")); return;}

        GameData game = validateGameID(username, gameID);
        if (game == null){return;}

        connections.remove(username);
        gameService.leaveGame(username, gameID);

        var message = String.format("%s left the game.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        notification(username, notification);
    }

    private void resign(String authToken, Integer gameID, Session session) throws IOException {
        var username = validateAuth(authToken);
        if (Objects.equals(username, "badAuth")) {connections.newSend(session ,
                new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"Error: Bad auth")); return;}

        GameData gameData = validateGameID(username, gameID);
        if (gameData == null){return;}

        ChessGame.TeamColor teamColor = null;

        if (Objects.equals(username, gameData.blackUsername())) {
            teamColor = ChessGame.TeamColor.BLACK;
        } else if (Objects.equals(username, gameData.whiteUsername())){
            teamColor = ChessGame.TeamColor.WHITE;
        }

        if (teamColor == null) { errorMessage(username, "Error: Not a player. Cannot make moves."); return;}

        connections.remove(username);

        var message = String.format("%s resigned the game. Game over.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        notification(username, notification);

    }


    public void loadGame(String username, Integer gameID, Boolean all) throws IOException, ResponseException {
        var loadMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, gameService.getGame(gameID));
        if (all) {
            connections.broadcast(username, loadMessage);
        } else {
            connections.send(username, loadMessage);
        }
    }

    public void errorMessage(String username, String message) throws IOException {
        connections.send(username, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message));
    }

    public void notification(String username, NotificationMessage notification) throws IOException {
        connections.broadcast(username, notification);
    }


    public String validateAuth(String authToken) throws IOException {
        String username = null;
        try { username = userService.getAuth(authToken).username(); } catch (Exception e) {
            username = "badAuth";
        }
        return username;
    }

    public GameData validateGameID(String username, Integer gameID) throws IOException {
        GameData game = null;
        try { game = gameService.getGame(gameID);} catch (Exception e) {
            errorMessage(username, "Error: Invalid game ID");
        }
        return game;
    }

    public void checkGameState(ChessGame newGame, GameData newGameData) throws IOException {
        String checked = null;

        if (newGame.isInCheck(ChessGame.TeamColor.BLACK)){
            checked = newGameData.blackUsername();
        } else if (newGame.isInStalemate(ChessGame.TeamColor.WHITE)) {
            checked = newGameData.whiteUsername();
        }

        if (checked != null) {
            notification(null, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s is in check!", checked)));
        }

        String checkedmate = null;
        String checkermate = null;

        if (newGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            checkedmate = newGameData.blackUsername();
            checkermate = newGameData.whiteUsername();
        } else if (newGame.isInCheckmate(ChessGame.TeamColor.WHITE)){
            checkermate = newGameData.blackUsername();
            checkedmate = newGameData.whiteUsername();
        }

        if (checkedmate != null) {
            notification(null, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s is in checkmate. Game over. %s wins!", checkedmate, checkermate)));
        }

        String stale = null;
        String fresh = null;

        if(newGame.isInStalemate(ChessGame.TeamColor.BLACK)){
            stale = newGameData.blackUsername();
            fresh = newGameData.whiteUsername();
        } else if (newGame.isInStalemate(ChessGame.TeamColor.WHITE)){
            stale = newGameData.whiteUsername();
            fresh = newGameData.blackUsername();
        }

        if(stale != null){
            notification(null, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s is in stalemate. Game Over. %s wins!", stale, fresh)));
        }
    }
}

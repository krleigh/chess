package websocket;

import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
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
            connections.add(gameID, username, session);
            errorMessage(username, "Error: bad auth", gameID);
            connections.remove(username, gameID);
            return;
        }

        connections.add(gameID, username, session);

        GameData game = validateGameID(username, gameID);
        if (game == null){return;}

        var msg = String.format("%s joined the %s", username, gameID.toString());
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, msg);
        notification(username, notification, gameID);

        loadGame(username, gameID, false);
    }

    private void makeMove(String authToken, Integer gameID, String move, Session session) throws IOException, ResponseException {
        //Check for valid auth and get username
        var username = validateAuth(authToken);
        if (Objects.equals(username, "badAuth")) { connections.newSend(session ,
                new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"Error: Bad auth")); return;}

        //Check for valid gameID and get GameData
        GameData gameData = validateGameID(username, gameID);
        if (gameData == null) {return;}

        //Check that client is not an observer
        if(notPlayer(username, gameData)) {return;}

        //Check Game Status
        if (isOver(username, gameData)) {return;}

        //Convert move to ChessMove
        ChessMove chessMove = makeChessMove(move);

        //Validate move
        ChessGame newGame = gameData.game();
        try {newGame.makeMove(chessMove);} catch (InvalidMoveException e) {
            errorMessage(username, "Error: Invalid move", gameID);
            return;
        }

        //Check that player moves their own color
        ChessGame.TeamColor teamColor = getTeamColor(username, gameData);
        if (teamColor != newGame.getBoard().getPiece(chessMove.getEndPosition()).getTeamColor()) {
            errorMessage(username, "Error: Cannot move opponent's pieces", gameID);
            return;
        }

        //Update game with move
        GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), newGame, GameData.GameStatus.ONGOING);
        try { gameService.updateGame(gameID, newGameData);} catch (Exception e) { System.out.println(e.getMessage());}

        //All players and observers load game
        loadGame(null, gameID, true);

        //Notify other player and observers of the move
        notification(username, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                String.format("%s moved %s", username, move)), gameID);

        //Check if there is a check, checkmate, or stalemate
        checksOrMatesCheck(newGame, newGameData);

    }

    private void leave(String authToken, Integer gameID, Session session) throws IOException, ResponseException {
        //Check for valid auth and get username
        var username = validateAuth(authToken);
        if (Objects.equals(username, "badAuth")) {connections.newSend(session ,
                new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"Error: Bad auth")); return;}

        //Check for valid gameID
        GameData gameData = validateGameID(username, gameID);
        if (gameData == null){return;}

        //If not observer, remove player from game
        if(getTeamColor(username, gameData)!=null) {
            //Check Game Status
            if (isOver(username, gameData)) {return;} else{
                gameService.leaveGame(username, gameID);
            }
        }

        //Notify others
        var message = String.format("%s left the game.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        notification(username, notification, gameID);

        //Remove connection
        connections.remove(username, gameID);
    }

    private void resign(String authToken, Integer gameID, Session session) throws IOException {
        //Check valid auth and get username
        var username = validateAuth(authToken);
        if (Objects.equals(username, "badAuth")) {connections.newSend(session ,
                new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"Error: Bad auth")); return;}

        //Check valid gameID and get GameData
        GameData gameData = validateGameID(username, gameID);
        if (gameData == null){return;}

        //Check that client is not an observer
        if(notPlayer(username, gameData)) {return;}

        //Check Game Status
        if (isOver(username, gameData)) {return;}

        //Update game
        try{ gameService.updateGame(gameID, new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                gameData.gameName(), gameData.game(), GameData.GameStatus.OVER));} catch (Exception e) { System.out.println(e.getMessage());}



        //Notify other player and observers
        var message = String.format("%s resigned the game. Game over.", username);
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        notification(null, notification, gameID);

        //Remove player connection
        connections.remove(username, gameID);

    }


    public void loadGame(String username, Integer gameID, Boolean all) throws IOException, ResponseException {
        var loadMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, null, gameService.getGame(gameID));
        if (all) {
            connections.broadcast(username, loadMessage, gameID);
        } else {
            connections.send(username, loadMessage, gameID);
        }
    }

    public void errorMessage(String username, String message, Integer gameID) throws IOException {
        connections.send(username, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, message), gameID);
    }

    public void notification(String username, NotificationMessage notification, Integer gameID) throws IOException {
        connections.broadcast(username, notification, gameID);
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
            errorMessage(username, "Error: Invalid game ID", gameID);
        }
        return game;
    }

    public Boolean notPlayer(String username, GameData gameData) throws IOException {
        ChessGame.TeamColor teamColor = getTeamColor(username, gameData);

        if (teamColor == null) { errorMessage(username, "Error: Not a player. Cannot make moves.", gameData.gameID()); return true;}
        return false;
    }

    public ChessGame.TeamColor getTeamColor(String username, GameData gameData) {
        ChessGame.TeamColor teamColor = null;

        if (Objects.equals(username, gameData.blackUsername())) {
            teamColor = ChessGame.TeamColor.BLACK;
        } else if (Objects.equals(username, gameData.whiteUsername())){
            teamColor = ChessGame.TeamColor.WHITE;
        }

        return teamColor;
    }

    public Boolean isOver(String username, GameData gameData) throws IOException {
        if (gameData.gameStatus()== GameData.GameStatus.OVER){
            errorMessage(username, "Game over.", gameData.gameID());
            return true;
        } else { return false;}
    }

    public void checksOrMatesCheck(ChessGame newGame, GameData newGameData) throws IOException {
        String checked = null;

        if (newGame.isInCheck(ChessGame.TeamColor.BLACK)){
            checked = newGameData.blackUsername();
        } else if (newGame.isInStalemate(ChessGame.TeamColor.WHITE)) {
            checked = newGameData.whiteUsername();
        }

        if (checked != null) {
            notification(null, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    String.format("%s is in check!", checked)), newGameData.gameID());
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
                    String.format("%s is in checkmate. Game over. %s wins!", checkedmate, checkermate)), newGameData.gameID());
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
                    String.format("%s is in stalemate. Game Over. %s wins!", stale, fresh)), newGameData.gameID());
        }
    }

    public ChessMove makeChessMove(String move) {
        String[] position = move.split(">");
        Integer startRow = position[0].charAt(0) - 'a' + 1;
        Integer startColumn = Character.getNumericValue(position[0].charAt(1));

        Integer endRow = position[1].charAt(0) - 'a' +1;
        Integer endColumn = Character.getNumericValue(position[1].charAt(1));

        ChessPiece.PieceType promP = null;

        if (position.length == 3){
            String piece = position[2];
            if (Objects.equals(piece, "queen")){ promP = ChessPiece.PieceType.QUEEN;}
            if (Objects.equals(piece, "rook")){ promP = ChessPiece.PieceType.ROOK;}
            if (Objects.equals(piece, "bishop")){ promP = ChessPiece.PieceType.BISHOP;}
            if (Objects.equals(piece, "knight")){ promP = ChessPiece.PieceType.KNIGHT;}
        }

        return new ChessMove(new ChessPosition(startRow, startColumn), new ChessPosition(endRow, endColumn), promP);
    }
}

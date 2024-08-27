package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import ui.DrawBoard;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


import javax.websocket.*;
import java.io.IOException;
import java.net.URISyntaxException;

import java.net.URI;

public class WebSocketFacade extends Endpoint {

    Session session;
    GameHandler gameHandler;
    Integer gameID;
    ChessGame.TeamColor teamColor;
    DrawBoard board;

    public WebSocketFacade(String url, GameHandler gameHandler, Integer gameID, ChessGame.TeamColor teamColor) throws ResponseException {

        this.gameHandler = gameHandler;
        this.gameID = gameID;
        this.teamColor = teamColor;

        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    var gson = new Gson();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> gameHandler.notify(gson.fromJson(message, NotificationMessage.class));
                        case LOAD_GAME -> loadGame(gson.fromJson(message, LoadGameMessage.class).getGame());
                        case ERROR -> gameHandler.notify(gson.fromJson(message, ErrorMessage.class));
                    }

                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e ){
            throw new ResponseException(500, e.getMessage());
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken) throws ResponseException {
        try {
            var command = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }

    }

    public void loadGame(GameData game) {
        gameHandler.updateGame(game);
        board = new DrawBoard(game, teamColor);
        System.out.println();
        board.draw();
    }

    public void leave(String authToken) throws ResponseException {
        try {
            var command = new LeaveCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void move() throws ResponseException {

    }

    public void resign() throws ResponseException {

    }

}

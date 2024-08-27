package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
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

    public WebSocketFacade(String url, GameHandler gameHandler, Integer gameID) throws ResponseException {

        this.gameHandler = gameHandler;
        this.gameID = gameID;

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
                    ServerMessage accessMessage = serverMessage;
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> accessMessage = gson.fromJson(message, NotificationMessage.class);
                        case LOAD_GAME -> accessMessage = gson.fromJson(message, LoadGameMessage.class);
                        case ERROR -> accessMessage = gson.fromJson(message, ErrorMessage.class);
                    }
                    gameHandler.notify(serverMessage);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e ){
            throw new ResponseException(500, e.getMessage());
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
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

package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import websocket.messages.ServerError;
import websocket.messages.ServerLoadGame;
import websocket.messages.ServerNotification;
import websocket.messages.ServerMessage;


import javax.websocket.*;
import java.io.IOException;
import java.net.URISyntaxException;

import java.net.URI;

public class WebSocketFacade extends Endpoint {

    Session session;
    GameHandler gameHandler;

    public WebSocketFacade(String url, GameHandler gameHandler) throws ResponseException {

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
                        case NOTIFICATION -> accessMessage = gson.fromJson(message, ServerNotification.class);
                        case LOAD_GAME -> accessMessage = gson.fromJson(message, ServerLoadGame.class);
                        case ERROR -> accessMessage = gson.fromJson(message, ServerError.class);
                    }
                    gameHandler.notify(accessMessage);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException e ){
            throw new ResponseException(500, e.getMessage());
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }
}

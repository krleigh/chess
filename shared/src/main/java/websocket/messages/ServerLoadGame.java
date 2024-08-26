package websocket.messages;

public class ServerLoadGame extends ServerMessage {

    public ServerLoadGame(ServerMessageType type, String message) {
        super(type);
        super.message = message;
    }
}

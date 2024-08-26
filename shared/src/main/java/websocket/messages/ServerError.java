package websocket.messages;

public class ServerError extends ServerMessage {

    public ServerError(ServerMessageType type, String message) {
        super(type);
        super.message = message;
    }
}

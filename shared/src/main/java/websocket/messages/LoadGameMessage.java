package websocket.messages;

public class LoadGameMessage extends ServerMessage {

    public LoadGameMessage(ServerMessageType type, String message) {
        super(type);
        super.message = message;
    }
}

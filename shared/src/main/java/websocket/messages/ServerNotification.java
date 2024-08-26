package websocket.messages;

public class ServerNotification extends ServerMessage {

    public ServerNotification(ServerMessageType type, String message) {
        super(type);
        super.message = message;
    }

}

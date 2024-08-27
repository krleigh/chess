package websocket.messages;

import model.GameData;

public class LoadGameMessage extends ServerMessage {

    GameData game;

    public LoadGameMessage(ServerMessageType type, String message, GameData game) {
        super(type);
        super.message = message;
        this.game = game;
    }

    public GameData getGame() {
        return game;
    }
}

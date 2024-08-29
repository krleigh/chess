package websocket;

import model.GameData;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

public interface GameHandler {
    void notify(ServerMessage serverMessage);
    void updateGame(GameData game);
    void notifyError(ErrorMessage errorMessage);
}

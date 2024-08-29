package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, String move) {
        super(commandType, authToken, gameID);
        super.move = move;
    }

}

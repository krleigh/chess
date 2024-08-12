package Server.requestresult;

import chess.ChessGame;

public record JoinRequest(int gameID, ChessGame.TeamColor playerColor) {
}

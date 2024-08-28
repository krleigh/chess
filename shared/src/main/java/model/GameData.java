package model;

import chess.ChessGame;

public record GameData
        (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game, GameStatus gameStatus) {

    public enum GameStatus {
        ONGOING,
        OVER
    }
}

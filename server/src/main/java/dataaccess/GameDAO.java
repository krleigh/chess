package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import service.requestresult.CreateRequest;

import java.util.Collection;

public interface GameDAO {

    GameData createGame(CreateRequest request) throws ResponseException;

    GameData[] listGames() throws ResponseException;

    GameData getGame(Integer gameID) throws ResponseException;

    GameData updateGame(Integer gameID, GameData newGame) throws ResponseException;

    void deleteGame(Integer gameID) throws ResponseException;

    void deleteAllGames() throws ResponseException;
}
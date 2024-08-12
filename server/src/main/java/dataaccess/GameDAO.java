package dataaccess;

import exception.ResponseException;
import model.GameData;
import Server.requestresult.CreateRequest;

public interface GameDAO {

    GameData createGame(CreateRequest request) throws ResponseException;

    GameData[] listGames() throws ResponseException;

    GameData getGame(Integer gameID) throws ResponseException;

    GameData updateGame(Integer gameID, GameData newGame) throws ResponseException;

    void deleteGame(Integer gameID) throws ResponseException;

    void deleteAllGames() throws ResponseException;
}
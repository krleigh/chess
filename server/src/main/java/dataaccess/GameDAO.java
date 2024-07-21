package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import service.requestresult.CreateRequest;

import java.util.Collection;

public interface GameDAO {

    GameData createGame(String username, CreateRequest request) throws ResponseException;

    GameData[] listGames() throws ResponseException;

    GameData getGame(Integer gameID) throws ResponseException;

    GameData updateGame(Integer gameID, GameData newGame) throws ResponseException;

    void deleteGame(Integer username) throws ResponseException;

    void deleteAllGames() throws ResponseException;
}
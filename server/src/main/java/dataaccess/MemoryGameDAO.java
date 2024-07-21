package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import service.requestresult.CreateRequest;
import service.requestresult.JoinRequest;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int nextID = 1;

    public GameData createGame(String username, CreateRequest request) throws ResponseException {
        GameData game = new GameData(nextID++, null, null, request.gameName(), new ChessGame());

        games.put(game.gameID(), game);
        return game;
    }

    public GameData[] listGames() throws ResponseException {
        GameData[] gamesArray = new GameData[games.size()];
        games.values().toArray(gamesArray);
        return gamesArray;
    }

    public GameData getGame(Integer gameID) throws ResponseException {
        return games.get(gameID);
    }

    public GameData updateGame(Integer gameID, GameData newGame) throws ResponseException {
        games.put(gameID, newGame);
        return newGame;
    }

    public void deleteGame(Integer gameID) throws ResponseException {
        games.remove(gameID);
    }

    public void deleteAllGames() throws ResponseException {
        games.clear();
    }
}

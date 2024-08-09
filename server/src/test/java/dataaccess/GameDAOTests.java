package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import service.requestresult.CreateRequest;
import service.requestresult.RegisterRequest;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTests {

    private GameDAO getDataAccess(Class<? extends GameDAO> databaseClass) throws ResponseException, DataAccessException {
        GameDAO gamedb;
        if (databaseClass.equals(MySQLGameDAO.class)) {
            gamedb = new MySQLGameDAO();
        } else {
            gamedb = new MemoryGameDAO();
        }
        gamedb.deleteAllGames();
        return gamedb;
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void addGameTest(Class<? extends GameDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);

        var request = new CreateRequest("my cool new game");
        var username = gameDAO.createGame(request).gameName();
        assertEquals("my cool new game", username);

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void addGameBadRequestTest(Class<? extends GameDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);

        Exception exception = assertThrows(Exception.class, () -> {
            gameDAO.createGame(null);
        });
    }


    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void listGamesTest(Class<? extends GameDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);
        ArrayList<Integer> expectedGames = new ArrayList<>();
        expectedGames.add(gameDAO.getGame(gameDAO.createGame(new CreateRequest("cool game 01")).gameID()).gameID());
        expectedGames.add(gameDAO.getGame(gameDAO.createGame(new CreateRequest("whale of a game")).gameID()).gameID());

        ArrayList<Integer> actualGames = new ArrayList<>();
        for (var game : gameDAO.listGames()) {
            actualGames.add(game.gameID());
        }

        assertArrayEquals(expectedGames.toArray(), actualGames.toArray());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void listGamesEmptyTest(Class<? extends GameDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);
        GameData[] empty = {};
        assertArrayEquals(empty, gameDAO.listGames());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void getGameTest(Class<? extends GameDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);

        int gameID = gameDAO.createGame(new CreateRequest("my awesome game")).gameID();
        assertEquals("my awesome game", gameDAO.getGame(gameID).gameName());

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void getGameNullTest(Class<? extends GameDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);
        assertEquals(null, gameDAO.getGame(34));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void updateGameTest(Class<? extends GameDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);

        var oldgame = gameDAO.createGame(new CreateRequest("my coolest game"));
        GameData newGame = new GameData(oldgame.gameID(), oldgame.whiteUsername(), oldgame.blackUsername(), "cool new name", new ChessGame());
        gameDAO.updateGame(oldgame.gameID(), newGame);
        assertEquals("cool new name", gameDAO.getGame(oldgame.gameID()).gameName());
        assertNotEquals(oldgame.game(), gameDAO.getGame(oldgame.gameID()).game());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void updateGameNegativeTest(Class<? extends GameDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);

        var oldgame = gameDAO.createGame(new CreateRequest("my coolest game"));

        Exception exception = assertThrows(Exception.class, () -> {
            gameDAO.updateGame(oldgame.gameID(), null);
        });

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void deleteGameTest(Class<? extends GameDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);

        var result = gameDAO.createGame(new CreateRequest("a new game"));

        assertEquals(1, gameDAO.listGames().length);

        gameDAO.deleteGame(result.gameID());

        assertEquals(0, gameDAO.listGames().length);

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void deleteAllGamesTest(Class<? extends GameDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);

        gameDAO.createGame(new CreateRequest("game 01"));
        gameDAO.createGame(new CreateRequest("game 02"));
        gameDAO.createGame(new CreateRequest("game 03"));

        assertEquals(3, gameDAO.listGames().length);

        gameDAO.deleteAllGames();

        assertEquals(0, gameDAO.listGames().length);
    }

}
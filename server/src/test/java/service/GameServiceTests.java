package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requestresult.CreateRequest;
import service.requestresult.JoinRequest;
import service.requestresult.RegisterRequest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {

    static final GameService GAME_SERVICE = new GameService();
    static final UserService USER_SERVICE = new UserService();

    @BeforeEach
    void clear() throws ResponseException, DataAccessException {
        GAME_SERVICE.deleteAllGames();
        USER_SERVICE.deleteAllUsers();
    }

    @Test
    void createGameTest() throws ResponseException {
        GAME_SERVICE.createGame(new CreateRequest("My Cool New Game"));
        var games = GAME_SERVICE.listGames();

        assertEquals(1, games.length);
        assertEquals("My Cool New Game", games[0].gameName());
    }

    @Test
    void createGameBadRequestTest() throws ResponseException {
        Exception exception = assertThrows(ResponseException.class, () -> {
            GAME_SERVICE.createGame(new CreateRequest(""));
        });
        var games = GAME_SERVICE.listGames();
        assertEquals(0, games.length);
    }

    @Test
    void joinGameTest() throws ResponseException, DataAccessException {
        USER_SERVICE.registerUser(new RegisterRequest("lugan", "bbwhale", "whale@ghwale.com"));
        int gameID = GAME_SERVICE.createGame(new CreateRequest("lugan's game")).gameID();
        GAME_SERVICE.joinGame("lugan", new JoinRequest(gameID, ChessGame.TeamColor.WHITE));

        assertEquals("lugan", GAME_SERVICE.getGame(gameID).whiteUsername());
    }

    @Test
    void joinGameTakenColorTest() throws ResponseException, DataAccessException {
        USER_SERVICE.registerUser(new RegisterRequest("lugan", "bbwhale", "whale@ghwale.com"));
        USER_SERVICE.registerUser(new RegisterRequest("ella", "elliphanti", "ele@phant.com"));
        int gameID = GAME_SERVICE.createGame(new CreateRequest("lugan's game")).gameID();
        GAME_SERVICE.joinGame("lugan", new JoinRequest(gameID, ChessGame.TeamColor.WHITE));

        Exception exception = assertThrows(ResponseException.class, () -> {
            GAME_SERVICE.joinGame("ella", new JoinRequest(gameID, ChessGame.TeamColor.WHITE));
        });
    }

    @Test
    void listGamesTest() throws ResponseException{
        ArrayList<GameData> expectedGames = new ArrayList<>();
        expectedGames.add(GAME_SERVICE.getGame(GAME_SERVICE.createGame(new CreateRequest("cool game 01")).gameID()));
        expectedGames.add(GAME_SERVICE.getGame(GAME_SERVICE.createGame(new CreateRequest("whale of a game")).gameID()));
        expectedGames.add(GAME_SERVICE.getGame(GAME_SERVICE.createGame(new CreateRequest("hello whale")).gameID()));
        expectedGames.add(GAME_SERVICE.getGame(GAME_SERVICE.createGame(new CreateRequest("my game")).gameID()));

        assertArrayEquals(expectedGames.toArray(), GAME_SERVICE.listGames());

    }

    @Test
    void listGamesNoGamesTest() throws  ResponseException{
        GameData[] empty = {};
        assertArrayEquals(empty, GAME_SERVICE.listGames());
    }

    @Test
    void getGameTest() throws ResponseException {
        int gameID = GAME_SERVICE.createGame(new CreateRequest("my awesome game")).gameID();
        assertEquals("my awesome game", GAME_SERVICE.getGame(gameID).gameName());
    }

    @Test
    void getGameDoesNotExistTest() throws ResponseException{
        Exception exception = assertThrows(ResponseException.class, () -> {
            GAME_SERVICE.getGame(3);
        });
    }

    @Test
    void deleteGame() throws ResponseException {
        var result = GAME_SERVICE.createGame(new CreateRequest("a new game"));

        assertEquals(1, GAME_SERVICE.listGames().length);

        GAME_SERVICE.deleteGame(result.gameID());

        assertEquals(0, GAME_SERVICE.listGames().length);
    }

    @Test
    void deleteAllGamesTest() throws ResponseException{
        GAME_SERVICE.createGame(new CreateRequest("game 01"));
        GAME_SERVICE.createGame(new CreateRequest("game 02"));
        GAME_SERVICE.createGame(new CreateRequest("game 03"));

        assertEquals(3, GAME_SERVICE.listGames().length);

        GAME_SERVICE.deleteAllGames();

        assertEquals(0, GAME_SERVICE.listGames().length);


    }

}

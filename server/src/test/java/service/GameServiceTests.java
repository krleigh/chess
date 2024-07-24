package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requestresult.CreateRequest;
import service.requestresult.JoinRequest;
import service.requestresult.LoginRequest;
import service.requestresult.RegisterRequest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {

    static final GameService gameService = new GameService(new MemoryGameDAO());
    static final UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());

    @BeforeEach
    void clear() throws ResponseException {
        gameService.deleteAllGames();
        userService.deleteAllUsers();
    }

    @Test
    void createGameTest() throws ResponseException {
        gameService.createGame(new CreateRequest("My Cool New Game"));
        var games = gameService.listGames();

        assertEquals(1, games.length);
        assertEquals("My Cool New Game", games[0].gameName());
    }

    @Test
    void createGameBadRequestTest() throws ResponseException {
        Exception exception = assertThrows(ResponseException.class, () -> {
            gameService.createGame(new CreateRequest(""));
        });
        var games = gameService.listGames();
        assertEquals(0, games.length);
    }

    @Test
    void joinGameTest() throws ResponseException{
        userService.registerUser(new RegisterRequest("lugan", "bbwhale", "whale@ghwale.com"));
        int gameID = gameService.createGame(new CreateRequest("lugan's game")).gameID();
        gameService.joinGame("lugan", new JoinRequest(gameID, ChessGame.TeamColor.WHITE));

        assertEquals("lugan", gameService.getGame(gameID).whiteUsername());
    }

    @Test
    void joinGameTakenColorTest() throws ResponseException{
        userService.registerUser(new RegisterRequest("lugan", "bbwhale", "whale@ghwale.com"));
        userService.registerUser(new RegisterRequest("ella", "elliphanti", "ele@phant.com"));
        int gameID = gameService.createGame(new CreateRequest("lugan's game")).gameID();
        gameService.joinGame("lugan", new JoinRequest(gameID, ChessGame.TeamColor.WHITE));

        Exception exception = assertThrows(ResponseException.class, () -> {
            gameService.joinGame("ella", new JoinRequest(gameID, ChessGame.TeamColor.WHITE));
        });
    }

    @Test
    void listGamesTest() throws ResponseException{
        ArrayList<GameData> expectedGames = new ArrayList<>();
        expectedGames.add(gameService.getGame(gameService.createGame(new CreateRequest("cool game 01")).gameID()));
        expectedGames.add(gameService.getGame(gameService.createGame(new CreateRequest("whale of a game")).gameID()));
        expectedGames.add(gameService.getGame(gameService.createGame(new CreateRequest("hello whale")).gameID()));
        expectedGames.add(gameService.getGame(gameService.createGame(new CreateRequest("my game")).gameID()));

        assertArrayEquals(expectedGames.toArray(), gameService.listGames());

    }

    @Test
    void listGamesNoGamesTest() throws  ResponseException{
        GameData[] empty = {};
        assertArrayEquals(empty, gameService.listGames());
    }

    @Test
    void getGameTest() throws ResponseException {
        int gameID = gameService.createGame(new CreateRequest("my awesome game")).gameID();
        assertEquals("my awesome game", gameService.getGame(gameID).gameName());
    }

    @Test
    void getGameDoesNotExistTest() throws ResponseException{
        Exception exception = assertThrows(ResponseException.class, () -> {
            gameService.getGame(3);
        });
    }

    @Test
    void deleteAllGamesTest() throws ResponseException{
        gameService.createGame(new CreateRequest("game 01"));
        gameService.createGame(new CreateRequest("game 02"));
        gameService.createGame(new CreateRequest("game 03"));

        assertEquals(3, gameService.listGames().length);

        gameService.deleteAllGames();

        assertEquals(0, gameService.listGames().length);


    }

}

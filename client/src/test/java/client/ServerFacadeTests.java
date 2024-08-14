package client;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import serverfacade.requestresult.*;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);

        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void clear() throws ResponseException {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerUserTest() throws ResponseException {
        var authData = serverFacade.registerUser(new RegisterRequest("lugan", "secretpassword", "fish@ewhale.com"));
        assertEquals(authData.username(), "lugan");
    }

    @Test
    public void registerUserExistsTest() throws ResponseException {
        serverFacade.registerUser(new RegisterRequest("lugan", "secretpassword", "fish@ewhale.com"));
        assertThrows(ResponseException.class, () -> {
            serverFacade.registerUser(new RegisterRequest("lugan", "secretpassword", "fish@ewhale.com"));
        });
    }

    @Test
    public void loginTest() throws ResponseException {
        var authData = serverFacade.registerUser(new RegisterRequest("lugan", "secretpassword", "fish@ewhale.com"));
        serverFacade.logout(authData.authToken());
        var newAuth = serverFacade.login(new LoginRequest("lugan", "secretpassword"));
        assertDoesNotThrow(() -> {
            serverFacade.listGames(newAuth.authToken());
        });

    }

    @Test
    public void loginWrongPasswordTest() throws ResponseException {
        var authData = serverFacade.registerUser(new RegisterRequest("lugan", "secretpassword", "fish@ewhale.com"));
        serverFacade.logout(authData.authToken());
        assertThrows( ResponseException.class, () -> {
            serverFacade.login(new LoginRequest("lugan", "wrongpassword"));
        });
    }

    @Test
    public void logoutTest() throws ResponseException {
        var authData = serverFacade.registerUser(new RegisterRequest("lugan", "secretpassword", "fish@ewhale.com"));
        serverFacade.logout(authData.authToken());
        assertThrows(ResponseException.class, () -> {
            serverFacade.listGames(authData.authToken());
        });
    }

    @Test
    public void logoutAlreadyLoggedOutTest() throws ResponseException {
        var authData = serverFacade.registerUser(new RegisterRequest("lugan", "secretpassword", "fish@ewhale.com"));
        serverFacade.logout(authData.authToken());
        assertThrows(ResponseException.class, () -> {
            serverFacade.logout(authData.authToken());
        });
    }

    @Test
    public void listGamesTest() throws ResponseException {
        var authData = serverFacade.registerUser(new RegisterRequest("perivanwinkle", "eight88", "octo@ewhale.com"));
        var gameID = serverFacade.createGame(new CreateRequest("cool octopi game"), authData.authToken()).gameID();
        var games = serverFacade.listGames(authData.authToken());
        assert(games.getGames()[0].gameID() == gameID);
    }

    @Test
    public void listGamesBadAuthTest() throws ResponseException {
        assertThrows(ResponseException.class, () -> {
            serverFacade.listGames("badauth");
        });
    }

    @Test
    public void createGameTest() throws ResponseException {
        var authData = serverFacade.registerUser(new RegisterRequest("ella", "phantphant", "peanut@ewhale.com"));
        var gameID = serverFacade.createGame(new CreateRequest("cool elephant game"), authData.authToken()).gameID();
        var games = serverFacade.listGames(authData.authToken());
        assert(games.getGames()[0].gameID() == gameID && Objects.equals(games.getGames()[0].gameName(), "cool elephant game"));
    }

    @Test
    public void createGameBadAuthTest() throws ResponseException {
        assertThrows(ResponseException.class, () -> {
            serverFacade.createGame(new CreateRequest("cool elephant game"), "badauth");
        });
    }

    @Test
    public void joinGameTest() throws ResponseException {
        var authData = serverFacade.registerUser(new RegisterRequest("ella", "phantphant", "peanut@ewhale.com"));
        var gameID = serverFacade.createGame(new CreateRequest("cool elephant game"), authData.authToken()).gameID();
        serverFacade.joinGame(new JoinRequest(gameID, ChessGame.TeamColor.BLACK), authData.authToken());
        var games = serverFacade.listGames(authData.authToken());
        assertEquals("ella", games.getGames()[0].blackUsername());
    }

    @Test
    public void joinGameTakenTest() throws ResponseException {
        var authDataElla = serverFacade.registerUser(new RegisterRequest("ella", "phantphant", "peanut@ewhale.com"));
        var authDataLugan = serverFacade.registerUser(new RegisterRequest("lugan", "whalewhale", "pearl@ewhale.com"));
        var gameID = serverFacade.createGame(new CreateRequest("cool elephant game"), authDataElla.authToken()).gameID();
        serverFacade.joinGame(new JoinRequest(gameID, ChessGame.TeamColor.BLACK), authDataElla.authToken());
        assertThrows(ResponseException.class, () -> {
            serverFacade.joinGame(new JoinRequest(gameID, ChessGame.TeamColor.BLACK), authDataLugan.authToken());
        });
    }

    @Test
    public void clearTest() throws ResponseException {
        var authDataElla = serverFacade.registerUser(new RegisterRequest("ella", "phantphant", "peanut@ewhale.com"));
        var authDataLugan = serverFacade.registerUser(new RegisterRequest("lugan", "whalewhale", "pearl@ewhale.com"));
        var gameID = serverFacade.createGame(new CreateRequest("cool elephant game"), authDataElla.authToken()).gameID();
        serverFacade.clear();

        assertDoesNotThrow(() -> {
            serverFacade.registerUser(new RegisterRequest("lugan", "whalewhale", "pearl@ewhale.com"));
        });

        var newAuth = serverFacade.registerUser(new RegisterRequest("ella", "phantphant", "peanut@ewhale.com"));
        GameData[] empty = {};
        assertArrayEquals(empty, serverFacade.listGames(newAuth.authToken()).getGames());
    }

}

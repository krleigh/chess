package service;


import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requestresult.CreateRequest;
import service.requestresult.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTests {

    private static final MemoryUserDAO userDAO = new MemoryUserDAO();
    private static final MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private static final MemoryGameDAO gameDAO = new MemoryGameDAO();

    static final ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
    static final UserService userService = new UserService(userDAO, authDAO);
    static final GameService gameService = new GameService(gameDAO);

    @BeforeEach
    void clear() throws ResponseException {
        clearService.clear();
    }

    @BeforeEach
    void addObjects() throws ResponseException {
        userService.registerUser(new RegisterRequest("user1", "goodpassword", "my@email.com"));
        userService.registerUser(new RegisterRequest("coolcat", "123kittens", "mew@mew.com"));
        gameService.createGame(new CreateRequest("my game"));
        gameService.createGame(new CreateRequest("cools"));
    }

    @Test
    void clearTest() throws ResponseException {
        var userlist = userService.listUsers();
        var authList = authDAO.listAuths();
        var gameList = gameDAO.listGames();

        assertNotEquals(0, userlist.size());
        assertNotEquals(0, authList.size());
        assertNotEquals(0, gameList.length);

        clearService.clear();

        userlist = userService.listUsers();
        authList = authDAO.listAuths();
        gameList = gameDAO.listGames();

        assertEquals(0, userlist.size());
        assertEquals(0, authList.size());
        assertEquals(0, gameList.length);

    }


}

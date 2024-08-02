package service;


import dataaccess.DataAccessException;
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

    private static final MemoryUserDAO USER_DAO = new MemoryUserDAO();
    private static final MemoryAuthDAO AUTH_DAO = new MemoryAuthDAO();
    private static final MemoryGameDAO GAME_DAO = new MemoryGameDAO();

    static final ClearService CLEAR_SERVICE = new ClearService(USER_DAO, AUTH_DAO, GAME_DAO);
    static final UserService USER_SERVICE = new UserService(USER_DAO, AUTH_DAO);
    static final GameService GAME_SERVICE = new GameService(GAME_DAO);

    @BeforeEach
    void clear() throws ResponseException, DataAccessException {
        CLEAR_SERVICE.clear();
    }

    @BeforeEach
    void addObjects() throws ResponseException, DataAccessException {
        USER_SERVICE.registerUser(new RegisterRequest("user1", "goodpassword", "my@email.com"));
        USER_SERVICE.registerUser(new RegisterRequest("coolcat", "123kittens", "mew@mew.com"));
        GAME_SERVICE.createGame(new CreateRequest("my game"));
        GAME_SERVICE.createGame(new CreateRequest("cools"));
    }

    @Test
    void clearTest() throws ResponseException, DataAccessException {
        var userlist = USER_SERVICE.listUsers();
        var authList = AUTH_DAO.listAuths();
        var gameList = GAME_DAO.listGames();

        assertNotEquals(0, userlist.size());
        assertNotEquals(0, authList.size());
        assertNotEquals(0, gameList.length);

        CLEAR_SERVICE.clear();

        userlist = USER_SERVICE.listUsers();
        authList = AUTH_DAO.listAuths();
        gameList = GAME_DAO.listGames();

        assertEquals(0, userlist.size());
        assertEquals(0, authList.size());
        assertEquals(0, gameList.length);

    }


}

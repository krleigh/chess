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



    static final UserService USER_SERVICE = new UserService();
    static final GameService GAME_SERVICE = new GameService();
    static final ClearService CLEAR_SERVICE = new ClearService(USER_SERVICE, GAME_SERVICE);

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
        var authList = USER_SERVICE.listAuths();
        var gameList = GAME_SERVICE.listGames();

        assertNotEquals(0, userlist.size());
        assertNotEquals(0, authList.size());
        assertNotEquals(0, gameList.length);

        CLEAR_SERVICE.clear();

        userlist = USER_SERVICE.listUsers();
        authList = USER_SERVICE.listAuths();
        gameList = GAME_SERVICE.listGames();

        assertEquals(0, userlist.size());
        assertEquals(0, authList.size());
        assertEquals(0, gameList.length);

    }


}

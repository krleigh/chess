package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requestresult.LoginRequest;
import service.requestresult.RegisterRequest;
import service.requestresult.RegisterResult;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    static final UserService SERVICE = new UserService();

    @BeforeEach
    void clear() throws ResponseException {
        SERVICE.deleteAllUsers();
    }

    @Test
    void registerUserTest() throws ResponseException {
        var register = new RegisterRequest("lugan", "bbwhale", "whale@gwhale.com");
        var registerResult = SERVICE.registerUser(register);
        System.out.println(registerResult);

        var users = SERVICE.listUsers();
        assertEquals(1, users.size());
        assertTrue(users.contains(new UserData(register.username(), register.password(), register.email())));
    }

    @Test
    void registerBadRequestTest() throws ResponseException {
        var register = new RegisterRequest(null, null, "hello@world.com");
        Exception exception = assertThrows(ResponseException.class, () -> {
            SERVICE.registerUser(register);
        });
        var users = SERVICE.listUsers();
        assertEquals(0, users.size());
    }

    @Test
    void loginTest() throws ResponseException {
        RegisterResult result = SERVICE.registerUser(new RegisterRequest("lugan", "cutewhale", "whale@gwhale.com"));
        SERVICE.logout(result.authToken());
        SERVICE.login(new LoginRequest("lugan", "cutewhale"));
        AuthData[] auths = new AuthData[1];
        SERVICE.listAuths().toArray(auths);
        assertEquals("lugan", auths[0].username());
    }

    @Test
    void loginBadPasswordTest() throws ResponseException {
        RegisterResult result = SERVICE.registerUser(new RegisterRequest("lugan", "cutewhale", "whale@gwhale.com"));
        SERVICE.logout(result.authToken());
        Exception exception = assertThrows(ResponseException.class, () -> {
            SERVICE.login(new LoginRequest("lugan", "wrongpassword"));
        });

        assertEquals(0, SERVICE.listAuths().size());

    }

    @Test
    void logoutTest() throws ResponseException, DataAccessException {
        RegisterResult result = SERVICE.registerUser(new RegisterRequest("lugan", "cutewhale", "whale@gwhale.com"));
        assertEquals(1, SERVICE.listAuths().size());
        SERVICE.logout(result.authToken());
        assertEquals(0, SERVICE.listAuths().size());
    }

    @Test
    void logoutNotAuthorizedTest() {
        Exception exception = assertThrows(ResponseException.class, () -> {
            SERVICE.logout("1290");
        });
    }

    @Test
    void authenticateTest() throws ResponseException, DataAccessException {
        RegisterResult result = SERVICE.registerUser(new RegisterRequest("lugan", "cutewhale", "whale@gwhale.com"));
        assertEquals("lugan", SERVICE.authenticate(result.authToken()));
    }

    @Test
    void authenticateInvalidTest() throws ResponseException {
        Exception exception = assertThrows(ResponseException.class, () -> {
            SERVICE.authenticate("1290");
        });
    }

    @Test
    void listUsersTest() throws ResponseException, DataAccessException {
        Collection<UserData> users = new ArrayList<>();
        users.add(SERVICE.getUser(SERVICE.registerUser(new RegisterRequest("lu", "password", "ewhale@gwhale.com")).username()));
        users.add(SERVICE.getUser(SERVICE.registerUser(new RegisterRequest("ella", "phantword", "trunk@peanut.com")).username()));

        assertTrue(SERVICE.listUsers().containsAll(users));
    }

    @Test
    void listUsersNoneTest() throws ResponseException {
        assertEquals(0, SERVICE.listUsers().size());
    }

    @Test
    void listAuthsTest() throws ResponseException, DataAccessException {
        Collection<AuthData> auths  = new ArrayList<>();
        var lugan = SERVICE.registerUser(new RegisterRequest("lu", "password", "ewhale@gwhale.com"));
        var ella = SERVICE.registerUser(new RegisterRequest("ella", "phantword", "trunk@peanut.com"));
        auths.add(new AuthData(lugan.authToken(), lugan.username()));
        auths.add(new AuthData(ella.authToken(), ella.username()));

        assertTrue(SERVICE.listAuths().containsAll(auths));
    }

    @Test
    void listAuthsNoneTest() throws ResponseException {
        assertEquals(0, SERVICE.listAuths().size());
    }

    @Test
    void getUserTest() throws ResponseException, DataAccessException {
        var register = SERVICE.registerUser(new RegisterRequest("lugan", "whaleword", "whales@ewhal.com"));
        assertEquals(new UserData("lugan", "whaleword", "whales@ewhal.com"), SERVICE.getUser(register.username()));
    }

    @Test
    void getUserDoesNotExistTest() {
        Exception exception = assertThrows(ResponseException.class, () -> {
            SERVICE.getUser("lugan");
        });
    }

    @Test
    void deleteUser() throws ResponseException, DataAccessException {
        var register = SERVICE.registerUser(new RegisterRequest("lugan", "whaleword", "whale@ewhale.com"));
        assertEquals(new UserData("lugan", "whaleword", "whale@ewhale.com"), SERVICE.getUser(register.username()));
        SERVICE.deleteUser("lugan");
        assertEquals(0, SERVICE.listUsers().size());
    }

    @Test
    void deleteAllUsersTest() throws ResponseException, DataAccessException {
        Collection<UserData> users = new ArrayList<>();
        users.add(SERVICE.getUser(SERVICE.registerUser(new RegisterRequest("lu", "password", "ewhale@gwhale.com")).username()));
        users.add(SERVICE.getUser(SERVICE.registerUser(new RegisterRequest("ella", "phantword", "trunk@peanut.com")).username()));

        assertTrue(SERVICE.listUsers().containsAll(users));

        SERVICE.deleteAllUsers();

        assertEquals(0, SERVICE.listUsers().size());
    }



}

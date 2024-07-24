package service;

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
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    static final UserService service = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());

    @BeforeEach
    void clear() throws ResponseException {
        service.deleteAllUsers();
    }

    @Test
    void registerUserTest() throws ResponseException {
        var register = new RegisterRequest("lugan", "bbwhale", "whale@gwhale.com");
        var registerResult = service.registerUser(register);
        System.out.println(registerResult);

        var users = service.listUsers();
        assertEquals(1, users.size());
        assertTrue(users.contains(new UserData(register.username(), register.password(), register.email())));
    }

    @Test
    void registerBadRequestTest() throws ResponseException {
        var register = new RegisterRequest(null, null, "hello@world.com");
        Exception exception = assertThrows(ResponseException.class, () -> {
            service.registerUser(register);
        });
        var users = service.listUsers();
        assertEquals(0, users.size());
    }

    @Test
    void loginTest() throws ResponseException {
        RegisterResult result = service.registerUser(new RegisterRequest("lugan", "cutewhale", "whale@gwhale.com"));
        service.logout(result.authToken());
        service.login(new LoginRequest("lugan", "cutewhale"));
        AuthData[] auths = new AuthData[1];
        service.listAuths().toArray(auths);
        assertEquals("lugan", auths[0].username());
    }

    @Test
    void loginBadPasswordTest() throws ResponseException {
        RegisterResult result = service.registerUser(new RegisterRequest("lugan", "cutewhale", "whale@gwhale.com"));
        service.logout(result.authToken());
        Exception exception = assertThrows(ResponseException.class, () -> {
            service.login(new LoginRequest("lugan", "wrongpassword"));
        });

        assertEquals(0, service.listAuths().size());

    }

    @Test
    void logoutTest() throws ResponseException {
        RegisterResult result = service.registerUser(new RegisterRequest("lugan", "cutewhale", "whale@gwhale.com"));
        assertEquals(1, service.listAuths().size());
        service.logout(result.authToken());
        assertEquals(0, service.listAuths().size());
    }

    @Test
    void logoutNotAuthorizedTest() {
        Exception exception = assertThrows(ResponseException.class, () -> {
            service.logout("1290");
        });
    }

    @Test
    void authenticateTest() throws ResponseException {
        RegisterResult result = service.registerUser(new RegisterRequest("lugan", "cutewhale", "whale@gwhale.com"));
        assertEquals("lugan", service.authenticate(result.authToken()));
    }

    @Test
    void authenticateInvalidTest() throws ResponseException {
        Exception exception = assertThrows(ResponseException.class, () -> {
            service.authenticate("1290");
        });
    }

    @Test
    void listUsersTest() throws ResponseException {
        Collection<UserData> users = new ArrayList<>();
        users.add(service.getUser(service.registerUser(new RegisterRequest("lu", "password", "ewhale@gwhale.com")).username()));
        users.add(service.getUser(service.registerUser(new RegisterRequest("ella", "phantword", "trunk@peanut.com")).username()));

        assertTrue(service.listUsers().containsAll(users));
    }

    @Test
    void listUsersNoneTest() throws ResponseException {
        assertEquals(0, service.listUsers().size());
    }

    @Test
    void listAuthsTest() throws ResponseException {
        Collection<AuthData> auths  = new ArrayList<>();
        var lugan = service.registerUser(new RegisterRequest("lu", "password", "ewhale@gwhale.com"));
        var ella = service.registerUser(new RegisterRequest("ella", "phantword", "trunk@peanut.com"));
        auths.add(new AuthData(lugan.authToken(), lugan.username()));
        auths.add(new AuthData(ella.authToken(), ella.username()));

        assertTrue(service.listAuths().containsAll(auths));
    }

    @Test
    void listAuthsNoneTest() throws ResponseException {
        assertEquals(0, service.listAuths().size());
    }

    @Test
    void getUserTest() throws ResponseException {
        var register = service.registerUser(new RegisterRequest("lugan", "whaleword", "whales@ewhal.com"));
        assertEquals(new UserData("lugan", "whaleword", "whales@ewhal.com"), service.getUser(register.username()));
    }

    @Test
    void getUserDoesNotExistTest() {
        Exception exception = assertThrows(ResponseException.class, () -> {
            service.getUser("lugan");
        });
    }

    @Test
    void deleteUser() throws ResponseException {
        var register = service.registerUser(new RegisterRequest("lugan", "whaleword", "whale@ewhale.com"));
        assertEquals(new UserData("lugan", "whaleword", "whale@ewhale.com"), service.getUser(register.username()));
        service.deleteUser("lugan");
        assertEquals(0, service.listUsers().size());
    }

    @Test
    void deleteAllUsersTest() throws ResponseException {
        Collection<UserData> users = new ArrayList<>();
        users.add(service.getUser(service.registerUser(new RegisterRequest("lu", "password", "ewhale@gwhale.com")).username()));
        users.add(service.getUser(service.registerUser(new RegisterRequest("ella", "phantword", "trunk@peanut.com")).username()));

        assertTrue(service.listUsers().containsAll(users));

        service.deleteAllUsers();

        assertEquals(0, service.listUsers().size());
    }



}

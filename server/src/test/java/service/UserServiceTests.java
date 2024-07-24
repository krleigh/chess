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

import java.util.Arrays;

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
    void authenticateTest() {

    }

    @Test
    void authenticateInvalidTest() {

    }

    @Test
    void listUsersTest() {

    }

    @Test
    void listUsersNoneTest() {

    }

    @Test
    void getUserTest() {

    }

    @Test
    void getUserDoesNotExistTest(){

    }

    @Test
    void deleteUser() {

    }

    @Test
    void deleteAllUsersTest() {

    }



}

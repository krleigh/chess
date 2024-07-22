package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requestresult.RegisterRequest;

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




}

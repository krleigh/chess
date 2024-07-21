package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    static final UserService service = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());

    @BeforeEach
    void clear() {
        service.deleteAllUsers();
    }

    @Test
    void registerUserTest() {
        var register = new RegisterRequest("lugan", "bbwhale", "whale@gwhale.com");
        var registerResult = service.registerUser(register);
        System.out.println(registerResult);

        var users = service.listUsers();
        assertEquals(1, users.size());
        assertTrue(users.contains(new UserData(register.username(), register.password(), register.email())));
    }




}

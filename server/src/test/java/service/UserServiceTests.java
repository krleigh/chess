package service;

import dataaccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    static final UserService service = new UserService(new MemoryUserDAO());

    @BeforeEach
    void clear() {
        service.deleteAllUsers();
    }

    @Test
    void registerUserTest() {
        var user = new UserData("lugan", "bbwhale", "whale@gwhale.com");
        user = service.registerUser(user);

        var users = service.listUsers();
        assertEquals(1, users.size());
        assertTrue(users.contains(user));
    }


}

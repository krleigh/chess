package dataaccess;

import exception.ResponseException;
import model.UserData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mindrot.jbcrypt.BCrypt;
import service.requestresult.RegisterRequest;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTests {

    private UserDAO getDataAccess(Class<? extends UserDAO> databaseClass) throws ResponseException, DataAccessException {
        UserDAO userdb;
        if (databaseClass.equals(MySQLUserDAO.class)) {
            userdb = new MySQLUserDAO();
        } else {
            userdb = new MemoryUserDAO();
        }
        userdb.deleteAllUsers();
        return userdb;
    }


    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void createUserTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);

        var request = new RegisterRequest("lugan", "whaleword", "ewhale@ewhale.org");
        var username = userDAO.createUser(request).username();
        assertEquals("lugan", username);

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void createUserBadRequestTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);

        var register = new RegisterRequest(null, null, "whale@world.com");
        Exception exception = assertThrows(ResponseException.class, () -> {
            userDAO.createUser(register);
        });
        var users = userDAO.listUsers();
        assertEquals(0, users.size());

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void listUsersTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);

        ArrayList<UserData> expectedUsers = new ArrayList<>();
        expectedUsers.add(userDAO.createUser(new RegisterRequest("lugan", "whaleword", "ewhale@gwhale.org")));
        expectedUsers.add(userDAO.createUser(new RegisterRequest("ella", "secretphant", "peanut@email.com")));
        expectedUsers.add(userDAO.createUser(new RegisterRequest("cheddar", "thedragon75", "cheese@cmail.com")));

        assertTrue(userDAO.listUsers().containsAll(expectedUsers));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void listUsersNoneTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);

        assertEquals(0, userDAO.listUsers().size());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void getUserTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);

        var register = userDAO.createUser(new RegisterRequest("lugan", "whaleword", "whales@ewhal.com"));
        assertEquals("whales@ewhal.com", userDAO.getUser("lugan").email());
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void getUserDoesNotExistTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);
        assertEquals(null, userDAO.getUser("lugan"));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void deleteUser(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);

        var register = userDAO.createUser(new RegisterRequest("lugan", "whaleword", "whale@ewhale.com"));
        assertNotEquals(0, userDAO.listUsers().size());
        userDAO.deleteUser("lugan");
        assertEquals(null, userDAO.getUser("lugan"));
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLUserDAO.class, MemoryUserDAO.class})
    void deleteAllUsersTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);

        Collection<UserData> users = new ArrayList<>();
        users.add(userDAO.getUser(userDAO.createUser(new RegisterRequest("lu", "password", "ewhale@gwhale.com")).username()));
        users.add(userDAO.getUser(userDAO.createUser(new RegisterRequest("ella", "phantword", "trunk@peanut.com")).username()));

        assertNotEquals(0, userDAO.listUsers().size());

        userDAO.deleteAllUsers();

        assertEquals(0, userDAO.listUsers().size());
    }

}

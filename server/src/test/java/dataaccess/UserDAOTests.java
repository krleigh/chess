package dataaccess;

import exception.ResponseException;
import model.UserData;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import service.requestresult.RegisterRequest;

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
    void addUserTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        UserDAO userDAO = getDataAccess(dbClass);

        var request = new RegisterRequest("lugan", "whaleword", "ewhale@ewhale.org");
        var username = userDAO.createUser(request).username();
        assertEquals("lugan", username);

    }



}

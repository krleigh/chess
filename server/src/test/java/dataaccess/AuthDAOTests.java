package dataaccess;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import service.requestresult.RegisterRequest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTests {

    private AuthDAO getDataAccess(Class<? extends UserDAO> databaseClass) throws ResponseException, DataAccessException {
        AuthDAO authdb;
        if (databaseClass.equals(MySQLUserDAO.class)) {
            authdb = new MySQLAuthDAO();
        } else {
            authdb = new MemoryAuthDAO();
        }
        authdb.deleteAllAuths();
        return authdb;
    }


    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void createAuthTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        AuthDAO authDAO = getDataAccess(dbClass);

        assertNotEquals(null, authDAO.createAuth("lugan").authToken());

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void createAuthNullTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        AuthDAO authDAO = getDataAccess(dbClass);

        Exception exception = assertThrows(ResponseException.class, () -> {
            authDAO.createAuth(null);
        });

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void listAuthsTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        AuthDAO authDAO = getDataAccess(dbClass);

        ArrayList<AuthData> expectedAuths = new ArrayList<>();
        expectedAuths.add(authDAO.createAuth("lugan"));
        expectedAuths.add(authDAO.createAuth("ella"));

        assert(authDAO.listAuths().containsAll(expectedAuths));

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void listNoAuthsTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        AuthDAO authDAO = getDataAccess(dbClass);

        assertEquals(0, authDAO.listAuths().size());

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void getAuthTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        AuthDAO authDAO = getDataAccess(dbClass);

        var auth = authDAO.createAuth("lugan").authToken();

        assertEquals(auth, authDAO.getAuth(auth).authToken());

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void getAuthNegativeTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        AuthDAO authDAO = getDataAccess(dbClass);

        assertEquals(null, authDAO.getAuth("293057047023203"));

    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void deleteAuthTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        AuthDAO authDAO = getDataAccess(dbClass);

        var auth = authDAO.createAuth("lugan").authToken();

        assertEquals(auth, authDAO.getAuth(auth).authToken());

        authDAO.deleteAuth(auth);

        assertEquals(null, authDAO.getAuth(auth));

    }


    @ParameterizedTest
    @ValueSource(classes = {MySQLAuthDAO.class, MemoryAuthDAO.class})
    void deleteAllAuthsTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        AuthDAO authDAO = getDataAccess(dbClass);

        var auth = authDAO.createAuth("lugan").authToken();

        assertEquals(auth, authDAO.getAuth(auth).authToken());

        authDAO.deleteAllAuths();

        assertEquals(0, authDAO.listAuths().size());

    }








}

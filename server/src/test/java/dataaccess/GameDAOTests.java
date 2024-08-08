package dataaccess;

import exception.ResponseException;
import model.UserData;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import service.requestresult.CreateRequest;
import service.requestresult.RegisterRequest;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTests {

    private GameDAO getDataAccess(Class<? extends UserDAO> databaseClass) throws ResponseException, DataAccessException {
        GameDAO gamedb;
        if (databaseClass.equals(MySQLGameDAO.class)) {
            gamedb = new MySQLGameDAO();
        } else {
            gamedb = new MemoryGameDAO();
        }
//        gamedb.deleteAllGames();
        return gamedb;
    }

    @ParameterizedTest
    @ValueSource(classes = {MySQLGameDAO.class, MemoryGameDAO.class})
    void addGameTest(Class<? extends UserDAO> dbClass) throws ResponseException, DataAccessException {
        GameDAO gameDAO = getDataAccess(dbClass);

        var request = new CreateRequest("my cool new game");
        var username = gameDAO.createGame(request).gameName();
        assertEquals("my cool new game", username);

    }

}
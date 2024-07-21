package dataaccess;

import exception.ResponseException;
import model.UserData;
import service.RegisterRequest;

import java.util.Collection;

public interface UserDAO {

    UserData createUser(RegisterRequest register) throws ResponseException ;

    Collection<UserData> listUsers() throws ResponseException ;

    UserData getUser(String username) throws ResponseException ;

    void deleteUser(String username) throws ResponseException ;

    void deleteAllUsers() throws ResponseException ;
}

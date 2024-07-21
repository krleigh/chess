package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.Collection;

public interface AuthDAO {

    AuthData createAuth(String username) throws ResponseException ;

    Collection<AuthData> listAuths() throws ResponseException ;

    AuthData getAuth(String authToken) throws ResponseException ;

    void deleteAuth(String authToken) throws ResponseException ;

    void deleteAllAuths() throws ResponseException ;
}
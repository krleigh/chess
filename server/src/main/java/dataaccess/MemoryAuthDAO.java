package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String,AuthData> auths = new HashMap<>();

    public AuthData createAuth(String username) throws ResponseException  {
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);

        auths.put(auth.authToken(), auth);
        return auth;
    }

    public Collection<AuthData> listAuths() throws ResponseException {
        return auths.values();
    }

    public AuthData getAuth(String authToken) throws ResponseException {
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) throws ResponseException {
        auths.remove(authToken);
    }

    public void deleteAllAuths() throws ResponseException {
        auths.clear();
    }

}

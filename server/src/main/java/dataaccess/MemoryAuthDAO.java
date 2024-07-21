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

        auths.put(auth.username(), auth);
        return auth;
    }

    public Collection<AuthData> listAuths() throws ResponseException {
        return auths.values();
    }

    public AuthData getAuth(String username) throws ResponseException {
        return auths.get(username);
    }

    public void deleteAuth(String username) throws ResponseException {
        auths.remove(username);
    }

    public void deleteAllAuths() throws ResponseException {
        auths.clear();
    }

}

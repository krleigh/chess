package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    final private HashMap<String,AuthData> auths = new HashMap<>();

    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);

        auths.put(auth.username(), auth);
        return auth;
    }

    public Collection<AuthData> listAuths() {
        return auths.values();
    }

    public AuthData getAuth(String username) {
        return auths.get(username);
    }

    public void deleteAuth(String username) {
        auths.remove(username);
    }

    public void deleteAllAuths() {
        auths.clear();
    }

}

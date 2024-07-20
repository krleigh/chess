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

        auths.put(auth.authToken(), auth);
        return auth;
    }

    Collection<AuthData> listAuths() {

    }

    AuthData getAuth(String username) {

    }

    void deleteAuth(String username) {

    }

    void deleteAllAuths() {

    }

}

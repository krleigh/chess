package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.Collection;

public interface AuthDAO {

    AuthData createAuth(String username);

    Collection<AuthData> listAuths();

    AuthData getAuth(String username);

    void deleteAuth(String username);

    void deleteAllAuths();
}
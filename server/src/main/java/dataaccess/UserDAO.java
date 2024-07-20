package dataaccess;

import model.UserData;
import service.RegisterRequest;

import java.util.Collection;

public interface UserDAO {

    UserData createUser(RegisterRequest register);

    Collection<UserData> listUsers();

    UserData getUser(String username);

    void deleteUser(String username);

    void deleteAllUsers();
}

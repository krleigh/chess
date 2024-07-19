package dataaccess;

import model.UserData;

import java.util.Collection;

public interface UserDAO {

    UserData registerUser(UserData user);

    Collection<UserData> listUsers();

    UserData getUser(String username);

    void deleteUser(String username);

    void deleteAllUsers();
}

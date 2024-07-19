package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {

    final private HashSet<UserData> users = new HashSet<>();

    public UserData registerUser(UserData user) {
        user = new UserData(user.username(), user.password(), user.email());

        users.add(user);
        return user;
    }

}

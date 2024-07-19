package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String,UserData> users = new HashMap<>();

    public UserData registerUser(UserData user) {
        user = new UserData(user.username(), user.password(), user.email());

        users.put(user.username(), user);
        return user;
    }

    public Collection<UserData> listUsers() {
        return users.values();
    }

    public UserData getUser(String username){
        return  users.get(username);
    }

    public void deleteUser(String username){
        users.remove(username);
    }

    public void deleteAllUsers(){
        users.clear();
    }

}

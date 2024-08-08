package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.requestresult.RegisterRequest;

import java.util.Collection;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    final private HashMap<String,UserData> users = new HashMap<>();

    public UserData createUser(RegisterRequest register) throws ResponseException {
        UserData user = new UserData(register.username(), register.password(), register.email());

        users.put(user.username(), user);
        return user;
    }

    public Collection<UserData> listUsers() throws ResponseException {
        return users.values();
    }

    public UserData getUser(String username) throws ResponseException {
        return  users.get(username);
    }

    public void deleteUser(String username) throws ResponseException {
        users.remove(username);
    }

    public void deleteAllUsers() throws ResponseException {
        users.clear();
    }

}

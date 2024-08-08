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
        if (register.username() == null || register.password() == null || register.email() == null){
            throw new ResponseException(400, "Error: bad request");
        }
        UserData user = new UserData(register.username(), register.password(), register.email());

        users.put(user.username(), user);
        return user;
    }

    public Collection<UserData> listUsers() throws ResponseException {
        return users.values();
    }

    public UserData getUser(String username) throws ResponseException {
        var user = users.get(username);
        if (user != null) {
            return user;
        } else{
            throw new ResponseException(500, "Error: User does not exist");
        }
    }

    public void deleteUser(String username) throws ResponseException {
        users.remove(username);
    }

    public void deleteAllUsers() throws ResponseException {
        users.clear();
    }

}

package service;

import dataaccess.UserDAO;
import model.UserData;

import java.util.Collection;

public class UserService {

    private final UserDAO dataAccess;

    public UserService(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public UserData registerUser(UserData user) {
        return dataAccess.registerUser(user);
    }

    public Collection<UserData> listUsers() {
        return dataAccess.listUsers();
    }

    public UserData getUser(String username) {
        return dataAccess.getUser(username);
    }

    public void deletePet(String username)  {
        dataAccess.deleteUser(username);
    }

    public void deleteAllUsers() {
        dataAccess.deleteAllUsers();
    }
}

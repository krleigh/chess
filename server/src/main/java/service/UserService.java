package service;

import dataaccess.UserDAO;
import model.UserData;

public class UserService {

    private final UserDAO dataAccess;

    public UserService(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public UserData registerUser(UserData user) {
        return dataAccess.registerUser(user);
    }
}

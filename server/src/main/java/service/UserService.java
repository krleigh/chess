package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Collection;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult registerUser(RegisterRequest register) {

        if (userDAO.getUser(register.username()) == null) {
           UserData user = userDAO.createUser(register);
           AuthData auth = authDAO.createAuth(register.username());
           return new RegisterResult(user.username(), auth.authToken());
        } else {
            return new RegisterResult(null, null);
        }
    }

    public Collection<UserData> listUsers() {
        return userDAO.listUsers();
    }

    public UserData getUser(String username) {
        return userDAO.getUser(username);
    }

    public void deleteUser(String username)  {
        userDAO.deleteUser(username);
    }

    public void deleteAllUsers() {
        userDAO.deleteAllUsers();
    }
}

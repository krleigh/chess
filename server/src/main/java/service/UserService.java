package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
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

    public RegisterResult registerUser(RegisterRequest register) throws ResponseException {
        if (register.username() == null || register.password() == null || register.email() == null){
            throw new ResponseException(400, "Error : Missing field");
        }
        if (userDAO.getUser(register.username()) != null) {
            throw new ResponseException(403, "Error : Username already exists");}

        UserData user = userDAO.createUser(register);
        AuthData auth = authDAO.createAuth(register.username());
        return new RegisterResult(user.username(), auth.authToken());
    }

    public Collection<UserData> listUsers() throws ResponseException {
        return userDAO.listUsers();
    }

    public UserData getUser(String username) throws ResponseException {
        return userDAO.getUser(username);
    }

    public void deleteUser(String username) throws ResponseException {
        userDAO.deleteUser(username);
        authDAO.deleteAuth(username);
    }

    public void deleteAllUsers() throws ResponseException {
        userDAO.deleteAllUsers();
        authDAO.deleteAllAuths();
    }
}

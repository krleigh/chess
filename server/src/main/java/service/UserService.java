package service;

import dataaccess.*;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import service.requestresult.*;
import spark.Response;

import java.util.Collection;
import java.util.Objects;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private org.mindrot.jbcrypt.BCrypt BCrypt;

    public UserService() {
        UserDAO temp;
        try {
            temp = new MySQLUserDAO();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            temp = new MemoryUserDAO();
        }
        this.userDAO = temp;
        AuthDAO tempauth;
        try {
            tempauth = new MySQLAuthDAO();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            tempauth = new MemoryAuthDAO();
        }
        this.authDAO = tempauth;
    }

    public RegisterResult registerUser(RegisterRequest register) throws ResponseException{
        if (register.username() == null || register.password() == null || register.email() == null) {
            throw new ResponseException(400, "Error: bad request");
        }

        if (userDAO.getUser(register.username()) != null) {
            throw new ResponseException(403, "Error: already taken");}
        String hashedPassword = BCrypt.hashpw(register.password(), BCrypt.gensalt());
        RegisterRequest hashedRegister = new RegisterRequest(register.username(), hashedPassword, register.email());
        UserData user = userDAO.createUser(hashedRegister);
        AuthData auth = authDAO.createAuth(register.username());
        return new RegisterResult(user.username(), auth.authToken());
    }

    public LoginResult login(LoginRequest login) throws ResponseException {

        var user = userDAO.getUser(login.username());
        if(user == null || !BCrypt.checkpw(login.password(), user.password())) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        AuthData auth = authDAO.createAuth(login.username());
        return new LoginResult(login.username(), auth.authToken());
    }

    public void logout(String authToken) throws ResponseException {

        if (authDAO.getAuth(authToken) == null ) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        authDAO.deleteAuth(authToken);

    }

    public String authenticate(String authToken) throws ResponseException {
        var auth = authDAO.getAuth(authToken);
        if (auth == null ) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        return auth.username();
    }

    public Collection<UserData> listUsers() throws ResponseException {
        return userDAO.listUsers();
    }

    public UserData getUser(String username) throws ResponseException {
        var user = userDAO.getUser(username);
        if (user == null ) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        return user;
    }

    public Collection<AuthData> listAuths() throws ResponseException {
        return authDAO.listAuths();
    }

    public void deleteUser(String username) throws ResponseException{
        userDAO.deleteUser(username);
        authDAO.deleteAuth(username);
    }

    public void deleteAllUsers() throws ResponseException{
        userDAO.deleteAllUsers();
        authDAO.deleteAllAuths();
    }
}

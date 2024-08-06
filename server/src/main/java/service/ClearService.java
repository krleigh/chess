package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;

public class ClearService {

    private final UserService userService;

    private final GameService gameService;

    public ClearService(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    public void clear() throws ResponseException, DataAccessException {
        userService.deleteAllUsers();
        gameService.deleteAllGames();
    }

}

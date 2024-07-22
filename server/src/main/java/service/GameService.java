package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.GameData;
import service.requestresult.CreateRequest;
import service.requestresult.CreateResult;
import service.requestresult.GameListResult;
import service.requestresult.JoinRequest;

import java.util.Collection;

public class GameService {

    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public CreateResult createGame(CreateRequest request) throws ResponseException {

        var game = gameDAO.createGame(request);

        return new CreateResult(game.gameID());
    }

    public void joinGame(String username, JoinRequest request) throws ResponseException {
        Integer gameID = request.gameID();
        if (gameDAO.getGame(gameID) == null){ throw new ResponseException(400, "Error: bad request");}
        ChessGame.TeamColor color = request.playerColor();

        GameData oldGame = gameDAO.getGame(gameID);

        if (color == ChessGame.TeamColor.BLACK && oldGame.blackUsername() !=null ||
                color == ChessGame.TeamColor.WHITE && oldGame.whiteUsername() != null)
         { throw new ResponseException(403, "Error: already taken"); }

        GameData newGame;

        if (color == ChessGame.TeamColor.BLACK) {
            newGame = new GameData(gameID, oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
        } else if (color == ChessGame.TeamColor.WHITE){
            newGame = new GameData(gameID, username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        } else {
            throw new ResponseException(400, "Error: bad request");
        }

        gameDAO.updateGame(gameID, newGame);

    }

    public GameData[] listGames() throws ResponseException {
        return gameDAO.listGames();
    }

    public void deleteAllGames() throws ResponseException {
        gameDAO.deleteAllGames();
    }
}

package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.GameData;
import serverfacade.requestresult.CreateRequest;
import serverfacade.requestresult.CreateResult;
import serverfacade.requestresult.JoinRequest;

import java.util.Objects;

public class GameService {

    private final GameDAO gameDAO;

    public GameService() {
        GameDAO temp;
        try {
            temp = new MySQLGameDAO();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            temp = new MemoryGameDAO();
        }
        this.gameDAO = temp;
    }

    public CreateResult createGame(CreateRequest request) throws ResponseException {
        if (Objects.equals(request.gameName(), "")) { throw new ResponseException(400, "Error: bad Request"); }
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

    public void leaveGame(String username, Integer gameID) throws ResponseException {
        if (gameDAO.getGame(gameID) == null){ throw new ResponseException(400, "Error: bad request");}
        GameData oldGame = gameDAO.getGame(gameID);

        GameData newGame;

        if (Objects.equals(username, oldGame.blackUsername())) {
            newGame = new GameData(gameID, oldGame.whiteUsername(), null, oldGame.gameName(), oldGame.game());
        } else if (Objects.equals(username, oldGame.whiteUsername())) {
            newGame = new GameData(gameID, null, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        } else {
            return;
        }

        gameDAO.updateGame(gameID, newGame);
    }

    public GameData[] listGames() throws ResponseException {
        return gameDAO.listGames();
    }

    public GameData getGame(int gameID) throws ResponseException {
        GameData game = gameDAO.getGame(gameID);
        if (game == null) {throw new ResponseException(400, "Error: game does not exist");}
        return game;
    }

    public void deleteGame(int gameID) throws ResponseException {
        gameDAO.deleteGame(gameID);
    }

    public void deleteAllGames() throws ResponseException {
        gameDAO.deleteAllGames();
    }

    public void updateGame(Integer gameID, GameData newgame) throws ResponseException {
        gameDAO.updateGame(gameID, newgame);
    }
}

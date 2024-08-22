package ui;

import chess.ChessGame;
import com.sun.nio.sctp.NotificationHandler;
import exception.ResponseException;
import model.GameData;
import serverfacade.ServerFacade;
import serverfacade.requestresult.*;

import java.util.Arrays;
import java.util.Objects;

public class Client {
    private String authToken;
    private String username;
    private DrawBoard board;
    private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;
    private State state = State.LOGGED_OUT;


    public Client (String serverUrl, Repl repl) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.repl = repl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "clear" -> clear();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            state = State.LOGGED_IN;
            RegisterResult result = server.registerUser(new RegisterRequest(params[0], params[1], params[2]));
            authToken = result.authToken();
            username = result.username();
            return String.format("You registered as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            state = State.LOGGED_IN;
            LoginResult result = server.login(new LoginRequest(params[0], params[1]));
            username = params[0];
            authToken = result.authToken();
            return String.format("You logged in as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String list() throws ResponseException {
        assertLoggedIn();
        var games = server.listGames(authToken);
        for (var game : games.getGames()){
            System.out.println(game.gameID() + " " + game.gameName() + "\n");
        }
        return "Games listed";
    }

    public String create(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            CreateResult result = server.createGame(new CreateRequest(params[0]), authToken);
            return String.format("Game %S created.", params[0]);
        } else {
            throw new ResponseException(400, "Expected: <game name>, names cannot include spaces");
        }
    }

    public String join(String...params) throws ResponseException {
      assertLoggedIn();
      if (state == State.GAMEPLAY){
          return "Please leave current game to join another.";
      }
        if (params.length == 2) {
            ChessGame.TeamColor teamColor;
            Integer gameID = Integer.parseInt(params[0]);
            if (Objects.equals(params[1], "white")) {
                 teamColor = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(params[1], "black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            } else {
                throw new ResponseException(400, "Expected: <game id> <team color>, for team color, input \"white\" or \"black\"");
            }
            server.joinGame(new JoinRequest(gameID , teamColor), authToken);
            var game = findGame(gameID, server.listGames(authToken).getGames());
            board = new DrawBoard(game, teamColor);
            board.draw();
            state = State.GAMEPLAY;
            return String.format("Joined game %s", gameID );
        }
        throw new ResponseException(400, "Expected: <game id> <team color>. For team color, input 0 for white and 1 for black.");
    }

    public String logout() throws ResponseException {
        assertLoggedIn();
        server.logout(authToken);
        authToken = null;
        state = State.LOGGED_OUT;
        return String.format("%s logged out", username);
    }

    private String observe(String...params) throws ResponseException {
       assertLoggedIn();
        if (state == State.GAMEPLAY){
            return "Please leave current game to join another.";
        }
        if (params.length == 1) {
            Integer gameID = Integer.parseInt(params[0]);
            var games = server.listGames(authToken);
            var game = findGame(gameID, games.getGames());
            if (game == null) {
                throw new ResponseException(400, "Error: Invalid game id");
            } else {
                board = new DrawBoard(game, ChessGame.TeamColor.WHITE);
                board.draw();
            }
            state = State.GAMEPLAY;
            return String.format("Observing game %s", gameID );
        }
        throw new ResponseException(400, "Expected: <game id>");
    }

    private GameData findGame(Integer gameID, GameData[] games) {
        for (var game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    private String clear() throws ResponseException {
        server.clear();
        return "Database cleared";
    }

    public String help() {
        if (state == State.LOGGED_IN) {
            return """
                    - create <NAME> - a game
                    - list - games
                    - join <ID> [WHITE|BLACK] - a game
                    - observe <ID> - a game
                    - logout - when you are done
                    - quit - playing chess
                    - help - with possible commands
                    """;
        } else if (state == State.GAMEPLAY) {
            return """
                        - leave - leave current game
                        - help - with possible commands
                        """;
        }
            return """
                    - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    - login <USERNAME> <PASSWORD> - to play chess
                    - quit - playing chess
                    - help - with possible commands
                    """;
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == State.LOGGED_OUT) {
            throw new ResponseException(400, "Error: Please log in");
        }
    }


}

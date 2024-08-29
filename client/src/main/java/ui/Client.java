package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import exception.ResponseException;
import model.GameData;
import serverfacade.ServerFacade;
import serverfacade.requestresult.*;
import websocket.GameHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Client {
    private String authToken;
    private String username;

    private final ServerFacade server;
    private final String serverUrl;
    private final GameHandler repl;
    public WebSocketFacade ws;
    private State state = State.LOGGED_OUT;


    private final HashMap<Integer, GameData> games = new HashMap<>();
    private String gameName;
    private DrawBoard board;


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
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "show" -> show(params);
                case "move" -> move(params);
                case "resign" -> resign();
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
            assertLoggedOut();
            state = State.LOGGED_IN;
            RegisterResult result = server.registerUser(new RegisterRequest(params[0], params[1], params[2]));
            authToken = result.authToken();
            username = result.username();
            return String.format("You registered as %s.", params[0]) + "\n" + help();
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (state != State.LOGGED_OUT) {
            return String.format("Already logged in as %s", username);
        }
        if (params.length == 2) {
            state = State.LOGGED_IN;
            LoginResult result = server.login(new LoginRequest(params[0], params[1]));
            username = params[0];
            authToken = result.authToken();
            return String.format("You logged in as %s.", params[0])+ "\n" + help();
        }
        throw new ResponseException(400, "Expected: <username> <password>");
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

    public String list() throws ResponseException {
        assertLoggedIn();
        listHelper(true);
        return "Games listed";
    }

    private void listHelper(Boolean print) throws ResponseException {
        var gameslist = server.listGames(authToken);
        Integer key = 1;
        for (var game : gameslist.getGames()){
            this.games.put(key, game);
            if (print) {
                System.out.println(key + " " + game.gameName() + " | white player: " + game.whiteUsername() +
                        " | black player: " + game.blackUsername() + "\n");
            }
            ++key;
        }
    }

    public String join(String...params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 2) {

            ChessGame.TeamColor teamColor;
            Integer id;

            try{id = Integer.parseInt(params[0]);}
            catch (Exception e) {throw new ResponseException(400,
                    "Expected: <game id> <team color>, for team color, input \"white\" or \"black\"");}

            listHelper(false);
            if (!games.containsKey(id)) { return "Invalid game ID.";}


            if (Objects.equals(params[1], "white")) {
                teamColor = ChessGame.TeamColor.WHITE;
            } else if (Objects.equals(params[1], "black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            } else {
                throw new ResponseException(400, "Expected: <game id> <team color>, for team color, input \"white\" or \"black\"");
            }

            var gameID = games.get(id).gameID();
            if (teamColor == ChessGame.TeamColor.WHITE && Objects.equals(games.get(id).whiteUsername(), username) ||
                    teamColor == ChessGame.TeamColor.BLACK && Objects.equals(games.get(id).blackUsername(), username)){

            } else {
                server.joinGame(new JoinRequest(gameID , teamColor), authToken);
            }
            state = State.GAMEPLAY;
            ws = new WebSocketFacade(serverUrl, repl, gameID, teamColor);
            ws.connect(authToken);

            this.gameName = games.get(id).gameName();

            return String.format("Joined game %s", gameID)+ "\n" + help();
        }
        throw new ResponseException(400, "Expected: <game id> <team color>. For team color, input black or white");
    }

    private String observe(String...params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            Integer gameID = Integer.parseInt(params[0]);
            var games = server.listGames(authToken);
            var game = findGame(gameID, games.getGames());
            if (game == null) {
                throw new ResponseException(400, "Error: Invalid game id");
            } else {
                ws = new WebSocketFacade(serverUrl, repl, gameID, ChessGame.TeamColor.WHITE);
                ws.connect(authToken);
                gameName = game.gameName();
            }
            state = State.OBSERVE;

            return String.format("Observing game %s", gameName)+ "\n" + help();
        }
        throw new ResponseException(400, "Expected: <game id>");
    }

    public String logout() throws ResponseException {
        assertLoggedIn();
        server.logout(authToken);
        authToken = null;
        state = State.LOGGED_OUT;
        return String.format("%s logged out", username);
    }


    public String redraw() throws ResponseException {
        assertObserveOrGameplay();
        ws.redraw(authToken);
        return "Board redrawn";
    }

    public String leave() throws ResponseException {
        assertObserveOrGameplay();
        ws.leave(authToken);
        state = State.LOGGED_IN;
        gameName = null;
        return "Left game.";
    }

    public String show(String...params) throws ResponseException {
        assertObserveOrGameplay();
        ChessPosition position = makeChessPosition(params[0]);
        ws.show(authToken, position);
        return String.format("Displaying valid moves for %s", position);
    }

    public String move(String...params) throws ResponseException {
        assertGamePlay();
        ChessMove move = makeChessMove(params[0]);
        ws.move(authToken, move);
        return String.format("Attempting move %s", move);
    }

    public ChessMove makeChessMove(String move) {
        String[] position = move.split(">");
        ChessPosition start = makeChessPosition(position[0]);
        ChessPosition end = makeChessPosition(position[1]);

        ChessPiece.PieceType promP = null;

        if (position.length == 3){
            String piece = position[2];
            if (Objects.equals(piece, "queen")){ promP = ChessPiece.PieceType.QUEEN;}
            if (Objects.equals(piece, "rook")){ promP = ChessPiece.PieceType.ROOK;}
            if (Objects.equals(piece, "bishop")){ promP = ChessPiece.PieceType.BISHOP;}
            if (Objects.equals(piece, "knight")){ promP = ChessPiece.PieceType.KNIGHT;}
        }

        return new ChessMove(start, end, promP);
    }

    public ChessPosition makeChessPosition(String position) {
        Integer column = position.charAt(0) - 'a' + 1;
        Integer row = Character.getNumericValue(position.charAt(1));
        return new ChessPosition(row, column);
    }

    public String resign() throws ResponseException {
        assertGamePlay();
        ws.resign(authToken);
        gameName = null;
        return "Resigned game.";
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
        state = State.LOGGED_OUT;
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
                    - help - with possible commands
                    """;
        } else if (state == State.GAMEPLAY) {
            return """
                        - redraw - redraw the chess board
                        - leave - leave current game
                        - show <ChessPosition> - show legal moves for piece in this position
                        - move <ChessMove> - make a move
                        - resign - resign current game
                        - help - with possible commands
                        """;
        } else if (state == State.OBSERVE) {
            return """
                        - redraw - redraw the chess board
                        - leave - leave current game
                        - show <ChessPosition> - show legal moves for piece in this position
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
        if (state != State.LOGGED_IN) {
            if (state == State.GAMEPLAY || state == State.OBSERVE) {
                throw new ResponseException(400, "Error: Please exit gameplay or observe mode");}
            throw new ResponseException(400, "Error: Please log in");
        }
    }

    private void assertGamePlay() throws ResponseException {
        if (state != State.GAMEPLAY) {
            throw new ResponseException(400, "Error: not in gameplay mode");
        }
    }

    private void assertLoggedOut() throws ResponseException {
        if (state != State.LOGGED_OUT) {
            if (state == State.GAMEPLAY || state == State.OBSERVE) {
                throw new ResponseException(400, "Error: Please exit gameplay or observe mode");}
            throw new ResponseException(400, "Error: Please log out");
        }
    }

    private void assertObserveOrGameplay() throws ResponseException {
        if (state != State.OBSERVE && state != State.GAMEPLAY) {
            throw new ResponseException(400, "Error: not in gameplay or observe mode");
        }
    }


}

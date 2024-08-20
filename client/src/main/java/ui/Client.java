package ui;

import com.sun.nio.sctp.NotificationHandler;
import exception.ResponseException;
import serverfacade.ServerFacade;
import serverfacade.requestresult.LoginRequest;
import serverfacade.requestresult.LoginResult;
import serverfacade.requestresult.RegisterRequest;
import serverfacade.requestresult.RegisterResult;

import java.util.Arrays;

public class Client {
    private String authToken;
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
                case "create" -> create();
                case "list" -> list();
                case "join" -> join();
                case "observe" -> observe();
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
            return String.format("You registered as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <username>, <password>, <email>");
//        return "\n register";
    }

    public String login(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length >= 2) {
            LoginResult result = server.login(new LoginRequest(params[0], params[1]));
            authToken = result.authToken();
            return String.format("You logged in as %s.", params[0]);
        }
        throw new ResponseException(400, "Expected: <name> <CAT|DOG|FROG>");
//        return "\n login";
    }

    public String list() throws ResponseException {
//        assertSignedIn();
//        var pets = server.listPets();
//        var result = new StringBuilder();
//        var gson = new Gson();
//        for (var pet : pets) {
//            result.append(gson.toJson(pet)).append('\n');
//        }
//        return result.toString();\
        return "\n list";
    }

    public String create(String... params) throws ResponseException {
//        assertSignedIn();
//        if (params.length == 1) {
//            try {
//                var id = Integer.parseInt(params[0]);
//                var pet = getPet(id);
//                if (pet != null) {
//                    server.deletePet(id);
//                    return String.format("%s says %s", pet.name(), pet.sound());
//                }
//            } catch (NumberFormatException ignored) {
//            }
//        }
//        throw new ResponseException(400, "Expected: <pet id>");
        return "\n create";
    }

    public String join() throws ResponseException {
//        assertSignedIn();
//        var buffer = new StringBuilder();
//        for (var pet : server.listPets()) {
//            buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
//        }
//
//        server.deleteAllPets();
//        return buffer.toString();
        return "\n join";
    }

    public String logout() throws ResponseException {
//        assertSignedIn();
//        ws.leavePetShop(visitorName);
//        ws = null;
//        state = State.SIGNEDOUT;
//        return String.format("%s left the shop", visitorName);
        return "\n logout";
    }

    private String observe() throws ResponseException {
//        for (var pet : server.listPets()) {
//            if (pet.id() == id) {
//                return pet;
//            }
//        }
//        return null;
        return "\n observe";
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

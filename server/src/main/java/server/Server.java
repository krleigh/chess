package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import service.ClearService;
import service.GameService;
import service.requestresult.*;
import service.UserService;
import spark.*;

public class Server {

    private final UserService userService;
    private final GameService gameService;

    private final Gson gson = new Gson();

    public Server()  {
        userService = new UserService();
        gameService = new GameService();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clear);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();

        Spark.awaitInitialization();
        System.out.println("Listening on port " + desiredPort);
        return Spark.port();
    }

    public int port() {
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(gson.toJson(new ErrorResult(ex.statusCode(), ex.getMessage())));
    }



    private Object registerUser(Request req, Response res) throws ResponseException, DataAccessException {
        var register = gson.fromJson(req.body(), RegisterRequest.class);

        var registerResult = userService.registerUser(register);
        res.status(200);
        res.header("Authorization", registerResult.authToken());
        res.body(gson.toJson(registerResult));

        return res.body();
    }

    private Object login(Request req, Response res) throws ResponseException {
        var user = gson.fromJson(req.body(), LoginRequest.class);
        var loginResult = userService.login(user);
        res.body(gson.toJson(loginResult));
        return res.body();
    }

    private Object logout(Request req, Response res) throws ResponseException {
        String authToken = req.headers("Authorization").toString();
        userService.logout(authToken);
        res.body("{}");
        return res.body();
    }

    private Object createGame(Request req, Response res) throws ResponseException {
        String authToken = req.headers("Authorization").toString();
        var username = userService.authenticate(authToken);

        var createRequest = gson.fromJson(req.body(), CreateRequest.class);

        res.body(gson.toJson(gameService.createGame(createRequest)));
        return res.body();
    }

    private Object joinGame(Request req, Response res) throws ResponseException {
        String authToken = req.headers("Authorization");
        var username = userService.authenticate(authToken);

        var joinRequest = gson.fromJson(req.body(), JoinRequest.class);

        gameService.joinGame(username, joinRequest);

        res.body("{}");
        return res.body();
    }

    private Object listGames(Request req, Response res) throws ResponseException {
        String authToken = req.headers("Authorization");
        var username = userService.authenticate(authToken);

        res.type("application/json");
        var list = gameService.listGames();
        GameListResult objList = new GameListResult(list);
        return gson.toJson(objList);
    }

    private Object clear(Request req, Response res) throws ResponseException, DataAccessException {
        new ClearService(userService, gameService).clear();
        res.status(200);
        return "{}";
    }
}

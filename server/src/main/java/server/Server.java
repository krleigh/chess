package server;

import com.google.gson.Gson;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exception.ResponseException;
import service.requestresult.ErrorResult;
import service.requestresult.LoginRequest;
import service.requestresult.RegisterRequest;
import service.UserService;
import spark.*;

public class Server {

    private final UserService userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
    private final Gson gson = new Gson();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
//        Spark.delete("/session", this::logout);
//        Spark.get("/game", this::listGames);
//        Spark.post("/game", this::createGame);
//        Spark.put("/game", this::joinGame);
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
        res.status(ex.StatusCode());
        res.body(new Gson().toJson(new ErrorResult(ex.StatusCode(), ex.getMessage())));
    }

    private Object registerUser(Request req, Response res) throws ResponseException {
        var register = new Gson().fromJson(req.body(), RegisterRequest.class);

        var registerResult = userService.registerUser(register);
        res.status(200);
        res.header("Auth Token: ", registerResult.authToken());
        res.body(new Gson().toJson(registerResult));

        return res.body();
    }

    private Object login(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), LoginRequest.class);
        var loginResult = userService.login(user);
        return new Gson().toJson(loginResult);
    }

//    private Object logout(Request req, Response res) {
//
//    }






    private Object clear(Request req, Response res) throws ResponseException {
        userService.deleteAllUsers();
        res.status(200);
        return "";
    }
}

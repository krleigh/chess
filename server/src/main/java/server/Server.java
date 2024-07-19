package server;

import com.google.gson.Gson;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import service.UserService;
import model.UserData;
import spark.*;

public class Server {

    private final UserService userService = new UserService(new MemoryUserDAO());

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::registerUser);
//        Spark.post("/pet", this::addPet);
//        Spark.get("/pet", this::listPets);
//        Spark.delete("/pet/:id", this::deletePet);
        Spark.delete("/db", this::clear);


        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

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

    private Object registerUser(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), UserData.class);
        userService.registerUser(user);
        return new Gson().toJson(user);
    }

    private Object clear(Request req, Response res) {
        userService.deleteAllUsers();
        res.status(204);
        return "";
    }
}

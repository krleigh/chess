package server;

import com.google.gson.Gson;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import service.UserService;
import model.UserData;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", this::registerUser);

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
        UserService service = new UserService(new MemoryUserDAO());
        user = service.registerUser(user);
        return new Gson().toJson(user);
    }
}

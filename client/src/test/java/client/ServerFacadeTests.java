package client;

import exception.ResponseException;
import serverfacade.requestresult.RegisterRequest;
import serverfacade.requestresult.RegisterResult;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerUserTest() throws ResponseException {
        var authData = serverFacade.registerUser(new RegisterRequest("lugan", "secretpassword", "fish@ewhale.com"));
        assertTrue(authData.authToken().length() > 10);
    }

}

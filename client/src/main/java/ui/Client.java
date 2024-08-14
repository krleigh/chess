package ui;

import com.sun.nio.sctp.NotificationHandler;
import serverfacade.ServerFacade;

public class Client {
    private String authToken;
    private final ServerFacade server;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;


    public Client (String serverUrl, NotificationHandler notificationHandler) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }



}

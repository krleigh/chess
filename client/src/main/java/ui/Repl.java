package ui;



public class Repl  {

    private final Client client;

    public Repl(String serverUrl) {
        client = new Client(serverUrl, this);
    }

    public void run() {

    }


    public void notify() {

    }
}

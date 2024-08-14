package serverfacade;

import serverfacade.requestresult.*;
import com.google.gson.Gson;
import exception.ResponseException;
import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public RegisterResult registerUser(RegisterRequest register) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, register, RegisterResult.class);
    }

    public LoginResult login(LoginRequest login) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, login, LoginResult.class);
    }

    public void logout(String auth) throws ResponseException{
        var path = "/session";
        this.makeRequest("DELETE", path, auth, null);
    }

    public GameListResult listGames(String auth) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, auth, GameListResult.class);
    }

    public CreateResult createGame(CreateRequest create) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, create, CreateResult.class);
    }

    public void joinGame(JoinRequest join) throws ResponseException {
        var path = "game";
        this.makeRequest("PUT", path, join, null);
    }

    public void clear() throws ResponseException{

    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
//            System.out.print(serverUrl);
            URL url = (new URI(serverUrl + path)).toURL();

            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
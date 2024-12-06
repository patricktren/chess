package server;

import chess.ChessGame;
import com.google.gson.Gson;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import exception.Message;
import exception.ResponseException;
import model.*;
import protocol.*;

public class ServerFacade {
    private final String serverUrl;
    private HashMap<Integer, Integer> gameIDMap = new HashMap<>();
    private Integer currGameId = null;
    private String authToken;
    public String username;
    public ChessGame.TeamColor playerColor;

    public ServerFacade(String serverUrl, String authToken) {
        this.serverUrl = serverUrl;
        this.authToken = authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public HashMap<Integer, Integer> getGameIDMap() {
        return gameIDMap;
    }
    public void resetCurrGameId() {
        currGameId = null;
    }

    public Integer getCurrGameId() {
        return currGameId;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getAuthToken() {
        return authToken;
    }

    public RegisterResponse register(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, registerRequest, null, RegisterResponse.class);
    }

    public LoginResponse login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, loginRequest, null, LoginResponse.class);
    }

    public CreateGameResponse create(CreateGameRequest createRequest) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, createRequest, createRequest.authToken(), CreateGameResponse.class);
    }

    public GetGamesResponse list(GetGamesRequest getGamesRequest) {
        try {
            var path = "/game";
            var games = this.makeRequest("GET", path, null, getGamesRequest.authToken(), GetGamesResponse.class);

            ArrayList<Game> gamesList = (ArrayList<Game>) games.games();
            gameIDMap.clear();
            for (int i = 0; i < gamesList.size(); i++) {
                Game game = gamesList.get(i);
                gameIDMap.put(i + 1, game.gameID());
            }
            return games;
        }
        catch (Exception e) {
            return null;
        }
    }

    public JoinGameResponse join(JoinGameRequest joinGameRequest) throws ResponseException {
        var path = "/game";
        if (!gameIDMap.containsKey(joinGameRequest.gameID())) {
            throw new ResponseException(400, "Invalid game ID");
        }
        var joinGameRequestFix = new JoinGameRequest(joinGameRequest.authToken(),
                joinGameRequest.playerColor(), gameIDMap.get(joinGameRequest.gameID()));
        JoinGameResponse joinGameResponse = this.makeRequest("PUT", path, joinGameRequestFix, joinGameRequestFix.authToken(), JoinGameResponse.class);
        currGameId = joinGameRequestFix.gameID();
        return joinGameResponse;
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) throws ResponseException {
        var path = "/session";
        return this.makeRequest("DELETE", path, null, logoutRequest.authToken(), LogoutResponse.class);
    }


    private <T> T makeRequest(String method, String path, Object requestBody, String requestHeader, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (requestHeader != null) {
                http.setRequestProperty("Authorization", requestHeader);
            }

            writeBody(requestBody, http);
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
        Message response = null;
        if (!isSuccessful(status)) {
            if (http.getContentLength() < 0) {
                try (InputStream respBody = http.getErrorStream()) {
                    InputStreamReader reader = new InputStreamReader(respBody);
                    response = new Gson().fromJson(reader, Message.class);
                    throw new ResponseException(status, response.message());
                }
            }
            throw new ResponseException(status, "Unknown error");
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

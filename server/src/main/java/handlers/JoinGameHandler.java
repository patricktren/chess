package handlers;

import com.google.gson.Gson;
import dataaccess.Database;
import exception.ResponseException;
import protocol.JoinGameRequest;
import protocol.JoinGameResponse;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    protected Database database;

    public JoinGameHandler(Database database) {
        this.database = database;
    }

    public Object joinGame(Request req, Response res) {
        Message message = null;
        try {
            String authToken = req.headers("Authorization");
            JoinGameRequest joinGameRequest = new Gson().fromJson(req.body(), JoinGameRequest.class);
            joinGameRequest = new JoinGameRequest(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());
            JoinGameResponse joinGameResponse = new JoinGameService(database).joinGame(joinGameRequest);

            return new Gson().toJson(joinGameResponse);
        }
        catch (ResponseException er) {
            res.status(er.getStatusCode());
            message = new Message(er.getMessage());
        }
        return new Gson().toJson(message);
    }
}

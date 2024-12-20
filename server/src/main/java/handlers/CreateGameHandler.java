package handlers;

import com.google.gson.Gson;
import dataaccess.Database;
import exception.Message;
import exception.ResponseException;
import protocol.CreateGameRequest;
import protocol.CreateGameResponse;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    protected Database database;

    public CreateGameHandler(Database database) {
        this.database = database;
    }

    public Object createGame(Request req, Response res) {
        Message message = null;
        try {
            String authToken = req.headers("Authorization");
            CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);
            createGameRequest = new CreateGameRequest(authToken, createGameRequest.gameName());
            CreateGameResponse createGameResponse = new CreateGameService(database).createGame(createGameRequest);

            return new Gson().toJson(createGameResponse);
        }
        catch (ResponseException er) {
            res.status(er.getStatusCode());
            message = new Message(er.getMessage());
        }
        return new Gson().toJson(message);
    }
}

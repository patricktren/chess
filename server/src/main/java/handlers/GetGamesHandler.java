package handlers;

import com.google.gson.Gson;
import dataaccess.Database;
import exception.Message;
import exception.ResponseException;
import protocol.GetGamesRequest;
import protocol.GetGamesResponse;
import service.GetGamesService;
import spark.Request;
import spark.Response;

public class GetGamesHandler {
    protected Database database;

    public GetGamesHandler(Database database) {
        this.database = database;
    }

    public Object getGames(Request req, Response res) {
        Message message = null;
        try {
            GetGamesRequest getGamesRequest = new GetGamesRequest(req.headers("Authorization"));
            GetGamesResponse joinGameResponse = new GetGamesService(database).getGames(getGamesRequest);

            return new Gson().toJson(joinGameResponse);
        }
        catch (ResponseException er) {
            res.status(er.getStatusCode());
            message = new Message(er.getMessage());
        }
        return new Gson().toJson(message);
    }
}

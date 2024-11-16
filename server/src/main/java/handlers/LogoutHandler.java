package handlers;

import com.google.gson.Gson;
import dataaccess.Database;
import exception.Message;
import exception.ResponseException;
import protocol.LogoutRequest;
import protocol.LogoutResponse;
import service.LogoutService;
import spark.Request;
import spark.Response;

public class LogoutHandler {
    protected Database database;

    public LogoutHandler(Database database) {
        this.database = database;
    }

    public Object logoutUser(Request req, Response res) {
        Message message = null;
        try {
            LogoutRequest logoutRequest = new LogoutRequest(req.headers("Authorization"));
            LogoutResponse logoutResponse = new LogoutService(database).logoutUser(logoutRequest);
            return new Gson().toJson(logoutResponse);
        }
        catch (ResponseException er) {
            res.status(er.getStatusCode());
            message = new Message(er.getMessage());
        }
        return new Gson().toJson(message);
    }
}

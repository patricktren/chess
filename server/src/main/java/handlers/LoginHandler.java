package handlers;

import com.google.gson.Gson;
import dataaccess.Database;
import exception.ResponseException;
import protocol.LoginResponse;
import service.LoginService;

import protocol.LoginRequest;
import spark.Request;
import spark.Response;

public class LoginHandler {
    protected Database database;

    public LoginHandler(Database database) {
        this.database = database;
    }

    public Object loginUser(Request req, Response res) {
        Message message;
        try {
            LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResponse loginResponse = new LoginService(database).loginUser(loginRequest);
            return new Gson().toJson(loginResponse);
        } catch (ResponseException er) {
            res.status(er.getStatusCode());
            message = new Message(er.getMessage());
        }
        return new Gson().toJson(message);
    }
}

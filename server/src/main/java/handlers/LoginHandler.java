package handlers;

import com.google.gson.Gson;
import dataaccess.Database;
import exception.ResponseException;
import model.User;
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
        try {
            LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
            LoginResponse loginResponse = new LoginService(database).loginUser(loginRequest);
            return new Gson().toJson(loginResponse);
        } catch (ResponseException er) {
            res.status(er.getStatusCode());
        }
        return "";
    }
}

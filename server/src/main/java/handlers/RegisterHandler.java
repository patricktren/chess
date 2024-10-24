package handlers;

import com.google.gson.Gson;
import dataaccess.MemoryDatabase;
import exception.ResponseException;
import model.User;
import protocol.RegisterRequest;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
    protected MemoryDatabase memoryDatabase;

    public RegisterHandler(MemoryDatabase memoryDatabase) {
        this.memoryDatabase = memoryDatabase;
    }
    public Object registerUser(Request req, Response res) throws ResponseException {
        try {
            var newUser = new Gson().fromJson(req.body(), User.class);
            var registerRequest = new RegisterRequest(newUser.getUsername(), newUser.password(), newUser.email());
            var registerResult = new RegisterService(memoryDatabase).registerUser(registerRequest);
            return new Gson().toJson(registerResult);
        } catch (ResponseException er) {
            res.status(er.getStatusCode());
        }
        return "";
    }
}

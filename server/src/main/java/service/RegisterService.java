package service;

import dataaccess.*;
import exception.ResponseException;
import model.User;
import protocol.RegisterRequest;
import protocol.RegisterResponse;

public class RegisterService extends Service {
    private final UserDAO userDAO;
    private final AuthTokenDAO authTokenDAO;

    public RegisterService(Database database) {
        super(database);
        this.userDAO = database.getUserDAO();
        this.authTokenDAO = database.getAuthTokenDAO();
    }

    // attempt to register a new user
    public RegisterResponse registerUser(RegisterRequest registerRequest) throws ResponseException {
        try {
            // check if bad request
            // username is empty (note: isBlank() checks if is whitespace, "", or null)
            if (registerRequest.username() == null || registerRequest.username().isEmpty()) {
                throw new ResponseException(400, "Error: must not be empty");
            }
            // password is empty
            if (registerRequest.password() == null || registerRequest.password().isEmpty()) {
                throw new ResponseException(400, "Error: must not be empty");
            }
            // email is empty or doesn't contain expected chars
            if (registerRequest.email() == null || registerRequest.email().isEmpty()
                    || !registerRequest.email().contains("@") || !registerRequest.email().contains(".")) {
                throw new ResponseException(400, "Error");
            }

            // check if the user already exists
            if (userExists(registerRequest.username())) {
                throw new ResponseException(403, "Error: username already taken");
            } else {
                // create the user object
                var newUser = new User(registerRequest.username(), registerRequest.password(), registerRequest.email());
                userDAO.createUser(newUser);
                // create auth & log in the new user
                return new RegisterResponse(newUser.getUsername(), createAuthToken(newUser.username()).getToken());
            }
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Error: Couldn't connect to database");
        }
    }
}

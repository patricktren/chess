package service;

import dataaccess.UserDAO;
import exception.ResponseException;
import model.User;
import protocol.RegisterRequest;
import protocol.RegisterResult;

public class RegisterService extends Service {
    private final UserDAO userDAO;

    public RegisterService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // attempt to register a new user
    RegisterResult registerUser(RegisterRequest registerRequest) throws ResponseException {
        // check if bad request
        // username is empty (note: isBlank() checks if is whitespace, "", or null)
        if (registerRequest.username().isBlank()) {
            throw new ResponseException(400, "Invalid username: must not be empty");
        }
        // password is empty
        if (registerRequest.password().isBlank()) {
            throw new ResponseException(400, "Invalid password: must not be empty");
        }
        // email is empty or doesn't contain expected chars
        if (registerRequest.email().isBlank()
            || !registerRequest.email().contains("@") || !registerRequest.email().contains(".")) {
            throw new ResponseException(400, "Invalid email");
        }

        // check if the user already exists
        if (userExists(registerRequest.username())) {
            throw new ResponseException(403, "Invalid username: username already taken");
        }
        else {
            // create the user object
            var newUser = new User(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDAO.createUser(newUser);
            // TO-DO: create auth & log in the new user

            return new RegisterResult(newUser.username(), null, "User successfully created");
        }
    }
}

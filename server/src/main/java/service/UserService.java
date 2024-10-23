package service;

import dataaccess.UserDAO;
import exception.ResponseException;
import model.User;
import org.eclipse.jetty.client.HttpResponseException;
import protocol.RegisterRequest;
import protocol.RegisterResult;

import java.util.Objects;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
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
        if (userDAO.getUser(registerRequest.username()) != null) {
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

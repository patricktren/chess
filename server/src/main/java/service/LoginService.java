package service;

import dataaccess.UserDAO;
import exception.ResponseException;
import protocol.LoginRequest;
import protocol.LoginResult;

import java.util.Objects;

public class LoginService extends Service {
    private final UserDAO userDAO;

    public LoginService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // attempt to log in a user
    LoginResult login(LoginRequest loginRequest) throws ResponseException {
        var user = userDAO.getUser(loginRequest.username());
        // user doesn't exist?
        if (user == null) {
            throw new ResponseException(500, "User doesn't exist");
        }
        // invalid password?
        else if (!Objects.equals(loginRequest.password(), user.getPassword())) {
            throw new ResponseException(401, "Invalid password");
        }
        // login
        else {

        }

        return null;
    }

}

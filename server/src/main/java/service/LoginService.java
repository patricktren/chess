package service;

import dataaccess.AuthTokenDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthToken;
import protocol.LoginRequest;
import protocol.LoginResult;

import java.util.Objects;
import java.util.UUID;

public class LoginService extends Service {
    private final UserDAO userDAO;
    private final AuthTokenDAO authTokenDAO;

    public LoginService(UserDAO userDAO, AuthTokenDAO authTokenDAO) {
        this.userDAO = userDAO;
        this.authTokenDAO = authTokenDAO;
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
            return new LoginResult(user.getUsername(), createAuthToken(user.getUsername()).getToken());
        }
    }

}

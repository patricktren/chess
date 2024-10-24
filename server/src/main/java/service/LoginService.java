package service;

import dataaccess.*;
import exception.ResponseException;
import protocol.LoginRequest;
import protocol.LoginResult;

import java.util.Objects;

public class LoginService extends Service {
    private final UserDAO userDAO;

    public LoginService(Database database) {
        super(database);
        this.userDAO = database.getUserDAO();
    }

    // attempt to log in a user
    LoginResult login(LoginRequest loginRequest) throws ResponseException {
        try {
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
        catch (DataAccessException er) {
            throw new ResponseException(500, "Couldn't connect to database");
        }
    }
}

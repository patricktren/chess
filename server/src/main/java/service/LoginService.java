package service;

import dataaccess.*;
import exception.ResponseException;
import org.mindrot.jbcrypt.BCrypt;
import protocol.LoginRequest;
import protocol.LoginResponse;

public class LoginService extends Service {
    private final UserDAO userDAO;

    public LoginService(Database database) {
        super(database);
        this.userDAO = database.getUserDAO();
    }

    // attempt to log in a user
    public LoginResponse loginUser(LoginRequest loginRequest) throws ResponseException {
        try {
            var user = userDAO.getUser(loginRequest.username());
            // user doesn't exist?
            if (user == null) {
                throw new ResponseException(401, "Error: Invalid username or password");
            }
            // invalid password?
            else if (!BCrypt.checkpw(loginRequest.password(), user.getPassword())) {
                throw new ResponseException(401, "Error: Invalid username or password");
            }
            // login
            else {
                return new LoginResponse(user.getUsername(), createAuthToken(user.getUsername()).getToken());
            }
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Couldn't connect to database");
        }
    }
}

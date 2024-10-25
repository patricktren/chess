package service;

import dataaccess.AuthTokenDAO;
import dataaccess.DataAccessException;
import dataaccess.Database;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthToken;
import protocol.LogoutRequest;
import protocol.LogoutResult;

public class LogoutService extends Service {
    private final AuthTokenDAO authTokenDAO;

    public LogoutService(Database database) {
        super(database);
        this.authTokenDAO = database.getAuthTokenDAO();
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws ResponseException {
        try {
            // verify auth token
            boolean authTokenExists = verifyAuthToken(logoutRequest.authToken());
            // if authToken is empty or doesn't exist
            if (logoutRequest.authToken().isBlank() || !authTokenExists) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            // delete authToken
            authTokenDAO.deleteAuthToken(logoutRequest.authToken());
            return new LogoutResult();
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Couldn't connect to database");
        }
    }
}

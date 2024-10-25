package service;

import dataaccess.AuthTokenDAO;
import dataaccess.DataAccessException;
import dataaccess.Database;
import exception.ResponseException;
import protocol.LogoutRequest;
import protocol.LogoutResponse;

public class LogoutService extends Service {
    private final AuthTokenDAO authTokenDAO;

    public LogoutService(Database database) {
        super(database);
        this.authTokenDAO = database.getAuthTokenDAO();
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) throws ResponseException {
        try {
            // verify auth authToken
            boolean authTokenExists = verifyAuthToken(logoutRequest.authToken());
            // if authToken is empty or doesn't exist
            if (logoutRequest.authToken().isBlank() || !authTokenExists) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            // delete authToken
            authTokenDAO.deleteAuthToken(logoutRequest.authToken());
            return new LogoutResponse();
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Couldn't connect to database");
        }
    }
}

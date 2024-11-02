package service;

import dataaccess.*;
import exception.ResponseException;
import model.AuthToken;

import java.util.UUID;

public abstract class Service {
    protected UserDAO userDAO;
    protected AuthTokenDAO authTokenDAO;
    protected GameDAO gameDAO;

    Service(Database database) {
        this.userDAO = database.getUserDAO();
        this.authTokenDAO = database.getAuthTokenDAO();
        this.gameDAO = database.getGameDAO();
    }

    protected boolean userExists(String username) throws ResponseException {
        try {
            return (userDAO.getUser(username) != null);
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Couldn't connect to database");
        }
    }
    public AuthToken createAuthToken(String username) throws DataAccessException {
        while (true) {
            var newAuthToken = new AuthToken(UUID.randomUUID().toString(), username);
            if (authTokenDAO.getAuthToken(newAuthToken.getToken()) != null) {
                continue;
            } else {
                authTokenDAO.createAuthToken(newAuthToken);
                return new AuthToken(newAuthToken.getToken(), username);
            }
        }
    }

    protected boolean verifyAuthToken(String authToken) throws DataAccessException {
        // check if authToken exists
        return (authTokenDAO.getAuthToken(authToken) != null);
    }
}

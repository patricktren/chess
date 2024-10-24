package service;

import dataaccess.*;
import exception.ResponseException;
import model.AuthToken;

import java.util.UUID;

public abstract class Service {
    protected UserDAO userDAO;
    protected AuthTokenDAO authTokenDAO;

    Service(Database database) {
        this.userDAO = database.getUserDAO();
        this.authTokenDAO = database.getAuthTokenDAO();
    }

    protected boolean userExists(String username) throws ResponseException {
        try {
            return (userDAO.getUser(username) != null);
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Couldn't connect to database");
        }
    }
    protected AuthToken createAuthToken(String username) {
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

    protected AuthToken verifyAuth(AuthToken authToken) {
        return null;
    }
}

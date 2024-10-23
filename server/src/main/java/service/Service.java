package service;

import dataaccess.AuthTokenDAO;
import dataaccess.UserDAO;
import model.AuthToken;
import model.User;
import protocol.RegisterResult;

import java.util.UUID;

public abstract class Service {
    UserDAO userDAO;
    AuthTokenDAO authTokenDAO;

    protected boolean userExists(String username) {
        return (userDAO.getUser(username) != null);
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

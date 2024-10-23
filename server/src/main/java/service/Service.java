package service;

import dataaccess.UserDAO;
import model.AuthToken;
import model.User;

public abstract class Service {
    UserDAO userDAO;
    protected boolean userExists(String username) {
        return (userDAO.getUser(username) != null);
    }

    AuthToken verifyAuth(AuthToken authToken) {
        return null;
    }
}

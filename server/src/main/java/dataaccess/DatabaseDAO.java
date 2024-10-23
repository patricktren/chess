package dataaccess;

public abstract class DatabaseDAO {
    private UserDAO userDAO;
    private AuthTokenDAO authTokenDAO;

    public UserDAO getUserDAO() {
        return userDAO;
    }
    public AuthTokenDAO getAuthTokenDAO() {
        return authTokenDAO;
    }
}

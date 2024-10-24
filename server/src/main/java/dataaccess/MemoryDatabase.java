package dataaccess;

public class MemoryDatabase implements Database{
    private MemoryUserDAO userDAO;
    private AuthTokenDAO authTokenDAO;

    public MemoryDatabase() {
        this.userDAO = new MemoryUserDAO();
        this.authTokenDAO = new MemoryAuthTokenDAO();
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }
    public AuthTokenDAO getAuthTokenDAO() {
        return authTokenDAO;
    }
}

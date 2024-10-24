package dataaccess;

public class MemoryDatabase implements Database{
    private final MemoryUserDAO userDAO;
    private final AuthTokenDAO authTokenDAO;

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

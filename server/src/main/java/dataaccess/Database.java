package dataaccess;

public interface Database {
    public UserDAO getUserDAO();
    public AuthTokenDAO getAuthTokenDAO();
    public GameDAO getGameDAO();

    public void clearDatabase() throws DataAccessException;
}

package dataaccess;

public interface Database {
    public UserDAO getUserDAO();
    public AuthTokenDAO getAuthTokenDAO();

    public void clearDatabase() throws DataAccessException;
}

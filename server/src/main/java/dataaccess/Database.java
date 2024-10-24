package dataaccess;

public interface Database {
    public UserDAO getUserDAO();
    public AuthTokenDAO getAuthTokenDAO();
}

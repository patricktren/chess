package dataaccess;

import java.sql.Connection;

public class SQLDatabase implements Database {
    SQLUserDAO sqlUserDAO;


    public SQLDatabase() {
        this.sqlUserDAO = new SQLUserDAO();
    }
    @Override
    public UserDAO getUserDAO() {
        return sqlUserDAO;
    }

    @Override
    public AuthTokenDAO getAuthTokenDAO() {
        return null;
    }

    @Override
    public GameDAO getGameDAO() {
        return null;
    }

    @Override
    public void clearDatabase() throws DataAccessException {

    }
}

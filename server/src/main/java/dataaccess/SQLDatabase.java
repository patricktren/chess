package dataaccess;

import java.sql.Connection;

public class SQLDatabase implements Database {
    SQLUserDAO sqlUserDAO;
    SQLAuthTokenDAO sqlAuthTokenDAO;
    SQLGameDAO sqlGameDAO;


    public SQLDatabase() {
        this.sqlUserDAO = new SQLUserDAO();
        this.sqlAuthTokenDAO = new SQLAuthTokenDAO();
        this.sqlGameDAO = new SQLGameDAO();
    }
    @Override
    public UserDAO getUserDAO() {
        return sqlUserDAO;
    }

    @Override
    public AuthTokenDAO getAuthTokenDAO() {
        return sqlAuthTokenDAO;
    }

    @Override
    public GameDAO getGameDAO() {
        return sqlGameDAO;
    }

    @Override
    public void clearDatabase() throws DataAccessException {
        sqlUserDAO.clearUsers();
        sqlAuthTokenDAO.clearAuthTokens();
        sqlGameDAO.clearGames();
    }
}

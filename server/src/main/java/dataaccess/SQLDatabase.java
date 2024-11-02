package dataaccess;

import exception.ResponseException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLDatabase implements Database {
    SQLUserDAO sqlUserDAO;
    SQLAuthTokenDAO sqlAuthTokenDAO;
    SQLGameDAO sqlGameDAO;


    public SQLDatabase() throws DataAccessException {
        configureDatabase();
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

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection connection = DatabaseManager.getConnection()) {
            for (var statement : createTableStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException er) {
            throw new DataAccessException("Error: issue in configureDatabase()");
        }

    }
    private final String[] createTableStatements = {
            """
             CREATE TABLE IF NOT EXISTS `users` (
              `username` varchar(255) NOT NULL,
              `password` varchar(255) DEFAULT NULL,
              `email` varchar(255) DEFAULT NULL,
              PRIMARY KEY (`username`)
              )
            """,

            """
            CREATE TABLE IF NOT EXISTS `games` (
              `game_id` int NOT NULL AUTO_INCREMENT,
              `game_name` varchar(255) DEFAULT NULL,
              `white_username` varchar(255) DEFAULT NULL,
              `black_username` varchar(255) DEFAULT NULL,
              'game_state' blob DEFAULT NULL,
              PRIMARY KEY (`game_id`)
              )
            """,

            """
            CREATE TABLE IF NOT EXISTS `auth_tokens` (
              `auth_token` varchar(255) DEFAULT NULL,
              `username` varchar(255) DEFAULT NULL
            )
            """
    };
}

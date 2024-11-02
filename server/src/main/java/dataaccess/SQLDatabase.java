package dataaccess;

import exception.ResponseException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLDatabase implements Database {
    final private SQLUserDAO sqlUserDAO;
    final private SQLAuthTokenDAO sqlAuthTokenDAO;
    final private SQLGameDAO sqlGameDAO;


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
            throw new DataAccessException("Error: issue in configureDatabase(): " + er.getMessage());
        }

    }
    private final String[] createTableStatements = {
            """
             CREATE TABLE IF NOT EXISTS users (
              `username` varchar(255) NOT NULL,
              `password` varchar(255) DEFAULT NULL,
              `email` varchar(255) DEFAULT NULL,
              PRIMARY KEY (`username`)
              );
            """,

            """
            CREATE TABLE IF NOT EXISTS games (
              `game_id` int NOT NULL AUTO_INCREMENT,
              `game_name` varchar(255) NOT NULL,
              `white_username` varchar(255) DEFAULT NULL,
              `black_username` varchar(255) DEFAULT NULL,
              `game_state` blob NOT NULL, -- Use backticks for column names
              PRIMARY KEY (`game_id`)
            );
            
            """,

            """
            CREATE TABLE IF NOT EXISTS auth_tokens (
              `auth_token` varchar(255) NOT NULL,
              `username` varchar(255) DEFAULT NULL,
            PRIMARY KEY (`auth_token`)
            );
            """
    };

    public boolean mayContainSQLInjection(String input) {
        String[] sqlKeywords = {"UNION", "SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "EXEC", "OR 1=1"};
        for (String keyword : sqlKeywords) {
            if (input.toUpperCase().contains(keyword)) {
                return true; // Potential SQL injection detected
            }
        }
        return false; // Input is likely safe
    }

}

package dataaccess;

import model.Game;
import model.User;

import java.sql.*;
import java.util.ArrayList;

import static dataaccess.DatabaseManager.getConnection;

public class SQLGameDAO implements GameDAO{
    @Override
    public void createGame(Game newGame) throws DataAccessException {
        String sql_statement = "INSERT INTO games (game_name, white_username, black_username) VALUES (?, ?, ?)";
        try (Connection connection = getConnection()) {
            // make the preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement,
                    Statement.RETURN_GENERATED_KEYS);

            // set the values to insert
            preparedStatement.setString(1, newGame.gameName());
            preparedStatement.setString(2, newGame.whiteUsername());
            preparedStatement.setString(3, newGame.blackUsername());
            // execute
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public Game getGame(Integer gameID) throws DataAccessException {
        String sql_statement = "SELECT game_id, game_name, white_username, black_username FROM games WHERE game_id = " + gameID;
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement);
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                Integer gameIDResult = resultSet.getInt("game_id");
                String gameNameResult = resultSet.getString("game_name");
                String whiteUsernameResult = resultSet.getString("white_username");
                String blackUsernameResult = resultSet.getString("black_username");

                return new Game(gameIDResult, gameNameResult, whiteUsernameResult, blackUsernameResult);
            }
            else {
                throw new DataAccessException("Error: game does not exist");
            }
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public ArrayList<Game> getGames(String token) throws DataAccessException {
        String sql_statement = "SELECT game_id, game_name, white_username, black_username FROM games";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement);
            ResultSet resultSet = preparedStatement.getResultSet();

            ArrayList<Game> games = new ArrayList<>();
            while (resultSet.next()) {
                Integer gameIDResult = resultSet.getInt("game_id");
                String gameNameResult = resultSet.getString("game_name");
                String whiteUsernameResult = resultSet.getString("white_username");
                String blackUsernameResult = resultSet.getString("black_username");

                games.add(new Game(gameIDResult, gameNameResult, whiteUsernameResult, blackUsernameResult));
            }
            return games;
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public void updateGame(Game game) throws DataAccessException {

    }

    @Override
    public void clearGames() throws DataAccessException {
        String sql_statement = "DELETE FROM games";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql_statement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public Integer getNextGameID() {
        return null;
    }
}

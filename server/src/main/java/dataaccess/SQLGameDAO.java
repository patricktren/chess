package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.Game;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static dataaccess.DatabaseManager.getConnection;

public class SQLGameDAO implements GameDAO{
    @Override
    public Integer createGame(Game newGame) throws DataAccessException {
        String sqlStatement = "INSERT INTO games (game_name, white_username, black_username, game_state) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection()) {
            // make the preparedStatement
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement,
                    Statement.RETURN_GENERATED_KEYS);

            // set the values to insert
            preparedStatement.setString(1, newGame.gameName());
            preparedStatement.setString(2, newGame.whiteUsername());
            preparedStatement.setString(3, newGame.blackUsername());
            preparedStatement.setString(4, new Gson().toJson(new ChessGame(), ChessGame.class));
            // execute
            preparedStatement.executeUpdate();

            // get the game_id
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys != null && generatedKeys.next()) {
                    // Return the generated game_id
                    return generatedKeys.getInt(1);
                }
                else {
                    return null;
                }
            }
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public Game getGame(Integer gameID) throws DataAccessException {
        String sqlStatement = "SELECT game_id, game_name, white_username, black_username, game_state FROM games WHERE game_id = '" + gameID + "';";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet != null && resultSet.next()) {
                Integer gameIDResult = resultSet.getInt("game_id");
                String gameNameResult = resultSet.getString("game_name");
                String whiteUsernameResult = resultSet.getString("white_username");
                String blackUsernameResult = resultSet.getString("black_username");
                String serializedGameStateResult = resultSet.getString("game_state");

                ChessGame gameStateResult = new Gson().fromJson(serializedGameStateResult, ChessGame.class);

                // set null values to empty strings
                if (gameNameResult != null && gameNameResult.equals("null")) {gameNameResult = null;}
                if (whiteUsernameResult != null && whiteUsernameResult.equals("null")) {whiteUsernameResult = null;}
                if (blackUsernameResult != null && blackUsernameResult.equals("null")) {blackUsernameResult = null;}

                return new Game(gameIDResult, gameNameResult, whiteUsernameResult, blackUsernameResult, gameStateResult);
            }
            else {
                return null;
            }
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public ArrayList<Game> getGames(String token) throws DataAccessException {
        String sqlStatement = "SELECT game_id, game_name, white_username, black_username, game_state FROM games;";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            ArrayList<Game> games = new ArrayList<>();
            while (resultSet.next()) {
                Integer gameIDResult = resultSet.getInt("game_id");
                String gameNameResult = resultSet.getString("game_name");
                String whiteUsernameResult = resultSet.getString("white_username");
                String blackUsernameResult = resultSet.getString("black_username");

                String serializedGameStateResult = resultSet.getString("game_state");
                ChessGame gameStateResult = new Gson().fromJson(serializedGameStateResult, ChessGame.class);

                // set null values to empty strings
                if (gameNameResult != null && gameNameResult.equals("null")) {gameNameResult = null;}
                if (whiteUsernameResult != null && whiteUsernameResult.equals("null")) {whiteUsernameResult = null;}
                if (blackUsernameResult != null && blackUsernameResult.equals("null")) {blackUsernameResult = null;}

                games.add(new Game(gameIDResult, gameNameResult, whiteUsernameResult, blackUsernameResult, gameStateResult));
            }
            return games;
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public void updateGame(Game game) throws DataAccessException {
        var gameName = game.gameName().replace("'", "''");
        String sqlStatement = String.format("UPDATE games SET game_id = %d, game_name = '%s', white_username = '%s', black_username = '%s', " +
                        "game_state = '%s' WHERE game_id = %d",
                game.gameID(), gameName, game.whiteUsername(), game.blackUsername(), new Gson().toJson(game.gameState()), game.gameID());
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException er) {
            throw new DataAccessException("Error " + er.getMessage());
        }
    }

    @Override
    public void clearGames() throws DataAccessException {
        String sqlStatement = "DELETE FROM games";
        try (Connection connection = getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
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

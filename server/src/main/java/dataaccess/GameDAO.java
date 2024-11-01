package dataaccess;

import chess.ChessGame;
import model.AuthToken;
import model.Game;

import java.util.ArrayList;
import java.util.List;

public interface GameDAO {
    public void createGame(Game newGame) throws DataAccessException;
    public Game getGame(Integer gameID) throws DataAccessException;
    public ArrayList<Game> getGames(String token) throws DataAccessException;
    public void updateGame(Game game) throws DataAccessException;
    public void clearGames() throws DataAccessException;
    public Integer getNextGameID();
}

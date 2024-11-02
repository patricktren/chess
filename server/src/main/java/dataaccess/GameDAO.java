package dataaccess;

import chess.ChessGame;
import model.AuthToken;
import model.Game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface GameDAO {
    public Integer createGame(Game newGame) throws DataAccessException;
    public Game getGame(Integer gameID) throws DataAccessException;
    public ArrayList<Game> getGames(String token) throws DataAccessException;
    public void updateGame(Game game) throws DataAccessException;
    public void clearGames() throws DataAccessException;
    public Integer getNextGameID();
}

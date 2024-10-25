package dataaccess;

import chess.ChessGame;
import model.AuthToken;

import java.util.List;

public interface GameDAO {
    public void createGame(ChessGame newGame) throws DataAccessException;
    public List<ChessGame> getGames(String token) throws DataAccessException;
    public void updateGame(ChessGame game) throws DataAccessException;
    public void deleteGame(int gameID) throws DataAccessException;
    public void clearGames() throws DataAccessException;
    public Integer getNextGameID();
}

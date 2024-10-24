package dataaccess;

import chess.ChessGame;
import model.AuthToken;

import java.util.List;

public interface GameDAO {
    public void createGame(ChessGame newGame);
    public List<ChessGame> getGames(String token);
    public void updateGame(ChessGame game);
    public void deleteGame(int gameID);
    public void clearGames();
}

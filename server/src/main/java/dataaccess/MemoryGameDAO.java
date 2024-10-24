package dataaccess;

import chess.ChessGame;
import model.AuthToken;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    Map<Integer, ChessGame> gameMap = new HashMap<>();

    MemoryGameDAO() {}

    @Override
    public void createGame(ChessGame newGame) {
        gameMap.put(newGame.getGameID(), newGame);
    }

    @Override
    public List<ChessGame> getGames(String token) {
        return new ArrayList<>(gameMap.values());
    }

    @Override
    public void updateGame(ChessGame game) {

    }

    @Override
    public void deleteGame(int gameID) {
        gameMap.remove(gameID);
    }

    @Override
    public void clearGames() {
        gameMap.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryGameDAO that = (MemoryGameDAO) o;
        return Objects.equals(gameMap, that.gameMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(gameMap);
    }
}

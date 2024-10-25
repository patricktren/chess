package dataaccess;

import chess.ChessGame;
import model.AuthToken;
import model.Game;

import java.util.*;

public class MemoryGameDAO implements GameDAO {
    Map<Integer, Game> gameMap = new HashMap<>();
    Integer nextGameID = 1000;

    MemoryGameDAO() {}

    @Override
    public void createGame(Game newGame) {
        gameMap.put(newGame.gameID(), newGame);
    }

    @Override
    public Game getGame(Integer gameID) {
        return gameMap.get(gameID);
    }

    @Override
    public ArrayList<Game> getGames(String token) {
        return new ArrayList<>(gameMap.values());
    }

    @Override
    public void updateGame(Game game) {
        gameMap.put(game.gameID(), game);
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
    public Integer getNextGameID() {
        nextGameID += 10;
        return nextGameID;
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

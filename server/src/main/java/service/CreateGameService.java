package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.Database;
import exception.ResponseException;
import protocol.CreateGameRequest;
import protocol.CreateGameResponse;

public class CreateGameService extends Service{
    Database database;
    public CreateGameService(Database database) {
        super(database);
        this.database = database;
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) throws ResponseException {
        try {
            // verify authToken
            boolean authTokenValid = verifyAuthToken(createGameRequest.authToken());
            if (!authTokenValid) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            // verify game name is not empty
            if (createGameRequest.gameName() == null || createGameRequest.gameName().isBlank()) {
                throw new ResponseException(400, "Error: game name cannot be empty");
            }
            ChessGame newGame = new ChessGame();
            newGame.setGameInfo(gameDAO.getNextGameID(), createGameRequest.gameName(), null, null);
            gameDAO.createGame(newGame);
            return new CreateGameResponse(newGame.getGameID());
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Error: failed to connect to database");
        }
    }
}

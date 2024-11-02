package service;

import chess.ChessGame;
import dataaccess.AuthTokenDAO;
import dataaccess.DataAccessException;
import dataaccess.Database;
import dataaccess.GameDAO;
import exception.ResponseException;
import model.Game;
import protocol.JoinGameRequest;
import protocol.JoinGameResponse;

import java.util.Objects;

public class JoinGameService extends Service{
    private final GameDAO gameDAO;
    private final AuthTokenDAO authTokenDAO;

    public JoinGameService(Database database) {
        super(database);
        this.gameDAO = database.getGameDAO();
        this.authTokenDAO = database.getAuthTokenDAO();
    }

    public JoinGameResponse joinGame(JoinGameRequest joinGameRequest) throws ResponseException {
        try {
            // verify auth authToken
            boolean authTokenExists = verifyAuthToken(joinGameRequest.authToken());
            // if authToken is empty or doesn't exist
            if (!authTokenExists) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            // verify game exists
            Game game = gameDAO.getGame(joinGameRequest.gameID());
            if (game == null) {
                throw new ResponseException(400, "Error: game does not exist");
            }
            if (joinGameRequest.playerColor() != ChessGame.TeamColor.WHITE && joinGameRequest.playerColor() != ChessGame.TeamColor.BLACK) {
                throw new ResponseException(400, "Error: invalid player color");
            }
            // verify game has the spot open the player is trying to take
            if ((Objects.equals(joinGameRequest.playerColor(), ChessGame.TeamColor.WHITE) && game.whiteUsername() != null && !(game.whiteUsername().equals("null")))
                    || (Objects.equals(joinGameRequest.playerColor(), ChessGame.TeamColor.BLACK) && game.blackUsername() != null && !(game.blackUsername().equals("null")))) {
                throw new ResponseException(403, "Error: already taken");
            }

            // join game
            String username = authTokenDAO.getAuthToken(joinGameRequest.authToken()).getUsername();
            if (joinGameRequest.playerColor() == ChessGame.TeamColor.WHITE) {
                Game updatedGame = new Game(game.gameID(), game.gameName(), username, game.blackUsername(), game.gameState());
                gameDAO.updateGame(updatedGame);
            }
            else {
                Game updatedGame = new Game(game.gameID(), game.gameName(), game.whiteUsername(), username, game.gameState());
                gameDAO.updateGame(updatedGame);
            }
            return new JoinGameResponse();
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, er.getMessage());
        }
    }
}

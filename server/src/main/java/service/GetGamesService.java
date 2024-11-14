package service;

import dataaccess.AuthTokenDAO;
import dataaccess.DataAccessException;
import dataaccess.Database;
import dataaccess.GameDAO;
import exception.ResponseException;
import protocol.GetGamesRequest;
import protocol.GetGamesResponse;

public class GetGamesService extends Service{
    private final GameDAO gameDAO;
    private final AuthTokenDAO authTokenDAO;
    private final Database database;

    public GetGamesService(Database database) {
        super(database);
        this.database = database;
        this.authTokenDAO = database.getAuthTokenDAO();
        this.gameDAO = database.getGameDAO();
    }

    public GetGamesResponse getGames(GetGamesRequest getGamesRequest) throws ResponseException {
        try {
            // verify auth authToken
            boolean authTokenExists = verifyAuthToken(getGamesRequest.authToken());
            // if authToken is empty or doesn't exist
            if (getGamesRequest.authToken().isBlank() || !authTokenExists) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            // get games
            return new GetGamesResponse(gameDAO.getGames(getGamesRequest.authToken()));
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Couldn't connect to database");
        }
    }
}

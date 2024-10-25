package service;

import dataaccess.Database;
import protocol.CreateGameRequest;
import protocol.CreateGameResponse;

public class CreateGameService extends Service{
    Database database;
    CreateGameService(Database database) {
        super(database);
        this.database = database;
    }

    public CreateGameResponse createGame(CreateGameRequest createGameRequest) {
        return null;
    }
}

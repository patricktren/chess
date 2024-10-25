package service;

import dataaccess.DataAccessException;
import dataaccess.Database;
import exception.ResponseException;
import protocol.ClearResponse;

public class ClearService {
    Database database;
    ClearService(Database database) {
        this.database = database;
    }

    ClearResponse clearDatabase() throws ResponseException {
        try {
            database.clearDatabase();
            return new ClearResponse();
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Could not connect to database");
        }
    }


}

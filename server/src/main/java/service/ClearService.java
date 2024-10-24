package service;

import dataaccess.DataAccessException;
import dataaccess.Database;
import exception.ResponseException;
import protocol.ClearResult;

public class ClearService {
    Database database;
    ClearService(Database database) {
        this.database = database;
    }

    ClearResult clearDatabase() throws ResponseException {
        try {
            database.clearDatabase();
            return new ClearResult();
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Could not connect to database");
        }
    }


}

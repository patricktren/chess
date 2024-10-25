package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.Database;
import exception.ResponseException;
import protocol.ClearResult;
import spark.Request;
import spark.Response;

public class ClearHandler {
    protected Database database;

    public ClearHandler(Database database) {
        this.database = database;
    }

    public Object clearDatabase(Request req, Response res) throws ResponseException {
        try {
            database.clearDatabase();
            return new Gson().toJson(new ClearResult());
        }
        catch (DataAccessException er) {
            throw new ResponseException(500, "Failed to connect to database");
        }
    }
}

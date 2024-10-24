package handlers;

import dataaccess.Database;
import exception.ResponseException;
import spark.Request;
import spark.Response;

public class ClearHandler {
    protected Database database;

    public ClearHandler(Database database) {
        this.database = database;
    }

//    public Object clearDatabase(Request req, Response res) {
//        try {
//
//        }
//        catch (ResponseException er) {
//
//        }
//        return "";
//
//    }
}

package server;

import dataaccess.Database;
import dataaccess.SQLDatabase;
import handlers.*;

import exception.ResponseException;
import spark.*;
import websocket.WebSocketHandler;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // initialize database
        Database database = null;
//        Database database = new MemoryDatabase();
        try {
            database = new SQLDatabase();
        }
        catch (Throwable er) {
            System.out.println("Error, unable to initialize database: " + er.getMessage());
        }

        WebSocketHandler webSocketHandler = new WebSocketHandler();
        Spark.webSocket("/ws", webSocketHandler);

        // initialize handlers
        RegisterHandler registerHandler = new RegisterHandler(database);
        LoginHandler loginHandler = new LoginHandler(database);
        LogoutHandler logoutHandler = new LogoutHandler(database);
        CreateGameHandler createGameHandler = new CreateGameHandler(database);
        JoinGameHandler joinGameHandler = new JoinGameHandler(database);
        GetGamesHandler getGamesHandler = new GetGamesHandler(database);

        ClearHandler clearHandler = new ClearHandler(database);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registerHandler::registerUser);
        Spark.post("/session", loginHandler::loginUser);
        Spark.delete("/session", logoutHandler::logoutUser);
        Spark.post("/game", createGameHandler::createGame);
        Spark.put("/game", joinGameHandler::joinGame);
        Spark.get("/game", getGamesHandler::getGames);

        Spark.delete("/db", clearHandler::clearDatabase);

        Spark.exception(ResponseException.class, this::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public int getPort() {
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.getStatusCode());
        System.out.println("Error in reading request: " + ex.getMessage());
    }
}

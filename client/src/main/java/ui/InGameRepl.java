package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthToken;
import server.ServerFacade;
import websocket.WebSocketFacade;
import websocket.NotificationHandler;

import java.util.Scanner;

public class InGameRepl extends Repl {
    private final InGameClient client;
    private final String authToken;
    private final ChessGame.TeamColor playerColor;
    private WebSocketFacade ws;

    public InGameRepl(ServerFacade server, String authToken, ChessGame.TeamColor playerColor) throws ResponseException {
        this.authToken = authToken;
        this.playerColor = playerColor;
        this.client = new InGameClient(server, this, authToken);
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void run(ChessGame.TeamColor color) {
        System.out.println("Welcome to the game.");

        Scanner scanner = new Scanner((System.in));
        String result = "";
        System.out.println(helpPrompt());
        repl(client, "leave", scanner, authToken);
    }

    public final String helpPrompt() {
        return """
            Options:
            Highlight legal moves: "highlight" <position> (e.g. f5)
            Make a move: "move" <starting_position> <ending_position> <optional promotion> (e.g. f7 f8 queen)
            Redraw chess board: "redraw"
            Resign from game: "resign"
            Leave game: "leave"
            Print this message: "help"
            """;
    }
}

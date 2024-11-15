package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.AuthToken;
import model.Game;
import protocol.*;
import server.ServerFacade;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class InGameClient {
    private static InGameRepl inGameRepl;
    private final ServerFacade server;
    private static BoardDrawer boardDrawer = new BoardDrawer();

    private HashMap<Integer, Integer> gameIDMap = new HashMap<>();

    public InGameClient(ServerFacade server, InGameRepl inGameRepl) {
        this.inGameRepl = inGameRepl;
        this.server = server;
    }

    public String evalInput(String input, String authToken) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> "leave";
                default -> inGameRepl.helpPrompt();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }
    private static void redraw() {
        boardDrawer.drawChessBoard(new ChessBoard(), inGameRepl.getPlayerColor());
    }
    private static String printChessBoard(ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);



        return "";
    }
}
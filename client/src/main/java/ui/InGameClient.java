package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.AuthToken;
import model.Game;
import org.junit.platform.commons.util.BlacklistedExceptions;
import protocol.*;
import server.ServerFacade;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class InGameClient implements Client {
    private static InGameRepl inGameRepl;
    private final ServerFacade server;
    private static BoardDrawer boardDrawer = new BoardDrawer();

    private HashMap<Integer, Integer> gameIDMap = new HashMap<>();

    public InGameClient(ServerFacade server, InGameRepl inGameRepl) {
        this.inGameRepl = inGameRepl;
        this.server = server;
        redraw();
    }

    public String evalInput(String input, String authToken) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "highlight" -> highlight(params, authToken);
                case "redraw" -> redraw();
                case "leave" -> "leave";
                case "help" -> inGameRepl.helpPrompt();
                default -> "Invalid command; refer to the options below:\n" + inGameRepl.helpPrompt();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private static String redraw() {
        var board = new ChessBoard();
        board.resetBoard();
//        boardDrawer.drawChessBoard(board, inGameRepl.getPlayerColor());
        boardDrawer.drawChessBoard(board, ChessGame.TeamColor.WHITE);
        System.out.println();
        boardDrawer.drawChessBoard(board, ChessGame.TeamColor.BLACK);
        return "";
    }

    private static String highlight(String[] params, String authToken) {
        if (params == null || params.length != 1 || params[0].length() != 2) {
            return "Incorrect number of parameters entered; please refer to the help menu.";
        }
        String positionStr = params[0].toLowerCase();
        Integer row = Integer.parseInt(String.valueOf(params[0].charAt(0)));
        Integer col = switch(String.valueOf(params[0].charAt(0))) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> 0;
        };


        return "";
    }
}
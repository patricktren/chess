package ui;

import chess.ChessGame;
import chess.ChessPosition;
import exception.ResponseException;
import model.Game;
import protocol.*;
import server.ServerFacade;
import websocket.WebSocketFacade;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.HashMap;

public class InGameClient implements Client {
    private static InGameRepl inGameRepl;
    private final ServerFacade server;
    private static BoardDrawer boardDrawer = new BoardDrawer();
    private WebSocketFacade ws;

    private final String authToken;

    public InGameClient(ServerFacade server, InGameRepl inGameRepl, String authToken) throws ResponseException {
        this.inGameRepl = inGameRepl;
        this.server = server;
        this.authToken = authToken;
        this.ws = new WebSocketFacade(server, inGameRepl, authToken, server.username);
        ws.enterGame(server.username, server.playerColor);
    }

    public String evalInput(String input, String authToken) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "highlight" -> highlight(params, authToken);
                case "redraw" -> redraw(null);
                case "leave" -> leave();
                case "resign" -> resign();
                case "move" -> move(params, authToken);
                case "help" -> inGameRepl.helpPrompt();
                default -> "Invalid command; refer to the options below:\n" + inGameRepl.helpPrompt();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String move(String[] params, String authToken) {
        try {
            String paramStr = String.join(" ", params);
            ws.makeMove(paramStr, authToken);
            return "";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String redraw(ChessPosition positionToHighlightMoves) {
        ChessGame currGame = getCurrGame();
        boardDrawer.drawChessBoard(currGame.getBoard(), inGameRepl.getPlayerColor(), positionToHighlightMoves);
        return "";
    }


    private String highlight(String[] params, String authToken) {
        if (params == null || params.length != 1 || params[0].length() != 2) {
            return "Incorrect number of parameters entered; please refer to the help menu.";
        }
        try {
            Integer row = Integer.parseInt(String.valueOf(params[0].charAt(1)));
            Integer col = ws.parseLetterToInt(params[0].charAt(0));
            redraw(new ChessPosition(row, col));

            return "";
        }
        catch (Exception e) {
            return "Parameters incorrectly entered; please type 'help' to see options";
        }
    }

    private ChessGame getCurrGame() {
        try {
            GetGamesResponse gameList = server.list(new GetGamesRequest(this.authToken));
            for (Game game:gameList.games()) {
                if (game.gameID().equals(server.getCurrGameId())) {
                    return game.gameState();
                }
            }
            return null;
        }
        catch (Exception er) {
            return null;
        }
    }

    public String leave() throws ResponseException {
        ws.leaveGame(server.username);
        return "leave";
    }

    public String resign() throws ResponseException {
        ws.resign(server.username);
        return "";
    }
}
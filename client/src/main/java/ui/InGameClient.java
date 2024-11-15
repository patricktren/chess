package ui;

import chess.ChessGame;
import model.AuthToken;
import model.Game;
import protocol.*;
import server.ServerFacade;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class InGameClient {
    private final InGameRepl inGameRepl;
    private final ServerFacade server;

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
                case "leave" -> "leave";
                default -> inGameRepl.helpPrompt();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }
}
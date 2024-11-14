package ui;

import chess.ChessGame;
import model.AuthToken;
import model.Game;
import protocol.CreateGameRequest;
import protocol.GetGamesRequest;
import protocol.GetGamesResponse;
import protocol.JoinGameRequest;
import server.ServerFacade;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class PostloginClient {
    private final PostloginRepl postloginRepl;
    private final ServerFacade server;

    private HashMap<Integer, Integer> gameIDMap = new HashMap<>();

    public PostloginClient(ServerFacade server, PostloginRepl postloginRepl) {
        this.postloginRepl = postloginRepl;
        this.server = server;
    }

    public String evalInput(String input, String authToken) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "list" -> list(authToken);
                case "create" -> create(params, authToken);
                case "join" -> join(params, authToken);
//                case "logout" -> logout(authToken);
                case "quit" -> "quit";
                default -> postloginRepl.helpPrompt();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String list(String authToken) {
        try {
            GetGamesResponse gamesResponse = server.list(new GetGamesRequest(authToken));
            String gameString = "";
            ArrayList<Game> games = (ArrayList<Game>) gamesResponse.games();
            gameIDMap.clear();
            for (int i=0; i < games.size(); i++) {
                Game game = games.get(i);
                gameIDMap.put(i+1, game.gameID());
                gameString += String.format("%d. Game name: %-8s White: %-8s Black: %-8s\n", i + 1, game.gameName(), game.whiteUsername(), game.blackUsername());
            }

            return gameString;
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String create (String[] params, String authToken) {
        try {
            String gameName = params[0];
            String gameID = server.create(new CreateGameRequest(authToken, gameName)).gameID().toString();
            return "";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String join(String[] params, String authToken) {
        try {
            // get game number
            Integer gameNum = Integer.parseInt(params[0]);

            // get player color
            ChessGame.TeamColor playerColor = null;
            if (params[1].equalsIgnoreCase("white")) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (params[1].equalsIgnoreCase("black")){
                playerColor = ChessGame.TeamColor.BLACK;
            } else {
                return "Invalid player color";
            }

            // join
            if (!gameIDMap.containsKey(gameNum)) {
                return "Invalid game ID.";
            }
            server.join(new JoinGameRequest(authToken, playerColor, gameIDMap.get(gameNum)));
            return "";
        } catch (Throwable e) {
            return switch (Integer.parseInt(e.getMessage())) {
                case 403 -> "Player color taken; please try another game or player color";
                default -> e.getMessage();
            };
        }
    }
//    private String logout(String authToken) {
//        try {
//            GetGamesResponse gamesResponse = server.logout(new GetGamesRequest(authToken));
//            return "";
//        } catch (Throwable e) {
//            return e.getMessage();
//        }
//    }
}

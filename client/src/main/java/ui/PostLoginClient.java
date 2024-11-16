package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.Game;
import protocol.*;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PostLoginClient implements Client {
    private final PostLoginRepl postloginRepl;
    private final ServerFacade server;

    private HashMap<Integer, Integer> gameIDMap = new HashMap<>();

    public PostLoginClient(ServerFacade server, PostLoginRepl postloginRepl) {
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
                case "logout" -> logout(authToken);
                case "watch" -> watch(params, authToken);
                case "quit" -> "quit";
                default -> "Invalid command; refer to the options below:\n" + postloginRepl.helpPrompt();
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
//            gameIDMap.clear();
            for (int i=0; i < games.size(); i++) {
                Game game = games.get(i);
//                gameIDMap.put(i+1, game.gameID());
                gameString += String.format("%d. Game name: %-8s White: %-8s Black: %-8s\n",
                        i + 1, game.gameName(), game.whiteUsername(), game.blackUsername());
            }

            return gameString;
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String create (String[] params, String authToken) {
        if (params == null || params.length != 1) {
            return "Incorrect number of parameters entered; please refer to the help menu.";
        }
        try {
            String gameName = params[0];
            String gameID = server.create(new CreateGameRequest(authToken, gameName)).gameID().toString();
            return "";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String join(String[] params, String authToken) {
        if (params == null || params.length != 2) {
            return "Incorrect number of parameters entered; please refer to the help menu.";
        }
        try {
            // get game number
            Integer gameNum = null;
            try {
                gameNum = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                return "Invalid game ID; please try again";
            }

            // get player color
            ChessGame.TeamColor playerColor = null;
            if (params[1].equalsIgnoreCase("white")) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (params[1].equalsIgnoreCase("black")){
                playerColor = ChessGame.TeamColor.BLACK;
            } else {
                return "Invalid player color; please try again";
            }

            server.join(new JoinGameRequest(authToken, playerColor, gameNum));
            new InGameRepl(server, authToken, playerColor);
            var draw = new BoardDrawer();
            var board = new ChessBoard();
            board.resetBoard();
            draw.drawChessBoard(board, playerColor);
            return "";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }
    private String logout(String authToken) {
        try {
            LogoutResponse gamesResponse = server.logout(new LogoutRequest(authToken));
            return "logout";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String watch(String[] params, String authToken) {
        return "To be implemented";
    }
}

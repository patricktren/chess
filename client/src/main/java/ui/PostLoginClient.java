package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.Game;
import protocol.*;
import server.ServerFacade;
import websocket.WebSocketFacade;

import java.util.*;

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
                case "help" -> postloginRepl.helpPrompt();
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
            return "Game successfully created!";
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
            server.setPlayerColor(playerColor);
            new InGameRepl(server, authToken, playerColor).run(playerColor);
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
        if (params == null || params.length != 1) {
            return "Incorrect number of parameters entered; please refer to the help menu.";
        }

        // get game number
        Integer gameNum = null;
        try {
            gameNum = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            return "Invalid game ID; please try again";
        }
        try {
            GetGamesResponse getGamesResponse = server.list(new GetGamesRequest(authToken));
            for (Game game:getGamesResponse.games()) {
                if (Objects.equals(server.getGameIDMap().get(gameNum), game.gameID())) {
                    WebSocketFacade ws = new WebSocketFacade(server, postloginRepl, authToken, server.username);
                    server.setCurrGameId(game.gameID());
                    server.setAuthToken(authToken);
                    ws.enterGame(server.username, null);
                    Scanner scanner = new Scanner((System.in));
                    String line = "";
                    while (!line.equals("leave")) {
                        line = scanner.nextLine();
                        if (!Objects.equals(line, "leave")) {
                            System.out.println("Invalid command; you may type 'leave' to leave, or keep watching.");
                        }
                        else {
                            ws.leaveGame(server.username);
                        }
                    }

                }
            }
        } catch (Throwable e) {
            return "Couldn't connect to database";
        }
        return "";
    }
}

package ui;

import model.AuthToken;
import protocol.CreateGameRequest;
import server.ServerFacade;

import java.util.Arrays;

public class PostloginClient {
    private final PostloginRepl postloginRepl;
    private final ServerFacade server;

    public PostloginClient(ServerFacade server, PostloginRepl postloginRepl) {
        this.postloginRepl = postloginRepl;
        this.server = server;
    }

    public String evalInput(String input, String authToken) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
//                case "list" -> list(authToken);
                case "create" -> create(params, authToken);
                case "quit" -> "quit";
                default -> postloginRepl.helpPrompt();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

//    private String list(AuthToken authToken) {
//        try {
//            ArrayList<Game> games = server.list(authToken);
//            return games.toString();
//        } catch (Throwable e) {
//            return e.getMessage();
//        }
//    }

    private String create (String[] params, String authToken) {
        try {
            String gameName = params[0];
            String gameID = server.create(new CreateGameRequest(authToken, gameName)).gameID().toString();
            return gameID;
        } catch (Throwable e) {
            return e.getMessage();
        }
    }
}

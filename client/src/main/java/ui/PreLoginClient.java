package ui;

import protocol.LoginRequest;
import protocol.RegisterRequest;
import server.ServerFacade;

import java.util.Arrays;

public class PreLoginClient implements Client{
    private final String serverUrl;
    private final PreLoginRepl preloginRepl;
    private ServerFacade server;

    public PreLoginClient(String serverUrl, PreLoginRepl preloginRepl) {
        this.serverUrl = serverUrl;
        this.preloginRepl = preloginRepl;
    }

    public String evalInput(String input, String authToken) {
        server = new ServerFacade(serverUrl, authToken);
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                case "help" -> preloginRepl.helpPrompt();
                default -> "Invalid command; refer to the options below:\n" + preloginRepl.helpPrompt();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String login(String[] params) {
        if (params == null || params.length != 2) {
            return "Incorrect number of parameters entered; please refer to the help menu.";
        }
        try {
            String username = params[0];
            String password = params[1];
            String authToken = server.login(new LoginRequest(username, password)).authToken();
            if (authToken != null) {
                new PostLoginRepl(server, authToken).run();
            }
            return "Welcome";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String register(String[] params) {
        if (params == null || params.length != 3) {
            return "Incorrect number of parameters entered; please refer to the help menu.";
        }
        try {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            String authToken = server.register(new RegisterRequest(username, password, email)).authToken();
            if (authToken != null) {
                new PostLoginRepl(server, authToken).run();
            }
            return "Welcome";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }
}

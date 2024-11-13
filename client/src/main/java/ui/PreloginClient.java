package ui;

import model.AuthToken;
import model.User;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PreloginClient {
    private final String serverUrl;
    private final PreloginRepl preloginRepl;
    private final ServerFacade server;

    public PreloginClient(String serverUrl, PreloginRepl preloginRepl) {
        this.serverUrl = serverUrl;
        this.preloginRepl = preloginRepl;
        server = new ServerFacade(serverUrl);
    }

    public String evalInput(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> "Welcome " + login(params);
                case "register" -> "Welcome " + register(params);
                case "quit" -> "quit";
                default -> preloginRepl.helpPrompt();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }
    private String login(String[] params) {
        try {
            String username = params[0];
            String password = params[1];
            AuthToken authToken = server.login(new User(username, password, null));
            return authToken.username();
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String register(String[] params) {
        try {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            AuthToken authToken = server.register(new User(username, password, email));
            return authToken.username();
        } catch (Throwable e) {
            return e.getMessage();
        }
    }
}

package ui;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PreloginClient {
    String serverUrl;
    PreloginRepl preloginRepl;
    public PreloginClient(String serverUrl, PreloginRepl preloginRepl) {
        serverUrl = serverUrl;
        preloginRepl = preloginRepl;
    }

    public String evalInput(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                case "help" -> preloginRepl.helpPrompt();
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
            return "";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    private String register(String[] params) {
        try {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            return "";
        } catch (Throwable e) {
            return e.getMessage();
        }
    }
}

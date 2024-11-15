package ui;

import server.ServerFacade;

import java.util.Scanner;

public class PostLoginRepl extends Repl{
    private final PostLoginClient client;
    private final String authToken;
    public PostLoginRepl(ServerFacade server, String authToken) {
        this.client = new PostLoginClient(server, this);
        this.authToken = authToken;
    }

    public void run() {
        System.out.println("Welcome to Chess. Select an option below.");

        Scanner scanner = new Scanner((System.in));

        System.out.println(helpPrompt());
        repl(client, "logout", scanner, authToken);
    }

    @Override
    public final String helpPrompt() {
        return """
            Options:
            List current games: "list"
            Create a new game: "create" <GAME NAME>
            Join a game: "join" <GAME ID> <COLOR>
            Watch a game: "watch" <GAME ID>
            Logout: "logout"
            Print this message: "help"
            """;
    }
}

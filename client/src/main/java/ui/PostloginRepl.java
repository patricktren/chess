package ui;

import model.AuthToken;
import server.ServerFacade;

import java.util.Scanner;

public class PostloginRepl {
    private final PostloginClient client;
    private final String authToken;
    public PostloginRepl(ServerFacade server, String authToken) {
        this.client = new PostloginClient(server, this);
        this.authToken = authToken;
    }

    public void run() {
        System.out.println("Welcome to Chess. Select an option below.");

        Scanner scanner = new Scanner((System.in));
        String result = "";
        System.out.println(helpPrompt());
        while (!result.equals("logout")) {
            String line = scanner.nextLine();

            try {
                result = client.evalInput(line, authToken);
                System.out.println(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg);
            }
        }
    }

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

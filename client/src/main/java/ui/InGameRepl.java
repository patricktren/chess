package ui;

import model.AuthToken;
import server.ServerFacade;

import java.util.Scanner;

public class InGameRepl {
    private final InGameClient client;
    private final String authToken;
    public InGameRepl(ServerFacade server, String authToken) {
        this.client = new InGameClient(server, this);
        this.authToken = authToken;
    }

    public void run() {
        System.out.println("Welcome to the game.");

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
            Highlight legal moves: "highlight" <position> (e.g. f5)
            Make a move: "move" <starting_position> <ending_position> <optional promotion> (e.g. f5 e4 q)
            Redraw chess board: "redraw"
            Change color scheme: "colors" <color number>
            Resign from game: "resign"
            Leave game: "leave"
            Print this message: "help"
            """;
    }
}

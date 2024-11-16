package ui;

import java.util.Scanner;

public class PreLoginRepl extends Repl{
    private final PreLoginClient client;
    public PreLoginRepl(String serverUrl) {
        client = new PreLoginClient(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to Chess. Login or register to start.");
        System.out.println(helpPrompt());

        Scanner scanner = new Scanner((System.in));
        String result = "";
        repl(client, "quit", scanner, null);
    }

    @Override
    public final String helpPrompt() {
        return """
            Options:
            Login as an existing user: "login" <USERNAME> <PASSWORD>
            Register as a new user: "register" <USERNAME> <PASSWORD> <EMAIL>
            Exit the program: "quit"
            Print this message: "help"
            """;
    }
}

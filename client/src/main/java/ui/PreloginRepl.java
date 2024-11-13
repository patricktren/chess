package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PreloginRepl {
    private final PreloginClient client;
    public PreloginRepl(String serverUrl) {
        client = new PreloginClient(serverUrl, this);
    }

    public void run() {
        System.out.println("Welcome to Chess. Login or register to start.");

        Scanner scanner = new Scanner((System.in));
        String result = "";
        while (!result.equals("quit")) {
            System.out.println(helpPrompt());

            String line = scanner.nextLine();

            try {
                result = client.evalInput(line);
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
            Login as an existing user: "login" <USERNAME> <PASSWORD>
            Register as a new user: "register" <USERNAME> <PASSWORD> <EMAIL>
            Exit the program: "quit"
            Print this message: "help"
            """;
    }
}

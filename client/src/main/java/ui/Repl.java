package ui;

import model.AuthToken;

import java.util.Scanner;

public class Repl {
    public void repl(Client client, String exitInput, Scanner scanner, String authToken) {
        String result = "";
        while (!result.equals(exitInput)) {
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
    public String helpPrompt() {
        return "";
    }
}

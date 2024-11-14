//import chess.*;
import ui.PreloginRepl;

public class ClientMain {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new PreloginRepl(serverUrl).run();
    }
}
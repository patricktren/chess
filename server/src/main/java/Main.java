import chess.*;
import com.google.gson.Gson;
import spark.*;
import java.util.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        new Server().run(8080);
    }
}
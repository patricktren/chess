package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import protocol.*;
import server.Server;
import server.ServerFacade;
import service.CreateGameService;
import service.LogoutService;


public class ServerFacadeTests {

    private static Server server;
    private static String serverUrl = "http://localhost:8080";
    private static ServerFacade serverFacade = new ServerFacade(serverUrl);

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started Main HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerSuccess() throws ResponseException {
        var registerRequest = new RegisterRequest("pat", "ree", "ree@gmail.com");
        var registerResponse = serverFacade.register(registerRequest);

        var expectedResponse = new RegisterResponse("pat", "authToken");

        Assertions.assertEquals(expectedResponse.username(), registerResponse.username());
        Assertions.assertNotNull(registerResponse);
    }

    @Test
    public void registerInvalidUsername() throws ResponseException {
        var registerRequest = new RegisterRequest("", "ree", "ree@gmail.com");

        Assertions.assertThrows(ResponseException.class, () -> {
            var registerResponse = serverFacade.register(registerRequest);
        });
    }

    @Test
    public void loginSuccess() throws ResponseException {
        var loginRequest = new LoginRequest("pat", "ree");
        var loginResponse = serverFacade.login(loginRequest);

        var expectedResponse = new LoginResponse("pat", "authToken");
        Assertions.assertEquals(expectedResponse.username(), loginResponse.username());
        Assertions.assertNotNull(loginResponse);
    }

    @Test
    public void loginInvalidPassword() throws ResponseException {
        var loginRequest = new LoginRequest("pat", "");
        Assertions.assertThrows(ResponseException.class, () -> {
            var loginResponse = serverFacade.login(loginRequest);
        });
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        var loginRequest = new LoginRequest("pat", "ree");
        var loginResponse = serverFacade.login(loginRequest);

        var createRequest = new CreateGameRequest(loginResponse.authToken(), "myGame");
        var createResponse = serverFacade.create(createRequest);

        Assertions.assertTrue(createResponse.gameID() >= 0);

        serverFacade.logout(new LogoutRequest(loginResponse.authToken()));
    }

    @Test
    public void createGameEmptyGameName() throws ResponseException {
        var loginRequest = new LoginRequest("pat", "ree");
        var loginResponse = serverFacade.login(loginRequest);

        var createRequest = new CreateGameRequest(loginResponse.authToken(), "");

        Assertions.assertThrows(ResponseException.class, () -> {
            var createResponse = serverFacade.create(createRequest);
        });
        serverFacade.logout(new LogoutRequest(loginResponse.authToken()));
    }

    @Test
    public void listGameSuccess() throws ResponseException {
        var loginRequest = new LoginRequest("pat", "ree");
        var loginResponse = serverFacade.login(loginRequest);

        GetGamesRequest listRequest = new GetGamesRequest(loginResponse.authToken());

        GetGamesResponse listResponse = serverFacade.list(listRequest);

        Assertions.assertTrue(!listResponse.games().isEmpty());

    }

    @Test
    public void listGameInvalidAuth() throws ResponseException {
        Assertions.assertThrows(ResponseException.class, () -> {
            GetGamesRequest listRequest = new GetGamesRequest("invalidAuth");

            GetGamesResponse listResponse = serverFacade.list(listRequest);
        });
    }

    @Test
    public void logoutSuccess() throws ResponseException {
        var loginRequest = new LoginRequest("pat", "ree");
        var loginResponse = serverFacade.login(loginRequest);

        var logoutRequest = new LogoutRequest(loginResponse.authToken());
        LogoutResponse logoutResponse = serverFacade.logout(logoutRequest);

        Assertions.assertTrue(logoutResponse != null);
    }

    @Test
    public void logoutInvalidAuth() throws ResponseException {
        var loginRequest = new LoginRequest("pat", "ree");
        var loginResponse = serverFacade.login(loginRequest);

        var logoutRequest = new LogoutRequest("invalidAuth");

        Assertions.assertThrows(ResponseException.class, () -> {
            LogoutResponse logoutResponse = serverFacade.logout(logoutRequest);
        });
    }


}

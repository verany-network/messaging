package net.verany.messaging;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.verany.messaging.command.AnswerCommand;
import net.verany.messaging.command.AuthCommand;
import net.verany.messaging.command.RedirectCommand;
import net.verany.messaging.database.DatabaseManager;
import net.verany.messaging.websocket.SocketServer;
import net.verany.messaging.websocket.WebSocketManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Getter
public class VeranyMessenger {

    public static VeranyMessenger INSTANCE;

    private final WebSocketManager socketManager = new WebSocketManager();
    private final DatabaseManager databaseManager;

    @SneakyThrows
    public VeranyMessenger() {
        INSTANCE = this;

        MongoData data = new Gson().fromJson(getResourceFileAsString(), MongoData.class);

        databaseManager = new DatabaseManager(data.getUsername(), data.getHostname(), data.getPassword(), "socket");
        databaseManager.connect();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            socketManager.getClients().forEach((webSocket, webSocketClient) -> webSocket.close());
            databaseManager.getCollection("sockets").drop();
            databaseManager.disconnect();
        }));

        initCommands();

        SocketServer server = new SocketServer(888);
        server.setReuseAddr(true);
        server.start();
    }

    private void initCommands() {
        socketManager.registerCommand(new AuthCommand("auth"));
        socketManager.registerCommand(new RedirectCommand("redirect"));
        socketManager.registerCommand(new AnswerCommand("answer"));
    }

    private static String getResourceFileAsString() throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("data.json")) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    @AllArgsConstructor
    @Getter
    public static class MongoData {
        private final String username, password, hostname;
    }
}

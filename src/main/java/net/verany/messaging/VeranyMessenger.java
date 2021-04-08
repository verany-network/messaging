package net.verany.messaging;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.verany.messaging.command.AuthCommand;
import net.verany.messaging.database.DatabaseManager;
import net.verany.messaging.websocket.SocketServer;
import net.verany.messaging.websocket.WebSocketManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class VeranyMessenger {

    public static final WebSocketManager SOCKET_MANAGER = new WebSocketManager();
    public static DatabaseManager databaseManager;

    @SneakyThrows
    public VeranyMessenger() {
        MongoData data = new Gson().fromJson(getResourceFileAsString("data.json"), MongoData.class);

        databaseManager = new DatabaseManager(data.getUsername(), data.getHostname(), data.getPassword(), "socket");
        databaseManager.connect();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            SOCKET_MANAGER.getClients().forEach((webSocket, webSocketClient) -> webSocket.close());
            databaseManager.disconnect();
        }));

        initCommands();

        SocketServer server = new SocketServer(888);
        server.setReuseAddr(true);
        server.start();
    }

    private void initCommands() {
        SOCKET_MANAGER.registerCommand(new AuthCommand("auth"));
    }

    private static String getResourceFileAsString(String file) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(file)) {
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

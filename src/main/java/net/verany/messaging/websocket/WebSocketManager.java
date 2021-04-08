package net.verany.messaging.websocket;

import com.mongodb.client.model.Filters;
import com.mongodb.lang.Nullable;
import lombok.Getter;
import net.verany.messaging.VeranyMessenger;
import net.verany.messaging.command.Command;
import org.bson.Document;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class WebSocketManager {

    private final Map<WebSocket, WebSocketClient> clients = new HashMap<>();
    private final List<Command> commands = new ArrayList<>();

    public void manage(WebSocket socket, String s) {
        JSONObject object;
        try {
            object = new JSONObject(s);
        } catch (Exception ignored) {
            return;
        }
        if (!object.keySet().contains("cmd"))
            return;

        String command = object.getString("cmd");
        WebSocketClient client = getClient(socket);
        if (client.getKey() == null && !command.equals("auth")) return;
        WebSocketRequest request = new WebSocketRequest(client, object);

        if (getCommand(command) != null)
            getCommand(command).onExecute(request);
    }

    public void registerCommand(Command command) {
        commands.add(command);
    }

    private Command getCommand(String cmd) {
        return commands.stream().filter(command -> command.getCommand().equalsIgnoreCase(cmd)).findFirst().orElse(null);
    }

    public WebSocketClient getClient(String key) {
        return clients.values().stream().filter(webSocketClient -> webSocketClient.getKey().equals(key)).findFirst().orElse(null);
    }

    public List<WebSocketClient> getClients(@Nullable String type) {
        List<WebSocketClient> toReturn = new ArrayList<>();
        for (Document document : VeranyMessenger.INSTANCE.getDatabaseManager().getCollection("sockets").find(type == null ? Filters.all("type") : Filters.eq("type", type))) {
            String key = document.getString("key");

            WebSocketClient client = getClients().values().stream().filter(webSocketClient -> webSocketClient.getKey().equals(key)).findFirst().orElse(null);
            if (client == null) continue;
            toReturn.add(client);
        }
        return toReturn;
    }

    public WebSocketClient getClient(WebSocket socket) {
        clients.putIfAbsent(socket, new WebSocketClient(socket));
        return clients.get(socket);
    }
}

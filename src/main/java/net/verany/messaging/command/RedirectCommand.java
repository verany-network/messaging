package net.verany.messaging.command;

import net.verany.messaging.VeranyMessenger;
import net.verany.messaging.websocket.WebSocketClient;
import net.verany.messaging.websocket.WebSocketRequest;

import java.util.List;

public class RedirectCommand extends Command {

    public RedirectCommand(String command) {
        super(command);
    }

    @Override
    public void onExecute(WebSocketRequest request) {
        String type = request.getRequest().has("type") ? request.getRequest().getString("type") : null;

        List<WebSocketClient> targetClients = VeranyMessenger.INSTANCE.getSocketManager().getClients(type);
        targetClients.forEach(webSocketClient -> webSocketClient.send(request.getRequest().toString()));
    }
}

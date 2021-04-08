package net.verany.messaging.command;

import net.verany.messaging.VeranyMessenger;
import net.verany.messaging.websocket.WebSocketClient;
import net.verany.messaging.websocket.WebSocketRequest;

public class AnswerCommand extends Command {

    public AnswerCommand(String command) {
        super(command);
    }

    @Override
    public void onExecute(WebSocketRequest request) {
        String key = request.getRequest().getString("id");
        WebSocketClient client = VeranyMessenger.INSTANCE.getSocketManager().getClient(key);
        if (client == null) return;
        client.send(request.getRequest().toString());
    }
}

package net.verany.messaging.command;

import net.verany.messaging.websocket.WebSocketRequest;

public class RedirectCommand extends Command {

    public RedirectCommand(String command) {
        super(command);
    }

    @Override
    public void onExecute(WebSocketRequest request) {
        if (!request.getRequest().has("type")) return;
        String type = request.getRequest().getString("type");

    }
}

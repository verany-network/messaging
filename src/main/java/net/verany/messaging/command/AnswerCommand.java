package net.verany.messaging.command;

import net.verany.messaging.websocket.WebSocketRequest;

public class AnswerCommand extends Command {

    public AnswerCommand(String command) {
        super(command);
    }

    @Override
    public void onExecute(WebSocketRequest request) {

    }
}

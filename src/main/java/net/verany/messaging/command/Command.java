package net.verany.messaging.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.verany.messaging.websocket.WebSocketRequest;

@RequiredArgsConstructor
@Getter
public abstract class Command {

    private final String command;

    public abstract void onExecute(WebSocketRequest request);

}

package net.verany.messaging.websocket;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.java_websocket.WebSocket;

@Getter
@Setter
@RequiredArgsConstructor
public class WebSocketClient {

    private final WebSocket socket;
    private String name, key = null;

    public void send(String message) {
        if (key == null) return;
        socket.send(message);
    }
}
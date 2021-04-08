package net.verany.messaging.websocket;

import net.verany.messaging.VeranyMessenger;
import net.verany.messaging.utils.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class SocketServer extends WebSocketServer {

    public SocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        new Logger(webSocket.getRemoteSocketAddress().getAddress().toString() + " requested connection...");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        new Logger(s + " disconnected.");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        VeranyMessenger.INSTANCE.getSocketManager().manage(webSocket, s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        new Logger("WebSocket Server started.");
        setConnectionLostTimeout(100);
    }

}

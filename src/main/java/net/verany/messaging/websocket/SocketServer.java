package net.verany.messaging.websocket;

import net.verany.messaging.VeranyMessenger;
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
        System.out.println(webSocket.getRemoteSocketAddress().getAddress().toString() + " hat sich verbunden");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        System.out.println(webSocket.getRemoteSocketAddress().getAddress().toString() + " hat verlassen");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println(webSocket.getRemoteSocketAddress().getAddress().toString() + " hat " + s + " geschrieben");
        VeranyMessenger.SOCKET_MANAGER.manage(webSocket, s);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket Server started");
        setConnectionLostTimeout(100);
    }

}

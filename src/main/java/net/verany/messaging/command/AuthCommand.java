package net.verany.messaging.command;

import com.mongodb.client.model.Filters;
import net.verany.messaging.VeranyMessenger;
import net.verany.messaging.utils.Logger;
import net.verany.messaging.websocket.WebSocketRequest;
import org.bson.Document;

public class AuthCommand extends Command {

    public AuthCommand(String command) {
        super(command);
    }

    @Override
    public void onExecute(WebSocketRequest request) {
        String key = request.getRequest().getString("key");
        Document document = VeranyMessenger.INSTANCE.getDatabaseManager().getCollection("sockets").find(Filters.eq("key", key)).first();
        if (document == null) {
            new Logger("Authentication failed with key " + key);
            return;
        }
        new Logger("Authentication success with key " + key + " (" + request.getClient().getSocket().getRemoteSocketAddress().getAddress().toString() + ")");
        request.getClient().setKey(key);
    }
}

package net.verany.messaging.command;

import com.mongodb.client.model.Filters;
import net.verany.messaging.VeranyMessenger;
import net.verany.messaging.websocket.WebSocketRequest;
import org.bson.Document;

public class AuthCommand extends Command {

    public AuthCommand(String command) {
        super(command);
    }

    @Override
    public void onExecute(WebSocketRequest request) {
        String key = request.getRequest().getString("key");
        Document document = VeranyMessenger.databaseManager.getCollection("sockets").find(Filters.eq("key", key)).first();
        if (document == null) {
            System.out.println("Could not auth requested key " + key);
            return;
        }
        System.out.println("Authed " + key);
        request.getClient().setKey(key);
    }
}

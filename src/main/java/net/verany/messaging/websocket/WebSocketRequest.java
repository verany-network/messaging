package net.verany.messaging.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.JSONObject;

@AllArgsConstructor
@Getter
public class WebSocketRequest {

    private final WebSocketClient client;
    private final JSONObject request;

    public boolean wantAnswer() {
        return request.keySet().contains("id");
    }

    public void answer(JSONObject answer) {
        if (wantAnswer())
            client.send(answer.put("id", request.get("id")).toString());
    }

}

package tech.carrotly.restapi.chat.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.java_websocket.WebSocket;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Connection {
    private WebSocket webSocket;
}

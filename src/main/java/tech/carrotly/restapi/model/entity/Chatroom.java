package tech.carrotly.restapi.model.entity;

import lombok.*;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chatroom {

    private UUID uuid;

    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    private Connection helpGiver;
    private Connection helpSeeker;
}

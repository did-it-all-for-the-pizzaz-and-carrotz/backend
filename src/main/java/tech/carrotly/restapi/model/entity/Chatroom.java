package tech.carrotly.restapi.model.entity;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private SocketIOClient helpGiver;
    private SocketIOClient helpSeeker;
}

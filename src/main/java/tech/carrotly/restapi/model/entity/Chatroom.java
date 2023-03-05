package tech.carrotly.restapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.carrotly.restapi.chat.models.Connection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chatroom implements Serializable {

    private UUID uuid;

    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    private Connection helpGiver;
    private Connection helpSeeker;
}

package tech.carrotly.restapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.carrotly.restapi.chat.dtos.GsonSerializable;
import tech.carrotly.restapi.chat.enums.Age;
import tech.carrotly.restapi.chat.models.Connection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chatroom extends GsonSerializable {

    private UUID uuid;
    private LocalDateTime localDateTime;
    Age ageOfHelpSeeker;

    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    private Connection helpGiver;
    private Connection helpSeeker;
    private Boolean assistantRequested;


}

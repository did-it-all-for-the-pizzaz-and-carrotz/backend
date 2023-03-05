package tech.carrotly.restapi.chat.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessage implements Serializable {
    UUID chatroomUuid;
    Participant sender;
    String message;
}

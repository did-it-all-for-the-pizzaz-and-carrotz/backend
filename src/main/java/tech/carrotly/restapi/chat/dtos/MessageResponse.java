package tech.carrotly.restapi.chat.dtos;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.carrotly.restapi.chat.enums.Participant;

import java.io.Serializable;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse extends GsonSerializable {
    UUID chatroomUuid;
    Participant sender;
    String message;
}

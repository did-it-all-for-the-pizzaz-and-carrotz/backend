package tech.carrotly.restapi.chat.dtos;

import com.google.gson.Gson;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageRequest extends GsonSerializable {
    UUID chatroomUuid;
    String message;
}

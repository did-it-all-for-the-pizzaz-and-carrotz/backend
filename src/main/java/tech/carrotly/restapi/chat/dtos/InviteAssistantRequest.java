package tech.carrotly.restapi.chat.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InviteAssistantRequest extends GsonSerializable {
    UUID chatroomUuid;
}

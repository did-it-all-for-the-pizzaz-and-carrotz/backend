package tech.carrotly.restapi.chat.payloads;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RequestAssistant {
    UUID chatroomUuid;
}

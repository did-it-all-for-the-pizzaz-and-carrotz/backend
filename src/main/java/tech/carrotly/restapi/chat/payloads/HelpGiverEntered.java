package tech.carrotly.restapi.chat.payloads;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class HelpGiverEntered {
    UUID chatroomUuid;
}

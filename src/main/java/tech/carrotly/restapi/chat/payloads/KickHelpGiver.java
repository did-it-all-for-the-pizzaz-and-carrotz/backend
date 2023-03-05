package tech.carrotly.restapi.chat.payloads;

import lombok.Builder;

import java.util.UUID;

@Builder
public class KickHelpGiver {
    UUID chatroomUuid;
}

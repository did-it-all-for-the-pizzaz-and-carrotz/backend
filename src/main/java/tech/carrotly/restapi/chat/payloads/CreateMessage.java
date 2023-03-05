package tech.carrotly.restapi.chat.payloads;

import java.util.UUID;

public class CreateMessage {
    UUID chatroomUuid;
    Participant sender;
    String message;
}

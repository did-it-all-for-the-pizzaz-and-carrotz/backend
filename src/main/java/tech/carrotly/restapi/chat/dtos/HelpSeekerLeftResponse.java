package tech.carrotly.restapi.chat.dtos;

import lombok.Builder;

import java.util.UUID;

@Builder
public class HelpSeekerLeftResponse extends GsonSerializable {
    UUID chatroomUuid;
}

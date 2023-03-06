package tech.carrotly.restapi.chat.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class HelpGiverEnteredResponse extends GsonSerializable {
    UUID chatroomUuid;
}

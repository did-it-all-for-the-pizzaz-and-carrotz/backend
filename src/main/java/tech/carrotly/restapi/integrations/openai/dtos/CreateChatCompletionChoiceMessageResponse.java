package tech.carrotly.restapi.integrations.openai.dtos;

import lombok.Getter;
import lombok.Setter;
import tech.carrotly.restapi.integrations.openai.enums.ChatParticipant;

@Getter
@Setter
public class CreateChatCompletionChoiceMessageResponse {
    ChatParticipant role;
    String content;
}

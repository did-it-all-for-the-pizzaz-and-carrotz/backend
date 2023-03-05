package tech.carrotly.restapi.integrations.openai.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateChatCompletionChoiceResponse {
    Integer index;
    List<CreateChatCompletionChoiceMessageResponse> messages;
    String finishReason;
}

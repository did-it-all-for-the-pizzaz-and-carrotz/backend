package tech.carrotly.restapi.integrations.openai.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateChatCompletionResponse {
    String id;
    String object;
    Integer created;
    List<CreateChatCompletionChoiceResponse> choices;
    CreateChatCompletionUsageResponse usage;
}


package tech.carrotly.restapi.integrations.openai.dtos;

import lombok.Builder;

@Builder
public class CreateChatCompletionMessageRequest {
    String role;
    String content;
}

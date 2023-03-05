package tech.carrotly.restapi.integrations.openai.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
public class CreateChatCompletionRequest {
    private String secretKey;
    private List<CreateChatCompletionMessageRequest> messages;
    private String model;
    private Float temperature;
    private Integer maxTokens;
    private Float presencePenalty;
    private Float frequencyPenalty;
}

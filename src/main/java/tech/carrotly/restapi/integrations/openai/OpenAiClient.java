package tech.carrotly.restapi.integrations.openai;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tech.carrotly.restapi.integrations.openai.dtos.CreateChatCompletionMessageRequest;
import tech.carrotly.restapi.integrations.openai.dtos.CreateChatCompletionRequest;
import tech.carrotly.restapi.integrations.openai.dtos.CreateChatCompletionResponse;
import tech.carrotly.restapi.integrations.openai.enums.ChatParticipant;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OpenAiClient {
    private final OpenAiApi api;
    private final String secretKey;
    private final String model;
    private final Float temperature;
    private final Integer maxTokens;
    private final Float presencePenalty;
    private final Float frequencyPenalty;
    private final String flavor;

    public CreateChatCompletionResponse createChatCompletion(List<CreateChatCompletionMessageRequest> messages) {
        CreateChatCompletionRequest request = CreateChatCompletionRequest.builder()
                .secretKey(secretKey)
                .model(model)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .presencePenalty(presencePenalty)
                .frequencyPenalty(frequencyPenalty)
                .messages(messages)
                .build();

        return api.createChatCompletion(request);
    };
}

package tech.carrotly.restapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tech.carrotly.restapi.integrations.openai.OpenAiApi;
import tech.carrotly.restapi.integrations.openai.dtos.*;
import tech.carrotly.restapi.integrations.openai.enums.ChatParticipant;
import tech.carrotly.restapi.service.AssistantService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssistantServiceImpl implements AssistantService {
    private final OpenAiApi openAiApi;

    @Value("${open-ai.secret-key}")
    private String secretKey;

    @Value("${open-ai.model}")
    private String model;

    @Value("${open-ai.temperature}")
    private Float temperature;

    @Value("${open-ai.max-tokens}")
    private Integer maxTokens;

    @Value("${open-ai.presence-penalty}")
    private Float presencePenalty;

    @Value("${open-ai.frequency-penalty}")
    private Float frequencyPenalty;

    @Value("${open-ai.flavor}")
    private String flavor;

    @Override
    public String sendMessage(String conversationId, String message) {
        List<CreateChatCompletionMessageRequest> messages = new ArrayList<>();
        messages.add(CreateChatCompletionMessageRequest.builder().role(ChatParticipant.SYSTEM).content(flavor).build());
        // Load some older messages to keep conversation context
        messages.add(CreateChatCompletionMessageRequest.builder().role(ChatParticipant.USER).content(message).build());

        CreateChatCompletionRequest request = CreateChatCompletionRequest.builder()
                .model(model)
                .temperature(temperature)
                .maxTokens(maxTokens)
                .presencePenalty(presencePenalty)
                .frequencyPenalty(frequencyPenalty)
                .messages(messages)
                .build();

        CreateChatCompletionResponse response = openAiApi.createChatCompletion(request);

        return response.getChoices().get(0).getMessage().getContent();
    }

    @Override
    public Boolean verifyMessage(String message) {
        CreateModerationRequest request = CreateModerationRequest.builder().input(message).build();
        CreateModerationResponse response = openAiApi.createModeration(request);

        return !response.getResults().get(0).getFlagged();
    }
}

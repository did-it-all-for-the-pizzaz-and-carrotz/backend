package tech.carrotly.restapi.integrations.openai;

import feign.RequestLine;
import tech.carrotly.restapi.integrations.openai.dtos.CreateChatCompletionRequest;
import tech.carrotly.restapi.integrations.openai.dtos.CreateChatCompletionResponse;

public interface OpenAiApi {
    @RequestLine("POST /v1/chat/completions")
    CreateChatCompletionResponse createChatCompletion(CreateChatCompletionRequest request);
}

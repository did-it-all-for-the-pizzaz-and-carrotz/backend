package tech.carrotly.restapi.integrations.openai;

import feign.Headers;
import feign.RequestLine;
import tech.carrotly.restapi.integrations.openai.dtos.CreateChatCompletionRequest;
import tech.carrotly.restapi.integrations.openai.dtos.CreateChatCompletionResponse;
import tech.carrotly.restapi.integrations.openai.dtos.CreateModerationRequest;
import tech.carrotly.restapi.integrations.openai.dtos.CreateModerationResponse;

public interface OpenAiApi {
    @RequestLine("POST /v1/chat/completions")
    @Headers("Content-Type: application/json")
    CreateChatCompletionResponse createChatCompletion(CreateChatCompletionRequest request);

    @RequestLine("POST /v1/moderations")
    @Headers("Content-Type: application/json")
    CreateModerationResponse createModeration(CreateModerationRequest request);
}

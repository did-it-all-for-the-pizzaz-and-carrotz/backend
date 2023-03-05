package tech.carrotly.restapi.integrations.openai.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;

import java.util.List;

@Builder
public class CreateChatCompletionRequest {
    String model;
    List<CreateChatCompletionMessageRequest> messages;
    Float temperature;
    @SerializedName("max_tokens")
    Integer maxTokens;
    @SerializedName("presence_penalty")
    Float presencePenalty;
    @SerializedName("frequency_penalty")
    Float frequencyPenalty;
}

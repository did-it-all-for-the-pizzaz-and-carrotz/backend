package tech.carrotly.restapi.integrations.openai.dtos;

import com.google.gson.annotations.SerializedName;

public class CreateChatCompletionUsageResponse {
    @SerializedName("prompt_tokens")
    Integer promptTokens;
    @SerializedName("completion_tokens")
    Integer completionTokens;
    @SerializedName("total_tokens")
    Integer totalTokens;
}

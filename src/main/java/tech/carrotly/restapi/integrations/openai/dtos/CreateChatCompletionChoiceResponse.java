package tech.carrotly.restapi.integrations.openai.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateChatCompletionChoiceResponse {
    Integer index;
    CreateChatCompletionChoiceMessageResponse message;
    @SerializedName("finish_reason")
    String finishReason;
}

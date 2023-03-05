package tech.carrotly.restapi.integrations.openai.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;

@Builder
public class CreateModerationRequest {
    String input;
}



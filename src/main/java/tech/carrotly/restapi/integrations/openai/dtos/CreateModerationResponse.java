package tech.carrotly.restapi.integrations.openai.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateModerationResponse {
    String id;
    String model;
    List<CreateModerationResultResponse> results;
}

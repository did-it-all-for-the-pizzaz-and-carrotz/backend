package tech.carrotly.restapi.integrations.openai.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateModerationResultResponse {
    CreateModerationResultCategoryResponse categories;
    @SerializedName("category_scores")
    CreateModerationResultCategoryScoreResponse categoryScores;
    Boolean flagged;
}

package tech.carrotly.restapi.integrations.openai.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateModerationResultCategoryScoreResponse {
    Float hate;
    @SerializedName("hate/threatening")
    Float hateThreatening;
    @SerializedName("self-harm")
    Float selfHarm;
    Float sexual;
    @SerializedName("sexual/minors")
    Float sexualMinors;
    Float violence;
    @SerializedName("violence/graphic")
    Float violenceGraphic;
}

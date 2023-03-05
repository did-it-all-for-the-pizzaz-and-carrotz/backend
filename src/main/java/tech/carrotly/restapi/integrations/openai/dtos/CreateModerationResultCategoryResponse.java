package tech.carrotly.restapi.integrations.openai.dtos;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateModerationResultCategoryResponse {
    Boolean hate;
    @SerializedName("hate/threatening")
    Boolean hateThreatening;
    @SerializedName("self-harm")
    Boolean selfHarm;
    Boolean sexual;
    @SerializedName("sexual/minors")
    Boolean sexualMinors;
    Boolean violence;
    @SerializedName("violence/graphic")
    Boolean violenceGraphic;
}

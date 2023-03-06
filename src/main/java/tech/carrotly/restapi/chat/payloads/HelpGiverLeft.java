package tech.carrotly.restapi.chat.payloads;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class HelpGiverLeft {
    private UUID chatroomUUID;

    public String toJson() {
        return new Gson().toJson(this);
    }
}

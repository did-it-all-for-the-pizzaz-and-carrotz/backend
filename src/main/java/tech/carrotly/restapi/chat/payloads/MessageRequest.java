package tech.carrotly.restapi.chat.payloads;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest implements Serializable {
    String chatroomUuid;
    String message;

    public String toJson() {
        return new Gson().toJson(this);
    }
}

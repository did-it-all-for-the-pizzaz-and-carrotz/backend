package tech.carrotly.restapi.chat.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatObject {
    private String roomUUID;
    private String from;
    private String content;
    private String sender;
}

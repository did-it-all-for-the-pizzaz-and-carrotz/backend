package tech.carrotly.restapi.chat.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.carrotly.restapi.chat.enums.Age;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatroomRequest {
    private Age age;
}

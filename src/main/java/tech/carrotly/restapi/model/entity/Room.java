package tech.carrotly.restapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    private String UUID;

    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    private User helper;
    private User personInNeed;

}

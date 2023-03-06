package tech.carrotly.restapi.chat.dtos;

import tech.carrotly.restapi.chat.models.Chatroom;

import java.util.Set;

public class AvailableChatroomsResponse extends GsonSerializable {
    Set<Chatroom> chatrooms;
}

package tech.carrotly.restapi.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import tech.carrotly.restapi.chat.models.ChatObject;

import java.util.UUID;

public interface ChatService {
    void addClient(SocketIOClient socketIOClient);
    UUID createChatroom();
    void addHelpSeeker(UUID chatroomUuid, SocketIOClient socketIOClient);
    void addHelpGiver(UUID chatroomUuid, SocketIOClient socketIOClient);
    void addMessage(UUID chatroomUuid, ChatObject message);
    void removeChatroom(Object chatroomUuid);
    void process(String message) throws JsonProcessingException;
}

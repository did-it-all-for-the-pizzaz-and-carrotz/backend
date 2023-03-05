package tech.carrotly.restapi.service;

import com.corundumstudio.socketio.SocketIOClient;
import tech.carrotly.restapi.chat.models.ChatObject;

import java.util.UUID;

public interface ChatService {
    void addClient(SocketIOClient socketIOClient);
    UUID createChatroom();
    void addHelpSeeker(UUID chatroomUuid, SocketIOClient socketIOClient);
    void addHelpGiver(UUID chatroomUuid, SocketIOClient socketIOClient);
    void addMessage(UUID chatroomUuid, ChatObject message);
    void removeChatroom(UUID chatroomUuid);
}

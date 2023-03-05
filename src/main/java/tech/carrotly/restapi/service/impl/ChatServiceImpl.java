package tech.carrotly.restapi.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.carrotly.restapi.chat.models.ChatObject;
import tech.carrotly.restapi.model.entity.Chatroom;
import tech.carrotly.restapi.model.entity.Message;
import tech.carrotly.restapi.service.ChatService;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final Map<UUID, Chatroom> chatrooms;
    private final Set<SocketIOClient> clients;

    @Override
    public void addClient(SocketIOClient socketIOClient) {
        clients.add(socketIOClient);
    }

    @Override
    public UUID createChatroom() {
        UUID chatroomUuid = UUID.randomUUID();
        Chatroom chatroom = Chatroom.builder().uuid(chatroomUuid).build();
        chatrooms.put(chatroomUuid, chatroom);
        return chatroomUuid;
    }

    @Override
    public void addHelpSeeker(UUID chatroomUuid, SocketIOClient socketIOClient) {
        chatrooms.get(chatroomUuid).setHelpSeeker(socketIOClient);
    }
    @Override
    public void addHelpGiver(UUID chatroomUuid, SocketIOClient socketIOClient) {
        chatrooms.get(chatroomUuid).setHelpGiver(socketIOClient);
    }

    @Override
    public void addMessage(UUID chatroomUuid, ChatObject object) {
        Message message = Message.builder().from(object.getFrom()).content(object.getContent()).build();
        chatrooms.get(chatroomUuid).getMessages().add(message);
    }

    @Override
    public void removeChatroom(UUID chatroomUuid) {
        chatrooms.remove(chatroomUuid);
    }
}

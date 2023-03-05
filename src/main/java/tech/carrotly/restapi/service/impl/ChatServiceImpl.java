package tech.carrotly.restapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.carrotly.restapi.chat.payloads.KickHelpGiver;
import tech.carrotly.restapi.model.entity.Chatroom;
import tech.carrotly.restapi.model.entity.Connection;
import tech.carrotly.restapi.service.ChatService;

import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final Map<UUID, Chatroom> chatrooms;
    private final ObjectMapper objectMapper;

//    private Map<String, Function<?, ?>> actionMap = new HashMap<>(Map.of(
//            "createChatroom", (payload) -> createChatroom(), // wysylamy do wszystkich zalogowanych ziutow popupa
//            "removeChatroom", (payload) -> removeChatroom((String) payload), //wysylamy do lekarza payload zeby na froncie wywalilo powiadomienie o zamknieciu roomu i do reszty jako koniec
//            "helperEnteredChatroom", (payload) -> helperEnteredChatroom(payload), // front dostaje popupa ze ktos dolaczyl, room znika z mozliwosci dolaczenia dla lekarzy
//            "helperLeftChatroom", (payload) -> helperLeftChatroom(payload), // front dostaje popupa ze ktos wyszedl, room pokazuje sie na nowo dla lekarzy (15 sekund czekania) -> popup i tryb AI
//            "onMessage", (payload) -> onMessage(payload), // wyslanie wiadomosci do uzytkownikow chatroomu
//    ));
//
//     @Override
//     public void addClient(SocketIOClient socketIOClient) {
//         clients.add(socketIOClient);
//
//
//     }
//
//     @Override
//     public UUID createChatroom() {
//
//         UUID chatroomUuid;
//         do {
//             chatroomUuid = UUID.randomUUID();
//         } while(chatrooms.containsKey(chatroomUuid));
//
//         Chatroom chatroom = Chatroom.builder().uuid(chatroomUuid).build();
//         chatrooms.put(chatroomUuid, chatroom);
//         return chatroomUuid;
//     }
//
//     @Override
//     public void addHelpSeeker(UUID chatroomUuid, SocketIOClient socketIOClient) {
//         chatrooms.get(chatroomUuid).setHelpSeeker(socketIOClient);
//     }
//     @Override
//     public void addHelpGiver(UUID chatroomUuid, SocketIOClient socketIOClient) {
//         chatrooms.get(chatroomUuid).setHelpGiver(socketIOClient);
//     }
//
//     @Override
//     public void addMessage(UUID chatroomUuid, ChatObject object) {
//         Message message = Message.builder().from(object.getFrom()).content(object.getContent()).build();
//         chatrooms.get(chatroomUuid).getMessages().add(message);
//     }
//
//     @Override
//     public void removeChatroom(Object chatroomUuid) {
//         chatrooms.remove(chatroomUuid);
//     }

    @Override
    public void process(String message) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(message);
        String topic = node.get("topic").toString();
        String payload = node.get("payload").toString();

        switch(topic) {
            case "createChatroom":
                createChatroom();
                break;
            case "helperEnteredChatroom":
                helperEnteredChatroom(payload);
                break;
            case "helperLeftChatroom":
                helperLeftChatroom(payload);
                break;
            case "onMessage":
                onMessage(payload);
                break;
        }
    }

    private void createChatroom() {

    }

    private void removeChatroom(UUID chatroomUuid) {
        Chatroom chatroom = chatrooms.get(chatroomUuid);
        chatrooms.remove(chatroom.getUuid());

        KickHelpGiver kickHelpGiver = KickHelpGiver.builder().chatroomUuid(chatroomUuid).build();

        Connection connection = chatroom.getHelpGiver();
        connection.getConn().send(new Gson().toJson(kickHelpGiver));
    }

    private void helperEnteredChatroom(String json) {
        RemoveChatroom payload = new Gson().fromJson(json, RemoveChatroom.class);

    }

    // front dostaje popupa ze ktos wyszedl, room pokazuje sie na nowo dla lekarzy
    // (15 sekund czekania) -> popup i tryb AI
    private void helperLeftChatroom(String json) {
        RemoveChatroom payload = new Gson().fromJson(json, RemoveChatroom.class);
    }

    private void onMessage(String payload) {

    }

}

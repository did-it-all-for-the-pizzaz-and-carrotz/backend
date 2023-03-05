package tech.carrotly.restapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Service;
import tech.carrotly.restapi.chat.models.Connection;
import tech.carrotly.restapi.chat.payloads.KickHelpGiver;
import tech.carrotly.restapi.chat.payloads.MessageRequest;
import tech.carrotly.restapi.model.entity.Chatroom;
import tech.carrotly.restapi.model.response.CreatedChatroomResponse;
import tech.carrotly.restapi.service.ChatService;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final Map<UUID, Chatroom> chatrooms;
    private final ObjectMapper objectMapper;
    private final Set<Connection> users = new HashSet<>();

//    private Map<String, Function<?, ?>> actionMap = new HashMap<>(Map.of(
//            "createChatroom", (payload) -> createChatroom(), // wysylamy do wszystkich zalogowanych ziutow popupa
//            "removeChatroom", (payload) -> removeChatroom((String) payload), //wysylamy do lekarza payload zeby na froncie wywalilo powiadomienie o zamknieciu roomu i do reszty jako koniec
//            "helperEnteredChatroom", (payload) -> helperEnteredChatroom(payload), // front dostaje popupa ze ktos dolaczyl, room znika z mozliwosci dolaczenia dla lekarzy
//            "helperLeftChatroom", (payload) -> helperLeftChatroom(payload), // front dostaje popupa ze ktos wyszedl, room pokazuje sie na nowo dla lekarzy (15 sekund czekania) -> popup i tryb AI
//            "onMessage", (payload) -> onMessage(payload), // wyslanie wiadomosci do uzytkownikow chatroomu
//    ));

    public UUID createChatroom(WebSocket conn) {
        final UUID chatroomUuid = UUID.randomUUID();

        Chatroom chatroom = Chatroom.builder()
                .uuid(chatroomUuid)
                .helpSeeker(Connection.builder().webSocket(conn).build())
                .build();

        chatrooms.put(chatroomUuid, chatroom);

        log.info("Chatroom with UUID {} has been created", chatroomUuid);

        conn.send(
                new Gson().toJson(CreatedChatroomResponse.builder()
                        .chatroomUUID(chatroomUuid)
                        .build())
        );

        return chatroomUuid;
    }

    @Override
    public void process(String message, WebSocket conn) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(message);
        String topic = node.get("topic").toString().replaceAll("\"", "");
        String payload = node.get("payload").toString();

        switch (topic) {
            case "createChatroom" -> createChatroom(conn);
            case "helperEnteredChatroom" -> helperEnteredChatroom(payload, conn);
            case "helperLeftChatroom" -> helperLeftChatroom(payload);
            case "onMessage" -> onMessage(payload, conn);
            case "helperLogin" -> onHelperLogin(payload, conn);
            case "helperLogout" -> onHelperLogout(payload, conn);
        }
    }

    private void onHelperLogout(String payload, WebSocket conn) {
        users.remove(Connection.builder().webSocket(conn).build());
        log.info("User {} has logged out", conn.getRemoteSocketAddress());
    }

    private void onHelperLogin(String payload, WebSocket conn) {
        users.add(Connection.builder().webSocket(conn).build());
        log.info("User {} has logged in", conn.getRemoteSocketAddress());
    }

    private void createChatroom() {

    }

    private void removeChatroom(UUID chatroomUuid) {
        Chatroom chatroom = chatrooms.get(chatroomUuid);
        chatrooms.remove(chatroom.getUuid());

        KickHelpGiver kickHelpGiver = KickHelpGiver.builder().chatroomUuid(chatroomUuid).build();

        Connection connection = chatroom.getHelpGiver();
        connection.getWebSocket().send(new Gson().toJson(kickHelpGiver));
    }

    private void helperEnteredChatroom(String json, WebSocket connection) {

    }

    private void sendHelperPlaceTakenChatroom(WebSocket connection) {

    }

    // front dostaje popupa ze ktos wyszedl, room pokazuje sie na nowo dla lekarzy
    // (15 sekund czekania) -> popup i tryb AI
    private void helperLeftChatroom(String json) {
    }

    @SneakyThrows
    private void onMessage(String payload, WebSocket conn) {
        MessageRequest message = objectMapper.readValue(payload, MessageRequest.class);

        log.info(payload);
        log.info(message.toString());

        final Chatroom chatroom = chatrooms.get(UUID.fromString(message.getChatroomUuid()));

        if(users.contains(Connection.builder().webSocket(conn).build())) {
            chatroom.getHelpGiver().getWebSocket().send(message.toJson());
            return;
        }
    }

}

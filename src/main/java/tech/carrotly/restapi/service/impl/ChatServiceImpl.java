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
import tech.carrotly.restapi.chat.payloads.*;
import tech.carrotly.restapi.model.entity.Chatroom;
import tech.carrotly.restapi.model.entity.Message;
import tech.carrotly.restapi.model.response.CreatedChatroomResponse;
import tech.carrotly.restapi.service.AssistantService;
import tech.carrotly.restapi.service.ChatService;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final Map<UUID, Chatroom> chatrooms;
    private final Map<Integer, UUID> helpSeekerToChatroom;
    private final Map<Integer, UUID> helpGiverToChatroom;
    private final ObjectMapper objectMapper;
    private final Set<Connection> users = new HashSet<>();
    private final AssistantService assistantService;

    public UUID createChatroom(WebSocket conn) {
        final UUID chatroomUuid = UUID.randomUUID();

        Chatroom chatroom = Chatroom.builder()
                .uuid(chatroomUuid)
                .helpSeeker(Connection.builder().webSocket(conn).build())
                .build();

        chatrooms.put(chatroomUuid, chatroom);

        log.info("Chatroom with UUID {} has been created", chatroomUuid);

        CreatedChatroomResponse createdChatroomResponse = CreatedChatroomResponse.builder()
                .chatroomUUID(chatroomUuid)
                .build();

        String createdChatroomResponseJson = new Gson().toJson(createdChatroomResponse);
        conn.send(createdChatroomResponseJson);
        users.forEach(entry -> entry.getWebSocket().send(createdChatroomResponseJson));

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
            case "requestAssistant" -> requestAssistant(payload);
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

    public void disconnect(WebSocket conn) {
        if (helpGiverToChatroom.containsKey(conn.hashCode())) disconnectHelpGiver(helpGiverToChatroom.get(conn.hashCode()));
        if (helpSeekerToChatroom.containsKey(conn.hashCode())) disconnectHelpSeeker(helpSeekerToChatroom.get(conn.hashCode()));
    }

    private void disconnectHelpGiver(UUID chatroomUuid) {
        Chatroom chatroom = chatrooms.get(chatroomUuid);
        chatroom.setHelpGiver(null);
    }

    private void disconnectHelpSeeker(UUID chatroomUuid) {
        Chatroom chatroom = chatrooms.get(chatroomUuid);
        chatrooms.remove(chatroom.getUuid());
        HelpSeekerLeft helpSeekerLeft = HelpSeekerLeft.builder().chatroomUuid(chatroom.getUuid()).build();
        String helpSeekerLeftJson = new Gson().toJson(helpSeekerLeft);
        users.forEach(entry -> entry.getWebSocket().send(helpSeekerLeftJson));
    }

    private void helperEnteredChatroom(String json, WebSocket conn) {
        HelpGiverEntered payload = new Gson().fromJson(json, HelpGiverEntered.class);
        Chatroom chatroom = chatrooms.get(payload.getChatroomUuid());
        chatroom.setHelpGiver(Connection.builder().webSocket(conn).build());
    }

    private void helperLeftChatroom(String json) {
        HelpGiverLeft payload = new Gson().fromJson(json, HelpGiverLeft.class);
        Chatroom chatroom = chatrooms.get(payload.getChatroomUuid());
        chatroom.setHelpGiver(null);
    }

    @SneakyThrows
    private void onMessage(String payload, WebSocket conn) {
        MessageRequest message = objectMapper.readValue(payload, MessageRequest.class);

        log.info(payload);
        log.info(message.toString());

        final Chatroom chatroom = chatrooms.get(UUID.fromString(message.getChatroomUuid()));

        if (chatroom.getAssistantRequested()) {
            log.info("Send message to assistant");
            String assistantResponse = assistantService.sendMessage(message.getMessage());
            chatroom.getMessages().add(Message.builder().content(assistantResponse).from(Participant.ASSISTANT.toString()).build());
            CreateMessage assistantMessage = CreateMessage.builder().message(assistantResponse).chatroomUuid(chatroom.getUuid()).sender(Participant.ASSISTANT).build();
            conn.send(assistantMessage.toJson());
        }

        if (chatroom.getHelpGiver() != null && Objects.equals(chatroom.getHelpGiver().getWebSocket(), conn)) {
            chatroom.getHelpGiver().getWebSocket().send(message.toJson());
        }
    }

    private void requestAssistant(String json) {
        log.info(json);
        RequestAssistant payload = new Gson().fromJson(json, RequestAssistant.class);
        log.info(payload.getChatroomUuid().toString());
        Chatroom chatroom = chatrooms.get(payload.getChatroomUuid());
        chatroom.setAssistantRequested(true);

        CreateMessage createMessage = CreateMessage.builder()
                .chatroomUuid(chatroom.getUuid())
                .sender(Participant.ASSISTANT)
                .message("Hi! I am your virtual assistant and I'm here to help you. How can I serve you today?")
                .build();

        chatroom.getHelpSeeker().getWebSocket().send(new Gson().toJson(createMessage));
    }

}

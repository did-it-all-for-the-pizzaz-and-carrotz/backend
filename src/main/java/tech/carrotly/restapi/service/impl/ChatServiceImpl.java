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
import tech.carrotly.restapi.chat.enums.Participant;
import tech.carrotly.restapi.chat.models.Connection;
import tech.carrotly.restapi.chat.dtos.*;
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

    @Override
    public void process(String message, WebSocket conn) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(message);
        String topic = node.get("topic").toString().replaceAll("\"", "");
        String payload = node.get("payload").toString();

        log.debug("Received: " + topic + " | " + payload);

        switch (topic) {
            case "createChatroom" -> onChatroomCreate(conn);
            case "helperEnteredChatroom" -> onHelperEnteredChatroom(conn, payload);
            case "helperLeftChatroom" -> onHelperLeftChatroom(conn, payload);
            case "message" -> onMessage(conn, payload);
            case "requestAssistant" -> onInviteAssistant(conn, payload);
            case "helperLogin" -> onHelperLogin(conn);
            case "helperLogout" -> onHelperLogout(conn);
        }
    }

    public void onChatroomCreate(WebSocket conn) {
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
    }

    private void onHelperLogout(WebSocket conn) {
        users.remove(Connection.builder().webSocket(conn).build());
        helpGiverToChatroom.remove(conn.hashCode());
        log.info("User {} has logged out", conn.getRemoteSocketAddress());
    }

    private void onHelperLogin(WebSocket conn) {
        users.add(Connection.builder().webSocket(conn).build());
        log.info("User {} has logged in", conn.getRemoteSocketAddress());
    }

    private void onHelperEnteredChatroom(WebSocket conn, String json) {
        HelpGiverEnteredResponse payload = new Gson().fromJson(json, HelpGiverEnteredResponse.class);
        Chatroom chatroom = chatrooms.get(payload.getChatroomUuid());
        chatroom.setHelpGiver(Connection.builder().webSocket(conn).build());
    }

    private void onHelperLeftChatroom(WebSocket conn, String json) {
        HelpGiverLeftResponse payload = new Gson().fromJson(json, HelpGiverLeftResponse.class);
        Chatroom chatroom = chatrooms.get(payload.getChatroomUuid());
        chatroom.setHelpGiver(null);
        helpGiverToChatroom.remove(conn.hashCode());
    }

    @SneakyThrows
    private void onMessage(WebSocket conn, String payload) {
        MessageRequest message = new Gson().fromJson(payload, MessageRequest.class);
        final Chatroom chatroom = chatrooms.get(message.getChatroomUuid());

        if (chatroom.getAssistantRequested()) {
            log.info("Send message to assistant");
            String assistantResponse = assistantService.sendMessage(message.getMessage());
            chatroom.getMessages().add(Message.builder().content(assistantResponse).from(Participant.ASSISTANT.toString()).build());
            MessageResponse assistantMessage = MessageResponse.builder().message(assistantResponse).chatroomUuid(chatroom.getUuid()).sender(Participant.ASSISTANT).build();
            conn.send(assistantMessage.toJson());
        }

        if (chatroom.getHelpGiver() != null && Objects.equals(chatroom.getHelpGiver().getWebSocket(), conn)) {
            chatroom.getHelpGiver().getWebSocket().send(message.toJson());
        }
    }

    private void onInviteAssistant(WebSocket conn, String json) {
        InviteAssistantRequest payload = new Gson().fromJson(json, InviteAssistantRequest.class);
        Chatroom chatroom = chatrooms.get(payload.getChatroomUuid());
        chatroom.setAssistantRequested(true);

        MessageResponse createMessage = MessageResponse.builder()
                .chatroomUuid(chatroom.getUuid())
                .sender(Participant.ASSISTANT)
                .message("Hi! I am your virtual assistant and I'm here to help you. How can I serve you today?")
                .build();

        conn.send(new Gson().toJson(createMessage));
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
        HelpSeekerLeftResponse helpSeekerLeft = HelpSeekerLeftResponse.builder().chatroomUuid(chatroom.getUuid()).build();
        String helpSeekerLeftJson = new Gson().toJson(helpSeekerLeft);
        users.forEach(entry -> entry.getWebSocket().send(helpSeekerLeftJson));
    }
}

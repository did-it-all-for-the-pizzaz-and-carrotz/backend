package tech.carrotly.restapi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import tech.carrotly.restapi.chat.dtos.*;
import tech.carrotly.restapi.chat.enums.Participant;
import tech.carrotly.restapi.chat.models.Connection;
import tech.carrotly.restapi.chat.payloads.HelpGiverLeft;
import tech.carrotly.restapi.model.entity.Chatroom;
import tech.carrotly.restapi.model.entity.Message;
import tech.carrotly.restapi.model.entity.Report;
import tech.carrotly.restapi.model.response.CreatedChatroomResponse;
import tech.carrotly.restapi.repository.ReportRepository;
import tech.carrotly.restapi.service.AssistantService;
import tech.carrotly.restapi.service.ChatService;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.Objects.nonNull;


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
    private final ReportRepository reportRepository;

    @Override
    public void process(String message, WebSocket conn) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(message);
        String topic = node.get("topic").toString().replaceAll("\"", "");
        String payload = node.get("payload").toString();

        log.debug("Received: " + topic + " | " + payload);

        switch (topic) {
            case "createChatroom" -> onChatroomCreate(conn, payload);
            case "helperEnteredChatroom" -> onHelperEnteredChatroom(conn, payload);
            case "helperLeftChatroom" -> onHelperLeftChatroom(conn, payload);
            case "MESSAGE" -> onMessage(conn, payload);
            case "helperReply" -> helperReply(conn, payload, message);
            case "requestAssistant" -> onInviteAssistant(conn, payload);
            case "helperLogin" -> onHelperLogin(conn);
            case "helperLogout" -> onHelperLogout(conn);
            case "reportSnapshot" -> reportSnapshot(conn);
        }
    }

    private void reportSnapshot(WebSocket conn) {
        final UUID chatUuid = helpSeekerToChatroom.get(conn.hashCode());
        Chatroom chatroom = chatrooms.get(chatUuid);

        List<Message> messages = chatroom.getMessages();
        reportRepository.save(Report.builder()
                .age(chatroom.getAgeOfHelpSeeker())
                .messages(messages)
                .localDateTime(LocalDateTime.now())
                .who("currently.com@gmail.com")
                .build());
    }

    private void helperReply(WebSocket conn, String payload, String message) {
        MessageRequest messageRequest = new Gson().fromJson(payload, MessageRequest.class);

        final var chatroom = chatrooms.get(messageRequest.getChatroomUuid());

        chatroom.getHelpSeeker().getWebSocket().send(message);

        chatroom.getMessages().add(Message.builder()
                .from("mail@gom")
                .content(messageRequest.getMessage())
                .build());
    }

    @SneakyThrows
    private void fetchAllFreeRooms(WebSocket conn) {
        List<Chatroom> rooms = chatrooms.entrySet().stream()
                .filter(e -> nonNull(e.getValue().getHelpSeeker()))
                .map(Map.Entry::getValue)
                .toList();

        JSONObject jsonObject = new JSONObject();
        JSONObject payload = new JSONObject();
        jsonObject.put("topic", "FETCH_ROOMS");
        jsonObject.put("payload", payload);

        JSONArray jsonArray = new JSONArray();
        payload.put("rooms", jsonArray);

        // TODO za czat bota wiadomosci wysylac
        rooms.forEach(room -> {
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put("chatroomUUID", room.getUuid());
                        jo.put("date", room.getLocalDateTime());
                        jo.put("assistantRequested", room.getAssistantRequested());
                        jo.put("age", room.getAgeOfHelpSeeker());

                        room.getMessages().stream()
                                .findFirst()
                                .map(mess -> {
                                    try {
                                        jo.put("title", mess);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return mess;
                                }).orElseGet(() -> {
                                    try {
                                        jo.put("title", "Czekam na pierwszą wiadomość...");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    jsonArray.put(jo);
                });

        log.info("Sending {} to dashboard user", jsonObject.toString());

        conn.send(jsonObject.toString());
    }

    @SneakyThrows
    public void onChatroomCreate(WebSocket conn, String payload) {
        final UUID chatroomUuid = UUID.randomUUID();
        CreateChatroomRequest req = new Gson().fromJson(payload, CreateChatroomRequest.class);

        Chatroom chatroom = Chatroom.builder()
                .uuid(chatroomUuid)
                .helpSeeker(Connection.builder().webSocket(conn).build())
                .assistantRequested(false)
                .localDateTime(LocalDateTime.now())
                .ageOfHelpSeeker(req.getAge())
                .build();

        chatrooms.put(chatroomUuid, chatroom);

        log.info("Chatroom {} has been created", chatroom);
        helpSeekerToChatroom.put(conn.hashCode(), chatroomUuid);

        CreatedChatroomResponse createdChatroomResponse = CreatedChatroomResponse.builder()
                .chatroomUUID(chatroomUuid)
                .build();

        String createdChatroomResponseJson = new Gson().toJson(createdChatroomResponse);

        // Build json object response
        JSONObject jsonObject = new JSONObject();
        JSONObject py = new JSONObject();
        jsonObject.put("topic", "GAIN_ACCESS");
        jsonObject.put("payload", createdChatroomResponseJson);

        log.info("Sending {} to chat room", jsonObject.toString());
        conn.send(jsonObject.toString());

        // TODO fix?
//        users.forEach(entry -> entry.getWebSocket().send(createdChatroomResponseJson));
    }

    private void onHelperLogout(WebSocket conn) {
        users.remove(Connection.builder().webSocket(conn).build());
        helpGiverToChatroom.remove(conn.hashCode());
        log.info("User {} has logged out", conn.getRemoteSocketAddress());
    }

    private void onHelperLogin(WebSocket conn) {
        users.add(Connection.builder().webSocket(conn).build());
        log.info("User {} has logged in", conn.getRemoteSocketAddress());
        fetchAllFreeRooms(conn);
    }

    @SneakyThrows
    private void onHelperEnteredChatroom(WebSocket conn, String json) {
        HelpGiverEnteredResponse payload = new Gson().fromJson(json, HelpGiverEnteredResponse.class);
        Chatroom chatroom = chatrooms.get(payload.getChatroomUuid());
        chatroom.setHelpGiver(Connection.builder().webSocket(conn).build());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("topic", "HELPER_ENTERED");
        jsonObject.put("payload", "{}");

        log.info("Sending {} to chat room", jsonObject.toString());
        chatroom.getHelpSeeker().getWebSocket().send(jsonObject.toString());
    }

    @SneakyThrows
    private void onHelperLeftChatroom(WebSocket conn, String json) {
        HelpGiverLeftResponse payload = new Gson().fromJson(json, HelpGiverLeftResponse.class);
        Chatroom chatroom = chatrooms.get(payload.getChatroomUuid());
        chatroom.setHelpGiver(null);
        helpGiverToChatroom.remove(conn.hashCode());


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("topic", "HELPER_LEFT");
        jsonObject.put("payload", "{}");

        log.info("Sending {} to chat room", jsonObject.toString());
        chatroom.getHelpSeeker().getWebSocket().send(jsonObject.toString());

        users.stream()
                .filter(u -> !u.getWebSocket().equals(conn))
                .forEach(user -> {
                    user.getWebSocket().send(HelpGiverLeft.builder()
                            .chatroomUUID(chatroom.getUuid())
                            .build()
                            .toJson());
                });
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
            chatroom.getMessages().add(Message.builder().from("seaker").content(message.getMessage()).build());
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
        if (helpGiverToChatroom.containsKey(conn.hashCode())) disconnectHelpGiver(helpGiverToChatroom.get(conn.hashCode()), conn);
        if (helpSeekerToChatroom.containsKey(conn.hashCode())) disconnectHelpSeeker(helpSeekerToChatroom.get(conn.hashCode()));
    }

    private void disconnectHelpGiver(UUID chatroomUuid, WebSocket conn) {
        Chatroom chatroom = chatrooms.get(chatroomUuid);
        chatroom.setHelpGiver(null);

        users.stream()
                .filter(u -> !u.getWebSocket().equals(conn))
                .forEach(user -> {
                    user.getWebSocket().send(HelpGiverLeft.builder()
                            .chatroomUUID(chatroomUuid)
                            .build()
                            .toJson());
                });
    }

    private void disconnectHelpSeeker(UUID chatroomUuid) {
        Chatroom chatroom = chatrooms.get(chatroomUuid);
        chatrooms.remove(chatroom.getUuid());
        HelpSeekerLeftResponse helpSeekerLeft = HelpSeekerLeftResponse.builder().chatroomUuid(chatroom.getUuid()).build();
        log.info("Removing room {}", chatroom);
        String helpSeekerLeftJson = new Gson().toJson(helpSeekerLeft);
        users.forEach(entry -> entry.getWebSocket().send(helpSeekerLeftJson));
    }
}

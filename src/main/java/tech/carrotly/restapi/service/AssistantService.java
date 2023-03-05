package tech.carrotly.restapi.service;

public interface AssistantService {
    String sendMessage(String conversationId, String message);

    Boolean verifyMessage(String message);
}

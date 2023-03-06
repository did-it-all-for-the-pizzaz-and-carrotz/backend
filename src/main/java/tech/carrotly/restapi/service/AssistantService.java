package tech.carrotly.restapi.service;

public interface AssistantService {
    String sendMessage(String message);

    Boolean verifyMessage(String message);
}

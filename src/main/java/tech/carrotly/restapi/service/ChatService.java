package tech.carrotly.restapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ChatService {
    void process(String message) throws JsonProcessingException;
}

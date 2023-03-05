package tech.carrotly.restapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.java_websocket.WebSocket;

public interface ChatService {
    void process(String message, WebSocket conn) throws JsonProcessingException;
}

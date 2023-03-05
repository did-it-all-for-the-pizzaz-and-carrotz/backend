package tech.carrotly.restapi.service.impl;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.carrotly.restapi.model.entity.Room;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl {

    private final Map<String, Room> roomMap;
    public final Set<SocketIOClient> clientSet = new HashSet<>();

    public void addClient(SocketIOClient socketIOClient) {

        log.info(socketIOClient.getRemoteAddress().toString());

        if(!clientSet.contains(socketIOClient)) {
            clientSet.add(socketIOClient);
        }

    }

    // onConnect -> createRoom
    // onDisconnect -> removeRoom
    // onSend ->
    // onNotify ->



}

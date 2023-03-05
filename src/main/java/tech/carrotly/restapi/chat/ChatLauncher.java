package tech.carrotly.restapi.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import tech.carrotly.restapi.chat.models.ChatObject;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

@Slf4j
@Component
public class ChatLauncher extends WebSocketServer {
    private final ObjectMapper objectMapper;

    public ChatLauncher(@Value("${socket.port}") Integer port,
                        ObjectMapper objectMapper) throws UnknownHostException {
        super(new InetSocketAddress(8081));
        this.objectMapper = objectMapper;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {

        System.out.println(
                conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println(conn + " has left the room!");
    }

    @SneakyThrows
    @Override
    public void onMessage(WebSocket conn, String message) {
        ChatObject chatObject = objectMapper.readValue(message, ChatObject.class);
        log.info(chatObject.toString());
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        broadcast(message.array());
        System.out.println(conn + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        log.info("Socket Server started...");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

}

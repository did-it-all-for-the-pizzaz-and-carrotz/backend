package tech.carrotly.restapi.chat;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import tech.carrotly.restapi.chat.models.ChatObject;
import tech.carrotly.restapi.service.impl.ChatServiceImpl;

@Slf4j
@Component
public class ChatLauncher {
    private final SocketIOServer server;
    private final ChatServiceImpl chatService;

    public ChatLauncher(@Qualifier("chatConfiguration") final Configuration configuration,
                        final ChatServiceImpl chatService) {
        this.server = new SocketIOServer(configuration);
        this.chatService = chatService;

        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                chatService.addClient(socketIOClient);
            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                log.info("Disconnected");
            }
        });

        server.addEventListener("notify", ChatObject.class, (socketIOClient, data, ackRequest) -> {
            server.getBroadcastOperations().sendEvent("notify", data);
        });

        server.addEventListener("send", ChatObject.class, (socketIOClient, data, ackRequest) -> {
            log.info("Sending message....");
            log.info(socketIOClient.toString());
        });
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosedEvent(ContextClosedEvent contextClosedEvent) throws InterruptedException {
        log.info("Socket.io - Shutdown initiated...");
        server.stop();
        Thread.sleep(2000);
        log.info("Socket.io - Shutdown completed.");
    }

    public void run() {
        log.info("Starting socket server...");
        server.start();
    }

    private void configureNamespaces() {

    }
}

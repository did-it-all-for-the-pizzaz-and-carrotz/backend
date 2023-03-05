package tech.carrotly.restapi;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tech.carrotly.restapi.chat.ChatLauncher;

@SpringBootApplication
@RequiredArgsConstructor
public class BackendApplication {

    private final ChatLauncher chatLauncher;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner runner() {
        return args -> {
            chatLauncher.run();
        };
    }


}

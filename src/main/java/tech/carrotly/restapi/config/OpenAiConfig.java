package tech.carrotly.restapi.config;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.carrotly.restapi.integrations.openai.OpenAiApi;

@Configuration
public class OpenAiConfig {
    @Value("${open-ai.secret-key}")
    private String secretKey;

    @Value("${open-ai.model}")
    private String model;

    @Value("${open-ai.temperature}")
    private Float temperature;

    @Value("${open-ai.max-tokens}")
    private Integer maxTokens;

    @Value("${open-ai.presence-penalty}")
    private Float presencePenalty;

    @Value("${open-ai.frequency-penalty}")
    private Float frequencyPenalty;

    @Value("${open-ai.flavor}")
    private String flavor;

    @Bean
    public OpenAiApi client() {
        return Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .requestInterceptor(template -> template.header("Authorization", "Bearer " + secretKey))
                .target(OpenAiApi.class, "https://api.openai.com");
    }
}

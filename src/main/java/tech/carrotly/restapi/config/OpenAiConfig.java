package tech.carrotly.restapi.config;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.carrotly.restapi.integrations.openai.OpenAiApi;
import tech.carrotly.restapi.integrations.openai.OpenAiClient;

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
    public OpenAiClient client() {
        OpenAiApi api = Feign.builder()
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(OpenAiApi.class, "https://api.openai.com");

        return new OpenAiClient(api, secretKey, model, temperature, maxTokens, presencePenalty, frequencyPenalty, flavor);
    }
}

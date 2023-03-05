package tech.carrotly.restapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.carrotly.restapi.integrations.openai.OpenAiApi;
import tech.carrotly.restapi.integrations.openai.OpenAiClient;
import tech.carrotly.restapi.integrations.openai.dtos.CreateChatCompletionMessageRequest;
import tech.carrotly.restapi.integrations.openai.dtos.CreateChatCompletionRequest;
import tech.carrotly.restapi.integrations.openai.dtos.CreateChatCompletionResponse;
import tech.carrotly.restapi.integrations.openai.enums.ChatParticipant;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SampleController {
    private final OpenAiClient openAiClient;
    private final Logger logger = LoggerFactory.getLogger(SampleController.class);

    @GetMapping
    public String test() {
        logger.info("#1");

        List<CreateChatCompletionMessageRequest> messages = new ArrayList<>();
        messages.add(CreateChatCompletionMessageRequest.builder().role(ChatParticipant.SYSTEM).content(flavor).build());
        messages.add(CreateChatCompletionMessageRequest.builder().role(ChatParticipant.USER).content(message).build());
        logger.info("#2");

        CreateChatCompletionResponse response = openAiClient.createChatCompletion(messages);
        logger.info("#3");
        logger.info(response.getChoices().get(0).getMessages().get(0).getContent());

        return "test";
    }
}


// @Service
// @RequiredArgsConstructor
// public class PostService {
//
//     private final PostRepository postRepository;
//     private final MediaRepository mediaRepository;
//     private final PostConverter postConverter;
package tech.carrotly.restapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.carrotly.restapi.service.AssistantService;

@RestController
@RequestMapping("/sample")
@RequiredArgsConstructor
@Slf4j
public class SampleController {
    private final AssistantService assistantService;
    private final Logger logger = LoggerFactory.getLogger(SampleController.class);

    @GetMapping("/test")
    public String test() {
        assistantService.verifyMessage("I just want to kill my self. How can I do this? (in real I am just testing moderation service)");
        return "OK";
    }
}

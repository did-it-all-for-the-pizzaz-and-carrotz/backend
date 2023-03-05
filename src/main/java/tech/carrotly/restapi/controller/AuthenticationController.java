package tech.carrotly.restapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.carrotly.restapi.model.request.AuthenticationRequest;
import tech.carrotly.restapi.model.request.CreateUserRequest;
import tech.carrotly.restapi.model.response.AuthenticationResponse;
import tech.carrotly.restapi.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService jwtAuthenticationService;

    @PostMapping("/register")
    public AuthenticationResponse register(@RequestBody CreateUserRequest request) throws Exception {
        return jwtAuthenticationService.register(request);
    }

    @PostMapping
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) throws Exception {
        return jwtAuthenticationService.authenticate(request);
    }
}

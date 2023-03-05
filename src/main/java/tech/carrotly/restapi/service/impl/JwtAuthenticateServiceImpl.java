package tech.carrotly.restapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.carrotly.restapi.model.request.AuthenticationRequest;
import tech.carrotly.restapi.model.request.CreateUserRequest;
import tech.carrotly.restapi.model.response.AuthenticationResponse;
import tech.carrotly.restapi.service.AuthenticationService;
import tech.carrotly.restapi.service.JwtService;
import tech.carrotly.restapi.service.UserService;

@Service
@RequiredArgsConstructor
public class JwtAuthenticateServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtServiceImpl;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(CreateUserRequest request) throws Exception {
        var user = userService.save(request);

        var jwtToken = jwtServiceImpl.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        final var user = userService.findUserByEmail(request.getEmail());
        final var jwtToken = jwtServiceImpl.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}

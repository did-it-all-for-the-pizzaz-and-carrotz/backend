package tech.carrotly.restapi.service;

import tech.carrotly.restapi.model.request.AuthenticationRequest;
import tech.carrotly.restapi.model.request.CreateUserRequest;
import tech.carrotly.restapi.model.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(CreateUserRequest request) throws Exception;
    AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception;
}

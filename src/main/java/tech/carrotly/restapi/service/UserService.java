package tech.carrotly.restapi.service;

import tech.carrotly.restapi.model.request.CreateUserRequest;
import tech.carrotly.restapi.model.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse findUserById(Long id) throws Exception;
    UserResponse findUserByEmail(String email) throws Exception;
    UserResponse save(CreateUserRequest createUserRequest) throws Exception;
}

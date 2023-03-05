package tech.carrotly.restapi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tech.carrotly.restapi.mapper.UserMapper;
import tech.carrotly.restapi.model.helpers.Error;
import tech.carrotly.restapi.model.request.CreateUserRequest;
import tech.carrotly.restapi.model.response.UserResponse;
import tech.carrotly.restapi.repository.UserRepository;
import tech.carrotly.restapi.service.SecurityService;
import tech.carrotly.restapi.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @Override
    public List<UserResponse> findAll() {
        return UserMapper.MAPPER.map(
                userRepository.findAll()
        );
    }

    @Override
    public UserResponse findUserById(Long id) throws Exception {
        return userRepository.findById(id)
                .map(UserMapper.MAPPER::map)
                .orElseThrow(Error.INVALID_USER_ID.throw_());
    }

    @Override
    public UserResponse findUserByEmail(String email) throws Exception {
        return userRepository.findByEmail(email)
                .map(UserMapper.MAPPER::map)
                .orElseThrow(Error.INVALID_USER_EMAIL.throw_());
    }

    @Override
    public UserResponse save(CreateUserRequest createUserRequest) throws Exception {

        log.info(createUserRequest.toString());
        log.info(UserMapper.MAPPER.map(createUserRequest).toString());

        Optional.of(userRepository.existsByEmail(createUserRequest.getEmail()))
                .filter(val -> val.equals(false))
                .orElseThrow(Error.INVALID_USER.throw_());

        return Optional.of(UserMapper.MAPPER.map(createUserRequest))
                .map(securityService::encodePassword)
                .map(userRepository::save)
                .map(UserMapper.MAPPER::map)
                .orElseThrow(Error.INVALID_USER.throw_());
    }
}

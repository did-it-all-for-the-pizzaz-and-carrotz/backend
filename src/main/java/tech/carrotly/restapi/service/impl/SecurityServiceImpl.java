package tech.carrotly.restapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.carrotly.restapi.model.entity.User;
import tech.carrotly.restapi.service.SecurityService;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final PasswordEncoder passwordEncoder;


    @Override
    public User encodePassword(User user) {
        return user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
    }

    @Override
    public Boolean comparePasswords(String passwordOne, String passwordTwo) {
        return Boolean.TRUE;
    }
}

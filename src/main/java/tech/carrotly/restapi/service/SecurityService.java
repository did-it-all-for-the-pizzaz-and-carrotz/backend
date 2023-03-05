package tech.carrotly.restapi.service;

import tech.carrotly.restapi.model.entity.User;

public interface SecurityService {

    User encodePassword(final User user );
    Boolean comparePasswords(final String passwordOne, final String passwordTwo);
}

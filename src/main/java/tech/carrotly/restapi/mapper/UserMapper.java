package tech.carrotly.restapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.carrotly.restapi.model.entity.User;
import tech.carrotly.restapi.model.request.CreateUserRequest;
import tech.carrotly.restapi.model.response.UserResponse;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    List<UserResponse> map(List<User> users);
    UserResponse map(User user);
    User map(CreateUserRequest createUserRequest);
}

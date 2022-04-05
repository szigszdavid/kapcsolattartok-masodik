package hu.futureofmedia.task.contactsapi.mapper;

import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.entities.User;
import org.mapstruct.Mapper;

import java.net.CacheRequest;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User createUserRequestToUser(CreateUserRequest request);
}

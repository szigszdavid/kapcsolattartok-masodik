package hu.futureofmedia.task.contactsapi.mapper;

import hu.futureofmedia.task.contactsapi.domain.User;
import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User createUserRequestToUser(CreateUserRequest request);
}

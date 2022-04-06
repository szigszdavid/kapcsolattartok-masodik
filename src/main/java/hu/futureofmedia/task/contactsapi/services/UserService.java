package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.User;
import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.exceptions.UserNotFoundExcpetion;

import java.util.List;

public interface UserService {

    Long addUser(CreateUserRequest request);

    User findUser(String username) throws UserNotFoundExcpetion;

    List<User> findAllUser();

    void setRoleForUser(String username, String roleName) throws UserNotFoundExcpetion;
}

package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.User;
import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.dtos.GetUserByIdRequestDto;
import hu.futureofmedia.task.contactsapi.dtos.GetUserByUsernameDto;
import hu.futureofmedia.task.contactsapi.exceptions.UserNotFoundExcpetion;

import java.util.List;

public interface UserService {

    Long addUser(CreateUserRequest request);

    GetUserByUsernameDto findUserByUsername(String username) throws UserNotFoundExcpetion;

    User findUser(String username) throws UserNotFoundExcpetion;

    List<User> findAllUser();

    GetUserByIdRequestDto findUserById(Long id) throws UserNotFoundExcpetion;
}

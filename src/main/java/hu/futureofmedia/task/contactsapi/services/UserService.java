package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.entities.User;

public interface UserService {

    Long addUser(CreateUserRequest request);
}

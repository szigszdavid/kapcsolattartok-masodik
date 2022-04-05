package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.entities.User;
import hu.futureofmedia.task.contactsapi.mapper.UserMapper;
import hu.futureofmedia.task.contactsapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    @Transactional
    public Long addUser(CreateUserRequest request) {

        User user = mapper.createUserRequestToUser(request);

        userRepository.save(user);

        return user.getId();
    }
}

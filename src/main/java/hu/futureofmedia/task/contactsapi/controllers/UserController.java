package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.domain.User;
import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<User> findAllUser()
    {
        return userService.findAllUser();
    }

    @PostMapping
    public Long addUser(@RequestBody CreateUserRequest request)
    {
        log.info("addUser with CreateUserRequest: {} called", request);

        return userService.addUser(request);
    }
}

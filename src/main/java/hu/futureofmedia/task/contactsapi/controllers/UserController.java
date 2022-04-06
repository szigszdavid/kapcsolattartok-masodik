package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.domain.User;
import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return userService.addUser(request);
    }
}

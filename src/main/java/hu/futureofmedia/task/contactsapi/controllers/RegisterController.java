package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts/register")
public class RegisterController {

    private final UserService userService;

    @PostMapping("register")
    public Long register(@RequestBody @Valid CreateUserRequest request) {
        return userService.addUser(request);
    }

}

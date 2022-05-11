package hu.futureofmedia.task.contactsapi.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.futureofmedia.task.contactsapi.domain.Privilege;
import hu.futureofmedia.task.contactsapi.domain.Role;
import hu.futureofmedia.task.contactsapi.domain.User;
import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.dtos.GetUserByIdRequestDto;
import hu.futureofmedia.task.contactsapi.dtos.GetUserByUsernameDto;
import hu.futureofmedia.task.contactsapi.exceptions.UserNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @GetMapping("/{id}")
    public GetUserByIdRequestDto findUserById(@PathVariable Long id) throws UserNotFoundExcpetion { return userService.findUserById(id);}

    @GetMapping("/name/{username}")
    public GetUserByUsernameDto findUserByUsername(@PathVariable String username) throws UserNotFoundExcpetion {return userService.findUserByUsername(username);}

    @PostMapping
    public Long addUser(@RequestBody @Valid CreateUserRequest request)
    {
        log.info("addUser with CreateUserRequest: {} called", request);

        return userService.addUser(request);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws UserNotFoundExcpetion, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null & authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.findUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 10000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getPrivileges().stream().map(Privilege::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception)
            {
                log.error("Error in: {}", exception.getMessage());
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        else
        {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}

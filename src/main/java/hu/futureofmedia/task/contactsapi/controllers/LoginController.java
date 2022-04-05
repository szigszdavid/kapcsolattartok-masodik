package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.entities.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hu.futureofmedia.task.contactsapi.dtos.AuthRequest;

import javax.validation.Valid;
import java.time.Instant;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
/*
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;


    @PostMapping("login")
    public void login(@RequestBody @Valid AuthRequest request) {

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            User user = (User) authentication.getPrincipal();

            Instant now = Instant.now();
            long expiry = 36000L;

            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(joining(" "));

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("example.io")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(format("%s,%s", user.getId(), user.getUsername()))
                    .claim("roles", scope)
                    .build();

            String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }

}
*/
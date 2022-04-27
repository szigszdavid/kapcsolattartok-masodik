package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.Privilege;
import hu.futureofmedia.task.contactsapi.domain.Role;
import hu.futureofmedia.task.contactsapi.domain.User;
import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.dtos.GetUserByIdRequestDto;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.exceptions.UserNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.mapper.UserMapper;
import hu.futureofmedia.task.contactsapi.repositories.RoleRepository;
import hu.futureofmedia.task.contactsapi.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final PrivilegeService privilegeService;

    @Override
    @Transactional
    public Long addUser(CreateUserRequest request) {

        log.info("addUser called with: {} !", request);

        User user = mapper.createUserRequestToUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Iterator<Long> privilegeIterator = request.getPrivilegesId().iterator();

        while (privilegeIterator.hasNext())
        {
            Privilege privilege = privilegeService.findPrivilegeById(privilegeIterator.next()).get();
            user.getPrivileges().add(privilege);
        }

        log.debug("User data before save: {}", user);

        userRepository.save(user);

        log.debug("User saved to database!");

        return user.getId();

    }

    @Override
    public User findUser(String username) throws UserNotFoundExcpetion {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundExcpetion("User not found"));
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public GetUserByIdRequestDto findUserById(Long id) throws UserNotFoundExcpetion {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundExcpetion("User not found!"));

        GetUserByIdRequestDto dto = mapper.userToGetUserByIdRequestDro(user);

        return dto;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getPrivileges().forEach(privilege -> authorities.add(new SimpleGrantedAuthority(privilege.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}

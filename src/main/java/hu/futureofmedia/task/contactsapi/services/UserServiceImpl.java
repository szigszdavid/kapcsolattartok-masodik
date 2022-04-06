package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.Role;
import hu.futureofmedia.task.contactsapi.domain.User;
import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.exceptions.UserNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.mapper.UserMapper;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Override
    @Transactional
    public Long addUser(CreateUserRequest request) {

        User user = mapper.createUserRequestToUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

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
    public void setRoleForUser(String username, String roleName) throws UserNotFoundExcpetion {
        User user = findUser(username);

        user.getAuthorities().add(roleService.findRoleByRoleName(roleName).get());
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        Collection<SimpleGrantedAuthority> vala = new ArrayList<>();
        Collection<Role> roles = user.getAuthorities();
        user.getAuthorities().forEach(role -> vala.add(new SimpleGrantedAuthority(role.getRoleName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), vala);
    }
}

package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.Role;

import java.util.Optional;
import java.util.List;

public interface RoleService {

    Optional<Role> findRoleById(Long id);

    Optional<Role> findRoleByName(String roleName);

    List<Role> findRolesByName(String name);

    List<Role> findAll();
}

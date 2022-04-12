package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.Role;
import hu.futureofmedia.task.contactsapi.dtos.RoleCreateRequest;

import java.util.Map;
import java.util.Optional;
import java.util.List;

public interface RoleService {

    Long addRole(RoleCreateRequest request);

    Optional<Role> findRoleById(Long id);

    Optional<Role> findRoleByName(String roleName);

    List<Role> findRolesByName(String name);

    List<Role> findAll();
}

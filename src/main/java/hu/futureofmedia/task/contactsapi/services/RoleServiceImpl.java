package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.Role;
import hu.futureofmedia.task.contactsapi.dtos.RoleCreateRequest;
import hu.futureofmedia.task.contactsapi.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final PrivilegeService privilegeService;

    @Override
    @Transactional
    public Long addRole(RoleCreateRequest request) {
        return null;
    }

    @Override
    public Optional<Role> findRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Optional<Role> findRoleByName(String roleName) {

        return roleRepository.findByName(roleName);
    }

    @Override
    public List<Role> findRolesByName(String name) {
        return roleRepository.findAllByName(name);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }


}

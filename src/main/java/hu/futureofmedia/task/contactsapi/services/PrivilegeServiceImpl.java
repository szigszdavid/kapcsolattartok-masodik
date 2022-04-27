package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.Privilege;
import hu.futureofmedia.task.contactsapi.repositories.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService{

    private final PrivilegeRepository privilegeRepository;

    @Override
    public Optional<Privilege> findPrivilegeById(Long id) {
        return privilegeRepository.findById(id);
    }
}

package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.Privilege;
import hu.futureofmedia.task.contactsapi.repositories.PrivilegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService{

    private final PrivilegeRepository privilegeRepository;

    @Override
    public List<Privilege> findPrivilegesById(List<Long> idList) {
        return privilegeRepository.findAllById(idList);
    }
}

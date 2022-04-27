package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.domain.Privilege;
import java.util.List;
import java.util.Optional;

public interface PrivilegeService {

    Optional<Privilege> findPrivilegeById(Long id);
}

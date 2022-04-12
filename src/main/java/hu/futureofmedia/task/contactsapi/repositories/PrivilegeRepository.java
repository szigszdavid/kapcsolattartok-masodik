package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.domain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);

}

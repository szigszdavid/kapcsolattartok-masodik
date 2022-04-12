package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    List<Role> findAllByName(String name);
}

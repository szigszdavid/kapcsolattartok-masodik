package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Page<Contact> findByStatus(Status status, Pageable pageable);

    Page<Contact> findContactsByFirstNameContainingAndStatusOrLastNameContainingAndStatus(String firstName,Status statusFirst, String lastName, Status statusLast, Pageable pageable);

    Page<Contact> findContactsByFirstNameContainingAndStatusAndLastNameContainingAndStatus(String firstName,Status statusFirst, String lastName, Status statusLast, Pageable pageable);

    Page<Contact> findContactsByCompanyIdAndStatus(Long companyId,Status status, Pageable pageable);

}

package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    Page<Contact> findByStatus(Status status, Pageable pageable);


    @Query("select e from Contact e " +
            "where e.status = :status " +
            "and ((e.firstName like %:name1% and e.lastName like %:name2%) " +
            "or (e.firstName like %:name2% and e.lastName like %:name1%))")
    Page<Contact> findContactsByStatusAndFirstNameContainingOrLastNameContaining(Status status, @Param("name1") String firstName,@Param("name2") String lastName, Pageable pageable);

    Page<Contact> findContactsByFirstNameContainingAndStatusOrLastNameContainingAndStatus(String firstName,Status statusFirst, String lastName, Status statusLast, Pageable pageable);

    Page<Contact> findContactsByFirstNameContainingAndStatusAndLastNameContainingAndStatus(String firstName,Status statusFirst, String lastName, Status statusLast, Pageable pageable);

    Page<Contact> findContactsByCompanyIdAndStatus(Long companyId,Status status, Pageable pageable);

    @Query("select e from Contact e where e.firstName in :parts or e.lastName in :parts")
    Page<Contact> findAllByName(List<String> parts, Pageable newPageable);
}

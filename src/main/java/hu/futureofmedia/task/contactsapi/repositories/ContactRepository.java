package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Override
    @Query(value = "select concat(c.firstName, ' ', c.lastName) as fullName,c.company.name, c.emailAddress, c.phoneNumber, c.id from Contact c where c.status = 'ACTIVE' ")
    Page<Contact> findAll(Pageable p);


}

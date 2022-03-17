package hu.futureofmedia.task.contactsapi.repositories;

import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    /*
    @Override
    @Query("select concat(c.firstName, ' ', c.lastName) as fullName,c.company.name, c.emailAddress, c.phoneNumber from Contact c")
    Page<Contact> findAll(Pageable p);
    */

}

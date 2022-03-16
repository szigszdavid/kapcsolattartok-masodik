package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;

@Validated
public interface IContactService {

    Page<Contact> findAllContacts(Integer page);

    void addContact(@Valid Contact contact);

    void updateContact(Contact contact);

    Optional<Contact> findContactByID(Long id);
}

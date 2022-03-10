package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface IContactService {

    Page<Contact> findAllContacts(Integer page);

    void addContact(@Valid Contact contact);
}

package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.springframework.data.domain.Page;

public interface IContactService {

    Page<Contact> findAllBooks(Integer page);

}

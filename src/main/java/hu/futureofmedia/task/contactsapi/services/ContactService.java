package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.OutputDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import java.util.Optional;

public interface ContactService { //Átnevezni simán ContactService

    List<OutputDTO> findAllContacts(Integer page);

    void addContact( ContactDTO contactDTO);

    void updateContact( ContactDTO contactDTO, Long id);

    ContactDTO findContactByID(Long id);

    void deleteContact(Long id);

    Pageable createNewPageable(Integer page);
}

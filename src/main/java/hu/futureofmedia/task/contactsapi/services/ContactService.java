package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ContactService {

    List<GetAllContactsDTO> findAllContacts(Integer page);

    ContactDTO addContact( ContactDTO contactDTO);

    ContactDTO updateContact(ContactDTO contactDTO, Long id) throws ContactNotFoundExcpetion;

    GetContactByIdDTO findContactByID(Long id) throws ContactNotFoundExcpetion;

    Contact findById(Long id) throws ContactNotFoundExcpetion;

    void deleteContact(Long id) throws ContactNotFoundExcpetion;

    Pageable createNewPageable(Integer page);
}

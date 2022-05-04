package hu.futureofmedia.task.contactsapi.services;

import graphql.GraphQL;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.concurrent.Executor;

public interface ContactService {

    List<GetAllContactsDTO> findAllContacts(Integer page);

    Long addContact(ContactDTO contactDTO);

    Long updateContact(ContactDTO contactDTO, Long id) throws ContactNotFoundExcpetion;

    GetContactByIdDTO findContactByID(Long id) throws ContactNotFoundExcpetion;

    Contact findById(Long id) throws ContactNotFoundExcpetion;

    void deleteContact(Long id) throws ContactNotFoundExcpetion;

    Pageable createNewPageable(Integer page);

}

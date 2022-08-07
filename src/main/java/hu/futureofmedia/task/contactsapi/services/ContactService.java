package hu.futureofmedia.task.contactsapi.services;

import graphql.GraphQL;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsWithNumberOfContactsDto;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.concurrent.Executor;

public interface ContactService {

    GetAllContactsWithNumberOfContactsDto findAllContacts(Integer page, String orderBy, String orderWay, Integer size);

    Long addContact(ContactDTO contactDTO);

    Long updateContact(ContactDTO contactDTO, Long id) throws ContactNotFoundExcpetion;

    GetContactByIdDTO findContactByID(Long id) throws ContactNotFoundExcpetion;

    Contact findById(Long id) throws ContactNotFoundExcpetion;

    void deleteContact(Long id) throws ContactNotFoundExcpetion;

    Pageable createNewPageable(Integer page);

    GetAllContactsWithNumberOfContactsDto findContactsByName(Integer page, String name);

    GetAllContactsWithNumberOfContactsDto findContactsByCompany(Integer page,Long company);

}

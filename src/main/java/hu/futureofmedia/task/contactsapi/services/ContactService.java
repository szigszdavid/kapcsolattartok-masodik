package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import java.util.Optional;

public interface ContactService { //Átnevezni simán ContactService

    List<GetAllContactsDTO> findAllContacts(Integer page);

    ContactDTO addContact( ContactDTO contactDTO);

    void updateContact( ContactDTO contactDTO, Long id) throws ContactNotFoundExcpetion;

    GetContactByIdDTO findContactByID(Long id) throws ContactNotFoundExcpetion;

    Contact findById(Long id) throws ContactNotFoundExcpetion;

    void deleteContact(Long id) throws ContactNotFoundExcpetion;

    Pageable createNewPageable(Integer page);
}

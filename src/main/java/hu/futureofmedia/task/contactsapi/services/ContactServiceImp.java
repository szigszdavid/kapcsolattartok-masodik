package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.PrePersist;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ContactServiceImp implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper mapper;

    @Override
    public List<GetAllContactsDTO> findAllContacts(Integer page)
    {
        return contactRepository.findByStatus(Status.ACTIVE, createNewPageable(page)).map(mapper::contactToGetAllContactsDTO).toList();
    }

    @Override
    @PrePersist
    public void addContact(ContactDTO contactDTO) {

        Contact contact = mapper.contactDTOToContact(contactDTO);

        contactRepository.save(contact);
    }

    @Override
    public void updateContact(ContactDTO contactDTO, Long id) throws ContactNotFoundExcpetion {
        // findById
        // merge contact, dto
        // save contact
        // return map(contact)
        contactDTO.setId(id);
        Contact contact = findById(id);
        mapper.updateContactWithMapper(contactDTO, contact);

        contactRepository.save(contact);
    }

    @Override
    public void deleteContact(Long id) throws ContactNotFoundExcpetion
    {
        Contact contact = findById(id);

        contact.setStatus(Status.DELETED);

        contactRepository.save(contact);
    }

    @Override
    public GetContactByIdDTO findContactByID(Long id) throws ContactNotFoundExcpetion {

        Contact contact = findById(id);

        return mapper.contactToGetContactByDTO(contact);
    }

    @Override
    public Pageable createNewPageable(Integer page)
    {
        return PageRequest.of(page == null ? 0 : page,10, Sort.by("firstName").and(Sort.by("lastName")));
    }

    @Override
    public Contact findById(Long id) throws ContactNotFoundExcpetion
    {
        return contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundExcpetion("Contact not found"));
    }
}
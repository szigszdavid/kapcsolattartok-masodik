package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.OutputDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ContactServiceImp implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper mapper;

    @Override
    public List<OutputDTO> findAllContacts(Integer page)
    {
        return contactRepository.findByStatus(Status.ACTIVE, createNewPageable(page)).map(mapper::contactToOutputDTO).toList();
    }

    @Override
    public void addContact(ContactDTO contactDTO) {

        Contact contact = mapper.contactDTOToContact(contactDTO);

        contactRepository.save(contact);
    }

    @Override
    public void updateContact(ContactDTO contactDTO, Long id) {

        contactDTO.setId(id);
        ContactDTO findByIdDTO = findContactByID(id);
        findByIdDTO = mapper.contactDTOToContactDTO(contactDTO);
        Contact contact = mapper.contactDTOToContact(findByIdDTO);

        mapper.updateContactWithMapper(findByIdDTO, contact);
        contactRepository.save(contact);
    }

    public void deleteContact(Long id)
    {
        ContactDTO contactDTO = findContactByID(id);

        contactDTO.setStatus("DELETED");

        contactRepository.save(mapper.contactDTOToContact(contactDTO));
    }

    @Override
    public ContactDTO findContactByID(Long id) {

        Contact contact = contactRepository.findById(id).orElse(null);

        return mapper.contactToContactDTO(contact);
    }

    @Override
    public Pageable createNewPageable(Integer page)
    {
        return PageRequest.of(page == null ? 0 : page,10, Sort.by("firstName").and(Sort.by("lastName")));
    }
}

package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ContactServiceImp implements ContactService {

    private final ContactRepository contactRepository;
    private final CompanyService companyService;
    private final ContactMapper mapper;

    @Value("${number.of.contacts.by.page:10}")
    private Integer numberOfContactsByPage;

    @Override
    public List<GetAllContactsDTO> findAllContacts(Integer page)
    {
        return contactRepository.findByStatus(Status.ACTIVE, createNewPageable(page)).map(mapper::contactToGetAllContactsDTO).toList();
    }

    @Override
    public Long addContact(ContactDTO contactDTO) {

        Contact contact = mapper.contactDTOToContact(contactDTO);

        contact.setStatus(Status.ACTIVE);

        setCompany(contact, contactDTO);

        contactRepository.save(contact);

        return contact.getId();
    }

    @Override
    public Long updateContact(ContactDTO contactDTO, Long id) throws ContactNotFoundExcpetion {

        contactDTO.setId(id);
        Contact contact = findById(id);
        mapper.updateContactWithMapper(contactDTO, contact);

        setCompany(contact, contactDTO);

        contactRepository.save(contact);

        return contact.getId();
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
        return PageRequest.of(page == null ? 0 : page,numberOfContactsByPage, Sort.by("firstName").and(Sort.by("lastName")));
    }

    @Override
    public Contact findById(Long id) throws ContactNotFoundExcpetion
    {
        return contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundExcpetion("Contact not found"));
    }


    private void setCompany(Contact contact, ContactDTO contactDTO) {

        Company company = companyService.findById(contactDTO.getCompanyId());

        contact.setCompany(company);
    }
}

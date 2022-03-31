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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.KPropertyPathExtensionsKt;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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
        log.info("findAllContacts in ContactServiceImp called on page: " + page);
        return contactRepository.findByStatus(Status.ACTIVE, createNewPageable(page)).map(mapper::contactToGetAllContactsDTO).toList();
    }

    @Override
    public Long addContact(ContactDTO contactDTO) {

        log.info("addContact in ContactServiceImp called!");

        Contact contact = mapper.contactDTOToContact(contactDTO);

        log.debug("Contact created from ContactDTO!");

        contact.setStatus(Status.ACTIVE);

        log.debug("Contact status changed to ACTIVE!");

        setCompany(contact, contactDTO);

        contactRepository.save(contact);

        log.debug("Contact saved to database!");

        return contact.getId();
    }

    @Override
    public Long updateContact(ContactDTO contactDTO, Long id) throws ContactNotFoundExcpetion {

        log.info("addContact in ContactServiceImp called!");

        contactDTO.setId(id);

        log.debug("ContactDTO id is changed to: " + id + " !");

        Contact contact = findById(id);

        log.debug("Contact with id: " + id + " found!");

        mapper.updateContactWithMapper(contactDTO, contact);

        log.debug("Contact's data is updated with data from ContactDTO!");

        setCompany(contact, contactDTO);

        contactRepository.save(contact);

        log.debug("Contact saved to database!");

        return contact.getId();
    }

    @Override
    public void deleteContact(Long id) throws ContactNotFoundExcpetion
    {
        log.info("deleteContact in ContactServiceImp called!");

        Contact contact = findById(id);

        log.debug("Contact with id:" + id + " found!");

        contact.setStatus(Status.DELETED);

        log.debug("Contact status changed to DELETED!");

        contactRepository.save(contact);

        log.debug("Contact saved to database!");
    }

    @Override
    public GetContactByIdDTO findContactByID(Long id) throws ContactNotFoundExcpetion {

        log.info("findContactByID in ContactServiceImp with id: " + id + " called!");

        Contact contact = findById(id);

        log.debug("Contact with id:" + id + " found!");

        GetContactByIdDTO getContactByIdDTO = mapper.contactToGetContactByDTO(contact);

        log.debug("Contact mapped to GetContactByIdDTO!");

        return getContactByIdDTO;
    }

    @Override
    public Pageable createNewPageable(Integer page)
    {
        log.info("createNewPageable in ContactServiceImp called");

        Pageable pageable = PageRequest.of(page == null ? 0 : page,numberOfContactsByPage, Sort.by("firstName").and(Sort.by("lastName")));

        log.debug("Pageable created on page: " + page + " with " + pageable.getPageSize() + " Contact/page");

        return pageable;
    }

    @Override
    public Contact findById(Long id) throws ContactNotFoundExcpetion
    {
        log.info("findById in ContactServiceImp called");

        return contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundExcpetion("Contact not found"));
    }


    private void setCompany(Contact contact, ContactDTO contactDTO) {

        log.info("Finding Company with id: " + contactDTO.getCompanyId() +"to Contact!");

        Company company = companyService.findById(contactDTO.getCompanyId());

        contact.setCompany(company);

        log.debug("Company setted for Contact!");
    }

}

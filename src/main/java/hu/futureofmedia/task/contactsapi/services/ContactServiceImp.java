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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(isolation = Isolation.SERIALIZABLE, readOnly = true, propagation = Propagation.REQUIRED)
public class ContactServiceImp implements ContactService {

    private final ContactRepository contactRepository;
    private final CompanyService companyService;
    private final ContactMapper mapper;

    @Value("${number.of.contacts.by.page}")
    private Integer numberOfContactsByPage;

    @Override
    public List<GetAllContactsDTO> findAllContacts(Integer page)
    {
        log.info("findAllContacts called on page: {}", page);
        return contactRepository.findByStatus(Status.ACTIVE, createNewPageable(page)).map(mapper::contactToGetAllContactsDTO).toList();
    }

    @Transactional
    @Override
    public Long addContact(ContactDTO contactDTO) {

        log.info("addContact called with: {} !", contactDTO);

        Contact contact = mapper.contactDTOToContact(contactDTO);

        contact.setStatus(Status.ACTIVE);

        setCompany(contact, contactDTO);

        log.debug("Contact data before save: {}", contact);

        contactRepository.save(contact);

        log.debug("Contact saved to database!");

        return contact.getId();
    }

    @Transactional
    @Override
    public Long updateContact(ContactDTO contactDTO, Long id) throws ContactNotFoundExcpetion {

        log.info("updateContact called with id: {}, and contactDTO: {} ", id, contactDTO);

        contactDTO.setId(id);


        Contact contact = findById(id);

        mapper.updateContactWithMapper(contactDTO, contact);

        setCompany(contact, contactDTO);

        log.debug("Contact before save: {}", contact);

        contactRepository.save(contact);

        log.debug("Contact saved to database!");

        return contact.getId();
    }

    @Transactional
    @Override
    public void deleteContact(Long id) throws ContactNotFoundExcpetion
    {
        log.info("deleteContact in ContactServiceImp called!");

        Contact contact = findById(id);

        contact.setStatus(Status.DELETED);

        log.debug("Contact before save: {}", contact);

        contactRepository.save(contact);

        log.debug("Contact saved to database!");
    }

    @Override
    public GetContactByIdDTO findContactByID(Long id) throws ContactNotFoundExcpetion {

        log.info("findContactByID with id: {} called!", id);

        Contact contact = findById(id);

        GetContactByIdDTO getContactByIdDTO = mapper.contactToGetContactByDTO(contact);

        log.debug("GetContactByIdDTO created!");

        return getContactByIdDTO;
    }

    @Transactional(propagation = Propagation.NEVER)
    @Override
    public Pageable createNewPageable(Integer page)
    {
        log.info("createNewPageable called with page: {}", page);

        Pageable pageable = PageRequest.of(page == null ? 0 : page,numberOfContactsByPage, Sort.by("firstName").and(Sort.by("lastName")));

        log.debug("Pageable created on page: {} with {} Contact/page", page, pageable.getPageSize());

        return pageable;
    }

    @Override
    public Contact findById(Long id) throws ContactNotFoundExcpetion
    {
        log.info("findById called with id: {}", id);

        return contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundExcpetion("Contact not found"));
    }


    private void setCompany(Contact contact, ContactDTO contactDTO) {

        Company company = companyService.findById(contactDTO.getCompanyId());

        contact.setCompany(company);

    }

}

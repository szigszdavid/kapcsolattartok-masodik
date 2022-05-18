package hu.futureofmedia.task.contactsapi.services;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;

import hu.futureofmedia.task.contactsapi.services.datafetchers.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import javax.jms.Queue;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ContactServiceImp implements ContactService {

    @Autowired
    private Queue queue;

    @Autowired
    private JmsTemplate jmsTemplate;


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

        jmsTemplate.convertAndSend(queue,contactDTO.getEmailAddress() + "," + contactDTO.getFirstName() + "," + contactDTO.getLastName() + "," + contact.getCompany().getName());

        log.info("ContactDTO sent to MessageQueue");

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

        log.debug("Contact before delete: {}", contact);

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

    @Transactional
    @Override
    public Pageable createNewPageable(Integer page)
    {
        log.info("createNewPageable called with page: {}", page);

        Pageable pageable = PageRequest.of(page == null ? 0 : page,numberOfContactsByPage, Sort.by("firstName").and(Sort.by("lastName")));

        log.debug("Pageable created on page: {} with {} Contact/page", page, pageable.getPageSize());

        return pageable;
    }

    @Override
    public List<GetAllContactsDTO> findContactsByName(Integer page, String name) {

        List<GetAllContactsDTO>  allCorrectNames = new ArrayList<>();

        if (name.split(" ").length >= 2)
        {

            String[] nameAfterSplit = name.split(" ");
            log.error("{} és {}",nameAfterSplit[0], nameAfterSplit[1]);
            List<GetAllContactsDTO> firstSublist = contactRepository.findContactsByFirstNameContainingAndStatusAndLastNameContainingAndStatus(nameAfterSplit[0], Status.ACTIVE, nameAfterSplit[1], Status.ACTIVE,createNewPageable(page)).map(mapper::contactToGetAllContactsDTO).toList();
            List<GetAllContactsDTO> secondSublist = contactRepository.findContactsByFirstNameContainingAndStatusAndLastNameContainingAndStatus(nameAfterSplit[1], Status.ACTIVE, nameAfterSplit[0], Status.ACTIVE,createNewPageable(page)).map(mapper::contactToGetAllContactsDTO).toList();

            if (!firstSublist.isEmpty())
            {
                allCorrectNames.addAll(firstSublist);
            }
            if(!secondSublist.isEmpty())
            {
                allCorrectNames.addAll(secondSublist);
            }
        }
        else if (name.split(" ").length == 1)
        {

            String[] nameAfterSplit = name.split(" ");

            List<GetAllContactsDTO> subList = contactRepository.findContactsByFirstNameContainingAndStatusOrLastNameContainingAndStatus(nameAfterSplit[0], Status.ACTIVE, nameAfterSplit[0], Status.ACTIVE,createNewPageable(page)).map(mapper::contactToGetAllContactsDTO).toList();

            if (!subList.isEmpty())
            {
                allCorrectNames.addAll(subList);
            }
        }
        else
        {
            List<GetAllContactsDTO> subList = contactRepository.findContactsByFirstNameContainingAndStatusOrLastNameContainingAndStatus(name, Status.ACTIVE, name, Status.ACTIVE,createNewPageable(page)).map(mapper::contactToGetAllContactsDTO).toList();

            if (!subList.isEmpty())
            {
                allCorrectNames.addAll(subList);
            }
        }

        return allCorrectNames;
    }

    @Override
    public List<GetAllContactsDTO> findContactsByCompany(Integer page,Long companyId)
    {
        if (companyId == 0)
        {
            return findAllContacts(page);
        }

        log.error("type of companyID : {}", companyId.getClass());

        return contactRepository.findContactsByCompanyIdAndStatus(companyId, Status.ACTIVE,createNewPageable(page)).map(mapper::contactToGetAllContactsDTO).toList();
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

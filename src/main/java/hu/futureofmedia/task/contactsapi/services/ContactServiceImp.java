package hu.futureofmedia.task.contactsapi.services;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsWithNumberOfContactsDto;
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
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
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
import java.util.Arrays;
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
    public GetAllContactsWithNumberOfContactsDto findAllContacts(Integer page, String orderBy, String orderWay, Integer size)
    {
        log.info("findAllContacts called on page: {}", page);
        if (size != null) {
            numberOfContactsByPage = size;
            log.error("size: {}", size);
        }
        List<GetAllContactsDTO> list  = contactRepository.findByStatus(Status.ACTIVE, createNewPageableWith(page, orderBy, orderWay)).map(mapper::contactToGetAllContactsDTO).toList();
        log.info("findAllContacts found {} page of Contacts", list.size());

        return createGetAllContactsWithNumber(list, page);
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

    private Pageable createNewPageableWith(Integer page, String orderBy, String orderWay)
    {
        log.info("createNewPageableWith called, sortBy : {}, sortWay : {}",orderBy, orderWay);
        if(orderBy != null)
        {
            if(orderBy.equals("emailaddress"))
            {
                orderBy = "emailAddress";
            }
            else if(orderBy.equals("company")) {}
            else {
                orderBy = null;
            }
        }

        if( orderBy == null && orderWay == null)
        {
            log.info("createNewPageable called with page: {}", page);

            return PageRequest.of(page == null ? 0 : page,numberOfContactsByPage, Sort.by("firstName").and(Sort.by("lastName")));
        }
        else if( orderBy == null && orderWay.equals("desc"))
        {
            log.info("createNewPageable called with page: {}", page);

            return PageRequest.of(page == null ? 0 : page,numberOfContactsByPage, Sort.by("firstName").descending().and(Sort.by("lastName")).descending());
        }
        else if ( orderBy != null && orderWay == null)
        {
            log.info("createNewPageable called with page: {}, orderBy: {}", page, orderBy);
            return PageRequest.of(page == null ? 0 : page,numberOfContactsByPage, Sort.by(orderBy));
        }
        else if( orderBy != null && orderWay.equals("desc"))
        {
            log.info("createNewPageable called with page: {}, desc");
            return PageRequest.of(page == null ? 0 : page,numberOfContactsByPage, Sort.by(orderBy).descending());
        }
        log.info("No match");

        return null;
    }

    @Override
    public GetAllContactsWithNumberOfContactsDto findContactsByName(Integer page, String name) {

        List<GetAllContactsDTO>  allCorrectNames = new ArrayList<>();

//        var parts = Arrays.asList(name.split(" "));
//        var p = contactRepository.findAllByName(parts, createNewPageable(page));
//        p.map(mapper::contactToGetAllContactsDTO);

        int totalLength = 0 ;
        if (name.split(" ").length >= 2)
        {

            String[] nameAfterSplit = name.split(" ");
            log.error("{} és {}",nameAfterSplit[0], nameAfterSplit[1]);
            var firstSubPage = contactRepository.findContactsByFirstNameContainingAndStatusAndLastNameContainingAndStatus(nameAfterSplit[0], Status.ACTIVE, nameAfterSplit[1], Status.ACTIVE,createNewPageable(page)).map(mapper::contactToGetAllContactsDTO);
            totalLength += firstSubPage.getTotalElements();
            List<GetAllContactsDTO> firstSublist = firstSubPage.getContent();
            var lastSubPage =  contactRepository.findContactsByFirstNameContainingAndStatusAndLastNameContainingAndStatus(nameAfterSplit[1], Status.ACTIVE, nameAfterSplit[0], Status.ACTIVE,createNewPageable(page)).map(mapper::contactToGetAllContactsDTO);
            totalLength += lastSubPage.getTotalElements();
            List<GetAllContactsDTO> secondSublist = lastSubPage.getContent();


            Page<GetAllContactsDTO> newVersion = contactRepository.findContactsByStatusAndFirstNameContainingOrLastNameContaining(Status.ACTIVE, nameAfterSplit[0], nameAfterSplit[1], createNewPageable(page)).map(mapper::contactToGetAllContactsDTO);
            log.error("New version: {}, {}", newVersion, newVersion.getTotalElements());

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

            var subPage = contactRepository.findContactsByFirstNameContainingAndStatusOrLastNameContainingAndStatus(nameAfterSplit[0], Status.ACTIVE, nameAfterSplit[0], Status.ACTIVE,createNewPageable(page)).map(mapper::contactToGetAllContactsDTO);
            totalLength += subPage.getTotalElements();
            List<GetAllContactsDTO> subList = subPage.getContent();

            if (!subList.isEmpty())
            {
                allCorrectNames.addAll(subList);
            }
        }
        else
        {
            var subPage = contactRepository.findContactsByFirstNameContainingAndStatusOrLastNameContainingAndStatus(name, Status.ACTIVE, name, Status.ACTIVE,createNewPageable(page)).map(mapper::contactToGetAllContactsDTO);
            totalLength += subPage.getTotalElements();
            List<GetAllContactsDTO> subList = subPage.getContent();

            if (!subList.isEmpty())
            {
                allCorrectNames.addAll(subList);
            }
        }

        Integer numberOfPages = totalLength / numberOfContactsByPage;

        if (numberOfPages % numberOfContactsByPage != 0)
        {
            numberOfPages++;
        }
        log.info("{},{},{}, {}", numberOfPages, totalLength, page + 1, numberOfContactsByPage);

        allCorrectNames = allCorrectNames.size() > numberOfContactsByPage ?
                allCorrectNames.subList(0, numberOfContactsByPage) : allCorrectNames;

        return new GetAllContactsWithNumberOfContactsDto(allCorrectNames, numberOfPages, totalLength, page + 1, numberOfContactsByPage);
    }

    @Override
    public GetAllContactsWithNumberOfContactsDto findContactsByCompany(Integer page,Long companyId)
    {
        if (companyId == 0)
        {
            return findAllContacts(page, null, null, null);
        }

        log.error("type of companyID : {}", companyId.getClass());

        int totalLength = 0;
        var list = contactRepository.findContactsByCompanyIdAndStatus(companyId, Status.ACTIVE,createNewPageable(page)).map(mapper::contactToGetAllContactsDTO);
        totalLength += list.getTotalElements();
        List<GetAllContactsDTO> allCorrectNames = list.getContent();

        Integer numberOfPages = totalLength / numberOfContactsByPage;

        if (numberOfPages % numberOfContactsByPage != 0)
        {
            numberOfPages++;
        }
        log.info("{},{},{}, {}", numberOfPages, totalLength, page + 1, numberOfContactsByPage);

        allCorrectNames = allCorrectNames.size() > numberOfContactsByPage ?
                allCorrectNames.subList(0, numberOfContactsByPage) : allCorrectNames;


        return new GetAllContactsWithNumberOfContactsDto(allCorrectNames, numberOfPages, totalLength, page + 1, numberOfContactsByPage);
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

    private GetAllContactsWithNumberOfContactsDto createGetAllContactsWithNumber(List<GetAllContactsDTO> list, Integer currentPageNumber)
    {
        Integer totalLength = contactRepository.findByStatus(Status.ACTIVE, null).getNumberOfElements();

        Integer numberOfPages= totalLength / numberOfContactsByPage;

        if (numberOfPages % numberOfContactsByPage != 0)
        {
            numberOfPages++;
        }
        log.info("There is {} page total", numberOfPages);
//        log.info("{},{},{}, {}", numberOfPages, totalLength, currentPageNumber + 1, numberOfContactsByPage);
        GetAllContactsWithNumberOfContactsDto dto = new GetAllContactsWithNumberOfContactsDto(list, numberOfPages, totalLength, currentPageNumber == null ? 0 : currentPageNumber + 1, numberOfContactsByPage);

        return dto;
    }



}

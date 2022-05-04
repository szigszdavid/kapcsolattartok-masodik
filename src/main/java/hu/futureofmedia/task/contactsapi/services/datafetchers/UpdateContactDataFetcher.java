package hu.futureofmedia.task.contactsapi.services.datafetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import hu.futureofmedia.task.contactsapi.services.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

@Slf4j
@Component
public class UpdateContactDataFetcher implements DataFetcher<Long> {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ContactMapper mapper;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private CompanyService companyService;

    @Override
    public Long get(DataFetchingEnvironment environment) throws Exception {

        Long id = Long.parseLong(environment.getArgument("id"));

        ContactDTO contactDTO = new ContactDTO(id, environment.getArgument("firstName"), environment.getArgument("lastName"), environment.getArgument("emailAddress"), environment.getArgument("phoneNumber"),environment.getArgument("comment"),Long.parseLong(environment.getArgument("companyId")));

        log.info("updateContact called with id: {}, and ContactDTO : {}", id, contactDTO);

        Contact contact = findById(id);

        mapper.updateContactWithMapper(contactDTO, contact);

        setCompany(contact, contactDTO);

        log.debug("Contact before save: {}", contact);

        contactRepository.save(contact);

        log.debug("Contact saved to database!");

        return contact.getId();
    }

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

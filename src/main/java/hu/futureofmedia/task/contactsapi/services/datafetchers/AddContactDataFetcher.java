package hu.futureofmedia.task.contactsapi.services.datafetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import hu.futureofmedia.task.contactsapi.services.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@Component
public class AddContactDataFetcher implements DataFetcher<Long> {

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


        ContactDTO contactDTO = new ContactDTO(null, environment.getArgument("firstName"), environment.getArgument("lastName"), environment.getArgument("emailAddress"), environment.getArgument("phoneNumber"),environment.getArgument("comment"),Long.parseLong(environment.getArgument("companyId")));

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

    private void setCompany(Contact contact, ContactDTO contactDTO) {

        Company company = companyService.findById(contactDTO.getCompanyId());

        contact.setCompany(company);

    }
}

package hu.futureofmedia.task.contactsapi.services.datafetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
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
public class DeleteContactDataFetcher implements DataFetcher<Boolean> {

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
    public Boolean get(DataFetchingEnvironment environment) throws Exception {
        log.info("deleteContact in ContactServiceImp called!");

        Long id = Long.parseLong(environment.getArgument("id"));

        Contact contact = findById(id);

        contact.setStatus(Status.DELETED);

        log.debug("Contact before save: {}", contact);

        contactRepository.save(contact);

        log.debug("Contact saved to database!");

        return true;
    }

    public Contact findById(Long id) throws ContactNotFoundExcpetion
    {
        log.info("findById called with id: {}", id);

        return contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundExcpetion("Contact not found"));
    }
}

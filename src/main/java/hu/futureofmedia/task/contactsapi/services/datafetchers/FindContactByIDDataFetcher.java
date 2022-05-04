package hu.futureofmedia.task.contactsapi.services.datafetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component

public class FindContactByIDDataFetcher implements DataFetcher<GetContactByIdDTO> {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ContactMapper mapper;

    @Override
    public GetContactByIdDTO get(DataFetchingEnvironment environment) {

        Long id = Long.parseLong(environment.getArgument("id"));

        log.info("findContactByID with id: {} called!", id);

        Contact contact = null;
        try {
            contact = findById(id);
        } catch (ContactNotFoundExcpetion e) {
            e.printStackTrace();
        }

        GetContactByIdDTO getContactByIdDTO = mapper.contactToGetContactByDTO(contact);

        log.debug("GetContactByIdDTO created!");

        return getContactByIdDTO;
    }

    public Contact findById(Long id) throws ContactNotFoundExcpetion
    {
        log.info("findById called with id: {}", id);

        return contactRepository.findById(id).orElseThrow(() -> new ContactNotFoundExcpetion("Contact not found"));
    }
}

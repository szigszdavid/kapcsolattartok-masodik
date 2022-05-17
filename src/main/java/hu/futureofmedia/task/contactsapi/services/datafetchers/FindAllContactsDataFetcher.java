package hu.futureofmedia.task.contactsapi.services.datafetchers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class FindAllContactsDataFetcher implements DataFetcher<List<GetAllContactsDTO>> {

    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private ContactMapper mapper;

    @Value("${number.of.contacts.by.page}")
    private Integer numberOfContactsByPage;

    @Override
    public List<GetAllContactsDTO> get(DataFetchingEnvironment environment) {

        Integer page = environment.getArgument("page");

        List<GetAllContactsDTO> list = contactRepository.findByStatus(Status.ACTIVE, createNewPageable(page)).map(mapper::contactToGetAllContactsDTO).toList();
        log.info("data in list: {}", list);
        return list;
    }

    @Transactional
    public Pageable createNewPageable(Integer page)
    {
        log.info("createNewPageable called with page: {}", page);

        Pageable pageable = PageRequest.of(page == null ? 0 : page,numberOfContactsByPage, Sort.by("firstName").and(Sort.by("lastName")));

        log.debug("Pageable created on page: {} with {} Contact/page", page, pageable.getPageSize());

        return pageable;
    }
}

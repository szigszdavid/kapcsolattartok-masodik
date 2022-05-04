package hu.futureofmedia.task.contactsapi.services;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import hu.futureofmedia.task.contactsapi.services.datafetchers.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jms.Queue;
import java.io.File;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class GraphQLServiceImpl implements GraphQLService{

    @Autowired
    private Queue queue;

    @Autowired
    private JmsTemplate jmsTemplate;


    private final ContactRepository contactRepository;
    private final CompanyService companyService;
    private final ContactMapper mapper;

    @Value("${number.of.contacts.by.page}")
    private Integer numberOfContactsByPage;

    @Value("classpath:schema.graphql")
    Resource resource;

    private GraphQL graphQl;

    @Autowired
    private FindAllContactsDataFetcher findAllContactsDataFetcher;
    @Autowired
    private FindContactByIDDataFetcher findContactByIDDataFetcher;
    @Autowired
    private AddContactDataFetcher addContactDataFetcher;
    @Autowired
    private UpdateContactDataFetcher updateContactDataFetcher;
    @Autowired
    private DeleteContactDataFetcher deleteContactDataFetcher;

    @PostConstruct
    private void loadSchema() throws IOException {
        File schemaFile = resource.getFile();

        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, wiring);
        graphQl = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring ->
                        typeWiring
                                .dataFetcher("findAllContacts", findAllContactsDataFetcher)
                                .dataFetcher("findContactById", findContactByIDDataFetcher)

                )
                .type(TypeRuntimeWiring.newTypeWiring("Mutation")
                        .dataFetcher("addContact", addContactDataFetcher)
                        .dataFetcher("updateContact", updateContactDataFetcher)
                        .dataFetcher("deleteContact", deleteContactDataFetcher)
                )
                .build();
    }

    @Override
    public GraphQL getGraphQL() {
        return graphQl;
    }


}

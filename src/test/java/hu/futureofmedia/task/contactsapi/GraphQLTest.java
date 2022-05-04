package hu.futureofmedia.task.contactsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.futureofmedia.task.contactsapi.apierrors.ApiError;
import hu.futureofmedia.task.contactsapi.dtos.CompanyDTO;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.services.CompanyService;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GraphQLTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ContactService contactService;

    @BeforeEach
    public void onSetUp()
    {
        companyService.addCompany(new CompanyDTO(1L, "Company #1"));
    }

    @Test
    public void getAllContactTest() throws Exception
    {
        ContactDTO contactDTOFirst = createValidContact(1L);
        ContactDTO contactDTOSecond = createValidContact(2L);

        String query = "{findAllContacts(page: 0)\n" +
                "    {\n" +
                "        firstName\n" +
                "        lastName\n" +
                "        emailAddress\n" +
                "        phoneNumber\n" +
                "        company {\n" +
                "            id\n" +
                "            name\n" +
                "        }\n" +
                "        comment\n" +
                "        status\n" +
                "    }}";
        mvc.perform(
                        post("/graphql")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(query)
                                .param("page","0")

                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.findAllContacts[0].firstName", is(contactDTOFirst.getFirstName() )))
                .andExpect(jsonPath("$.data.findAllContacts[1].firstName", is(contactDTOSecond.getFirstName())));
    }

    @Test
    void findContactByRealIdTest() throws Exception {
        ContactDTO contactDTO = createValidContact(1L);

        String query = "{findContactById(id: 1)\n" +
                "     {\n" +
                "        id\n" +
                "        firstName\n" +
                "        lastName\n" +
                "        emailAddress\n" +
                "        phoneNumber\n" +
                "        company {\n" +
                "            id\n" +
                "            name\n" +
                "        }\n" +
                "        comment\n" +
                "        status\n" +
                "    }}";
        mvc.perform(post("/graphql")
                        .contentType("application/json")
                        .content(query)
                        .with(user("user").roles("LIST")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.findContactById.id").value(1L));

    }

    @Test
    void findContactByFakeIdTest() throws Exception {

        String query = "{findContactById(id: 10)\n" +
                "     {\n" +
                "        id\n" +
                "        firstName\n" +
                "        lastName\n" +
                "        emailAddress\n" +
                "        phoneNumber\n" +
                "        company {\n" +
                "            id\n" +
                "            name\n" +
                "        }\n" +
                "        comment\n" +
                "        status\n" +
                "    }}";
        mvc.perform(get("/graphql",300)
                        .contentType("application/json")
                        .with(user("user").roles("LIST"))
                        .content(query))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());

    }

    @Test
    public void numberOfContactsByPageTest() throws Exception {
        ContactDTO contactDTOFirst = createValidContact(1L);
        createValidContact(2L);
        createValidContact(3L);
        createValidContact(4L);
        createValidContact(5L);
        createValidContact(6L);
        createValidContact(7L);
        createValidContact(8L);
        createValidContact(9L);
        createValidContact(10L);

        String queryFirstPage = "{\n" +
                "    findAllContacts(page: 0)\n" +
                "    {\n" +
                "        firstName\n" +
                "        lastName\n" +
                "        emailAddress\n" +
                "        phoneNumber\n" +
                "        company {\n" +
                "            id\n" +
                "            name\n" +
                "        }\n" +
                "        comment\n" +
                "        status\n" +
                "    }}";

        String querySecondPage = "{\n" +
                "    findAllContacts(page: 1)\n" +
                "    {\n" +
                "        firstName\n" +
                "        lastName\n" +
                "        emailAddress\n" +
                "        phoneNumber\n" +
                "        company {\n" +
                "            id\n" +
                "            name\n" +
                "        }\n" +
                "        comment\n" +
                "        status\n" +
                "    }}";

        String content = mvc.perform(
                        post("/graphql")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page","0")
                                .content(queryFirstPage))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.findAllContacts[0].firstName", is(contactDTOFirst.getFirstName())))
                .andReturn().getResponse().getContentAsString();

        ContactDTO contactDTOLast = createValidContact(11L);

        String secondPageContent = mvc.perform(
                        post("/graphql")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(querySecondPage)
                                .param("page","1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.findAllContacts[0].firstName", is(contactDTOLast.getFirstName())))
                .andReturn().getResponse().getContentAsString();


    }

    @Test
    public void numberOfContactsByPageAfterDeleteTest() throws Exception {
        createValidContact(1L);
        createValidContact(2L);
        createValidContact(3L);
        createValidContact(4L);
        createValidContact(5L);
        createValidContact(6L);
        createValidContact(7L);
        createValidContact(8L);
        createValidContact(9L);
        createValidContact(10L);
        ContactDTO contactDTOLast = createValidContact(11L);

        String queryBeforeDelete = "{findAllContacts(page: 1)\n" +
                "    {\n" +
                "        firstName\n" +
                "        lastName\n" +
                "        emailAddress\n" +
                "        phoneNumber\n" +
                "        company {\n" +
                "            id\n" +
                "            name\n" +
                "        }\n" +
                "        comment\n" +
                "        status\n" +
                "    }}";

        String queryDelete = "mutation {deleteContact(id : 11)}";

        String queryAfterDelete = "{findAllContacts(page: 1)\n" +
                "    {\n" +
                "        firstName\n" +
                "        lastName\n" +
                "        emailAddress\n" +
                "        phoneNumber\n" +
                "        company {\n" +
                "            id\n" +
                "            name\n" +
                "        }\n" +
                "        comment\n" +
                "        status\n" +
                "    }}";

        String secondPageContent = mvc.perform(
                        post("/graphql")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(queryBeforeDelete)
                                .param("page","1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.findAllContacts[0].firstName", is(contactDTOLast.getFirstName())))
                .andReturn().getResponse().getContentAsString();

        mvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("user").roles("DELETE"))
                        .content(queryDelete))
                .andExpect(status().isOk());

        secondPageContent = mvc.perform(
                        post("/graphql")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page","1")
                                .with(user("user").roles("USER"))
                                .content(queryAfterDelete))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals("{\"errors\":[],\"data\":{\"findAllContacts\":[]},\"extensions\":null,\"dataPresent\":true}", secondPageContent);

    }

    /*
    @Test
    public void blankFirstNameTest() throws Exception
    {
        String query = "mutation {\n" +
                "\n" +
                "   addContact( firstName : \"\", lastName : \"dfg\", emailAddress: \"abc@gmail.com\", phoneNumber: \"06301234567\", comment: \"Hello\", companyId : 2)\n" +
                "}";

        MvcResult result = mvc.perform(post("/graphql")
                        .contentType("application/json")
                        .content(query))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiError apiError = objectMapper.readValue(content, ApiError.class);
        assertEquals("firstName: ContactDTO.firstName.Required", apiError.getErrors().get(0));

    }

    @Test
    public void invalidEmailAddressTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setEmailAddress("valamigmail.com");

        String body = objectMapper.writeValueAsString(contactDTO);

        MvcResult result = mvc.perform(post("/graphql")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiError apiError = objectMapper.readValue(content, ApiError.class);
        assertEquals("emailAddress: ContactDTO.emailAddress.Email format required", apiError.getErrors().get(0));
    }

    @Test
    public void invalidPhoneNumberTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setPhoneNumber("1234567");

        String body = objectMapper.writeValueAsString(contactDTO);

        MvcResult result = mvc.perform(post("/graphql")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiError apiError = objectMapper.readValue(content, ApiError.class);
        assertEquals("phoneNumber: Contact.PhoneNumber wrong format",apiError.getErrors().get(0));

    }

    @Test
    public void invalidCompanyTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setCompanyId(4L);

        String body = objectMapper.writeValueAsString(contactDTO);

        MvcResult result = mvc.perform(post("/graphql")
                        .contentType("application/json")
                        .with(user("admin").roles("CREATE"))
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiError apiError = objectMapper.readValue(content, ApiError.class);
        assertEquals("companyId: Contact.Company does not exists", apiError.getErrors().get(0));
    }

     */

    @Test
    public void deleteContactTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);

        String queryDelete = "mutation { deleteContact(id : 1) }";

        String queryAfterDelete = "{findContactById(id: 1)\n" +
            "     {\n" +
            "        id\n" +
            "        firstName\n" +
            "        lastName\n" +
            "        emailAddress\n" +
            "        phoneNumber\n" +
            "        company {\n" +
            "            id\n" +
            "            name\n" +
            "        }\n" +
            "        comment\n" +
            "        status\n" +
            "    }}";

        mvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(queryDelete)
                        .with(user("admin").roles("DELETE")))
                .andExpect(status().isOk());

        mvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(queryAfterDelete)
                        .with(user("admin").roles("LIST")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.findContactById.id").value(1L))
                .andExpect(jsonPath("$.data.findContactById.status").value("DELETED"));
    }


    @Test
    public void updateContactTest() throws Exception {

        ContactDTO contactDTO = createValidContact(1L);

        String queryUpdate = "mutation {\r\n\r\n   updateContact(id : 1, firstName : \"Asd\", lastName : \"dfg\", emailAddress: \"abc@gmail.com\", phoneNumber: \"06301234567\", comment: \"Hello\", companyId : 1)\r\n}";

        String queryAfterDelete = "{findContactById(id: 1)\n" +
                "     {\n" +
                "        id\n" +
                "        firstName\n" +
                "        lastName\n" +
                "        emailAddress\n" +
                "        phoneNumber\n" +
                "        company {\n" +
                "            id\n" +
                "            name\n" +
                "        }\n" +
                "        comment\n" +
                "        status\n" +
                "    }}";

        mvc.perform(post("/graphql")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(queryUpdate)
                        .with(user("user").roles("MODIFY")))
                .andExpect(status().isOk());

        mvc.perform(post("/graphql")
                        .contentType("application/json")
                        .content(queryAfterDelete)
                        .with(user("user").roles("LIST")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.findContactById.id").value(1))
                .andExpect(jsonPath("$.data.findContactById.lastName", is("dfg")));

    }

    @Test
    void addValidContactTest() throws Exception {

        String queryAdd = "mutation {\r\n\r\n   addContact(firstName : \"Asd\", lastName : \"dfg\", emailAddress: \"abc@gmail.com\", phoneNumber: \"06301234567\", comment: \"Hello\", companyId : 1)\r\n}";

        mvc.perform(post("/graphql")
                        .contentType("application/json")
                        .content(queryAdd)
                        .with(user("user").roles("CREATE")))
                .andExpect(jsonPath("$.data.addContact", is("1")));

    }


    private ContactDTO createValidContact(Long id)
    {
        ContactDTO contactDTO = new ContactDTO();

        contactDTO.setId(id);
        contactDTO.setFirstName("FirstName");
        contactDTO.setLastName("LastName");
        contactDTO.setEmailAddress("emailAdress@gmail.com");
        contactDTO.setPhoneNumber("+36301234567");
        contactDTO.setCompanyId(1L);

        contactService.addContact(contactDTO);

        return contactDTO;
    }

    private ContactDTO createUpdateContact(Long id)
    {
        ContactDTO contactDTO = new ContactDTO();

        contactDTO.setId(id);
        contactDTO.setFirstName("FirstName");
        contactDTO.setLastName("Tamás");
        contactDTO.setEmailAddress("emailAdress@gmail.com");
        contactDTO.setPhoneNumber("+36301234567");
        contactDTO.setCompanyId(1L);

        return contactDTO;
    }
}

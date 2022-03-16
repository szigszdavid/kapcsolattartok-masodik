package hu.futureofmedia.task.contactsapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.futureofmedia.task.contactsapi.controllers.ContactController;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import hu.futureofmedia.task.contactsapi.services.IContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ContactController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private ContactRepository contactRepository;

    @MockBean
    private IContactService service;


    @Test
    public void getAllContact() throws Exception
    {
        createTestContact();
        createTestContact();

        mvc.perform(
                        get("/contacts").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].firstName", is("FirstName")))
                .andExpect(jsonPath("$[1].firstName", is("FirstName")));
    }

    @Test
    void testFindContactByRealId() throws Exception {
        createTestContact();

        mvc.perform(get("/contacts/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    void testFindContactByFakeId() throws Exception {
        ContactDTO contactDTO = createValidContact();

        MvcResult mvcResult = mvc.perform(get("/contacts/{id}",2)
                        .contentType("application/json"))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(null));
    }

    @Test
    void testAddValidContact() throws Exception {

        ContactDTO contactDTO = createValidContact();
        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());

    }

    @Test
    void testAddInvalidContact() throws Exception {

        ContactDTO contactDTO = new ContactDTO();
        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    private ContactDTO createValidContact()
    {
        ContactDTO contactDTO = new ContactDTO();

        contactDTO.setId(1L);
        contactDTO.setFirstName("FirstName");
        contactDTO.setLastName("LastName");
        contactDTO.setEmailAddress("emailAdress@gmail.com");
        contactDTO.setPhoneNumber("+36301234567");
        contactDTO.setCompanyName("Company #1");
        contactDTO.setStatus("ACTIVE");

        return contactDTO;
    }

    private void createTestContact()
    {
        Contact contact = new Contact();

        contact.setId(1L);
        contact.setFirstName("FirstName");
        contact.setLastName("LastName");
        contact.setEmailAddress("emailAdress@gmail.com");
        contact.setPhoneNumber("+36301234567");
        contact.setCompany(new Company(1L,"Company #1"));
        contact.setStatus(Status.ACTIVE);

        contactRepository.saveAndFlush(contact);
    }
}

package hu.futureofmedia.task.contactsapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.futureofmedia.task.contactsapi.dtos.CompanyDTO;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import hu.futureofmedia.task.contactsapi.services.CompanyService;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;


import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

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
    public void blankFirstNameTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setFirstName("");

        String body = objectMapper.writeValueAsString(contactDTO);

        MvcResult result = mvc.perform(post("/contacts")
                        .contentType("application/json")
                        .content(body))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("ContactDTO.firstName.Required"));
    }

    @Test
    public void invalidEmailAddressTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setEmailAddress("valamigmail.com");

        String body = objectMapper.writeValueAsString(contactDTO);

        MvcResult result = mvc.perform(post("/contacts")
                        .contentType("application/json")
                        .content(body))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("ContactDTO.emailAddress.Email format required"));

    }

    @Test
    public void invalidPhoneNumberTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setPhoneNumber("1234567");

        String body = objectMapper.writeValueAsString(contactDTO);

        MvcResult result = mvc.perform(post("/contacts")
                        .contentType("application/json")
                        .content(body))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Contact.PhoneNumber wrong format"));

    }

    @Test
    public void invalidCompanyTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setCompany(new Company(4L,"Company #4"));

        String body = objectMapper.writeValueAsString(contactDTO);

        MvcResult result = mvc.perform(post("/contacts")
                        .contentType("application/json")
                        .content(body))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("Contact.Company does not exists"));
    }

    @Test
    public void deleteContactTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);

        mvc.perform(delete("/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(get("/contacts/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status", is("DELETED")));
    }

    @Test
    public void updateContactTest() throws Exception {

        ContactDTO contactDTO = createValidContact(1L);

        ContactDTO updateContactDTO = createUpdateContact(1L);

        String body = objectMapper.writeValueAsString(updateContactDTO);

        mvc.perform(put("/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mvc.perform(get("/contacts/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName", is("Tamás")))
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    public void getAllContactTest() throws Exception
    {
        ContactDTO contactDTOFirst = createValidContact(1L);
        ContactDTO contactDTOSecond = createValidContact(2L);

        mvc.perform(
                        get("/contacts").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].fullName", is(contactDTOFirst.getFirstName() + " " + contactDTOFirst.getLastName())))
                .andExpect(jsonPath("$.[1].fullName", is(contactDTOSecond.getFirstName() + " " + contactDTOSecond.getLastName())));
    }

    @Test
    void findContactByRealIdTest() throws Exception {
        ContactDTO contactDTO = createValidContact(1L);

        mvc.perform(get("/contacts/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    void findContactByFakeIdTest() throws Exception {

        mvc.perform(get("/contacts/{id}",3)
                        .contentType("application/json"))
                        .andExpect(status().isBadRequest())
                        .andDo(print());

    }

    @Test
    void addValidContactTest() throws Exception {

        ContactDTO contactDTO = createValidContact(1L);
        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts")
                        .contentType("application/json")
                        .content(body))
                .andExpect(jsonPath("$", is(1)));

    }


    private ContactDTO createValidContact(Long id)
    {
        ContactDTO contactDTO = new ContactDTO();

        contactDTO.setId(id);
        contactDTO.setFirstName("FirstName");
        contactDTO.setLastName("LastName");
        contactDTO.setEmailAddress("emailAdress@gmail.com");
        contactDTO.setPhoneNumber("+36301234567");
        contactDTO.setCompany(companyService.findById(1L));

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
        contactDTO.setCompany(companyService.findById(1L));

        return contactDTO;
    }


}

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    private ContactRepository contactRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void blankFirstNameTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setFirstName("");

        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("Vezetéknév nem lehet üres", result.getResponse().getContentAsString()))
                .andExpect(result -> assertEquals(409, result.getResponse().getStatus()));
    }

    @Test
    public void nullFirstNameTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setFirstName(null);

        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("Meg kell adni a vezetéknevet", result.getResponse().getContentAsString()))
                .andExpect(result -> assertEquals(409, result.getResponse().getStatus()));
    }

    @Test
    public void invalidEmailAddressTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setEmailAddress("valamigmail.com");

        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("Az e-mail címnek helyes formátumúnak kell lennie!", result.getResponse().getContentAsString()))
                .andExpect(result -> assertEquals(409, result.getResponse().getStatus()));

    }

    @Test
    public void invalidPhoneNumberTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setPhoneNumber("1234567");

        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof NumberFormatException))
                .andExpect(result -> assertEquals("A telefonszámnak helyes formátumúnak kell lennie", result.getResponse().getContentAsString()))
                .andExpect(result -> assertEquals(409, result.getResponse().getStatus()));
    }

    @Test
    public void invalidCompanyTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setCompanyName("Company #4");

        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException))
                .andExpect(result -> assertEquals("Nem létező cég lett kiválasztva", result.getResponse().getContentAsString()))
                .andExpect(result -> assertEquals(409, result.getResponse().getStatus()));
    }

    @Test
    public void invalidStatusTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setStatus("NEW");

        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(result -> assertEquals("Nem létező státusz lett beállítva, válasszon egyet a következőek közül: ACTIVE, DELETED", result.getResponse().getContentAsString()))
                .andExpect(result -> assertEquals(409, result.getResponse().getStatus()));
    }

    @Test
    public void missingColumnDataTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setFirstName("");

        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(result -> assertEquals(400, result.getResponse().getStatus()));
    }

    @Test
    public void deleteContactTest() throws Exception
    {
        createTestContact("Take4");

        mvc.perform(put("/contacts/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String actualResponseBody = mvc.perform(get("/contacts/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status", is("DELETED")))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updateContactTest() throws Exception {

        createTestContact("Take3");

        ContactDTO contactDTO = createValidContact(1L);

        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(put("/contacts/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        String actualResponseBody = mvc.perform(get("/contacts/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName", is("FirstName")))
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    public void getAllContactTest() throws Exception
    {
        createTestContact("Take1");
        createTestContact("Take2");

        mvc.perform(
                        get("/contacts").contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //.andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$.[0].fullName", is("Take1 LastName")))
                .andExpect(jsonPath("$.[1].fullName", is("Take2 LastName")));
    }

    @Test
    void findContactByRealIdTest() throws Exception {
        createTestContact("Take1");

        mvc.perform(get("/contacts/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    void findContactByFakeIdTest() throws Exception {
        ContactDTO contactDTO = createValidContact(2L);

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
    void addValidContactTest() throws Exception {

        ContactDTO contactDTO = createValidContact(1L);
        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isOk());

    }

    @Test
    void addInvalidContactTest() throws Exception {

        ContactDTO contactDTO = new ContactDTO();
        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts/add")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    private ContactDTO createValidContact(Long id)
    {
        ContactDTO contactDTO = new ContactDTO();

        contactDTO.setId(id);
        contactDTO.setFirstName("FirstName");
        contactDTO.setLastName("LastName");
        contactDTO.setEmailAddress("emailAdress@gmail.com");
        contactDTO.setPhoneNumber("+36301234567");
        contactDTO.setCompanyName("DTOCompany #1");
        companyRepository.saveAndFlush(new Company(contactDTO.getCompanyName()));
        contactDTO.setStatus("ACTIVE");

        return contactDTO;
    }

    private void createTestContact(String firstName)
    {
        Contact contact = new Contact();

        contact.setFirstName(firstName);
        contact.setLastName("LastName");
        contact.setEmailAddress("emailAdress@gmail.com");
        contact.setPhoneNumber("+36301234567");
        companyRepository.save(new Company(firstName));
        contact.setCompany(companyRepository.findCompanyByName(firstName));
        contact.setStatus(Status.ACTIVE);

        contactRepository.saveAndFlush(contact);
    }
}

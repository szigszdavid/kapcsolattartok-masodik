package hu.futureofmedia.task.contactsapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.futureofmedia.task.contactsapi.apierrors.ApiError;
import hu.futureofmedia.task.contactsapi.dtos.CompanyDTO;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.CreateUserRequest;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.services.CompanyService;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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

        String content = mvc.perform(
                        get("/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page","0"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].fullName", is(contactDTOFirst.getFirstName() + " " + contactDTOFirst.getLastName())))
                .andReturn().getResponse().getContentAsString();

        assertEquals(10,content.split("fullName").length - 1);

        ContactDTO contactDTOLast = createValidContact(11L);

        String secondPageContent = mvc.perform(
                        get("/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page","1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].fullName", is(contactDTOLast.getFirstName() + " " + contactDTOLast.getLastName())))
                .andReturn().getResponse().getContentAsString();

        assertEquals(1,secondPageContent.split("fullName").length - 1);
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

        String secondPageContent = mvc.perform(
                        get("/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page","1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].fullName", is(contactDTOLast.getFirstName() + " " + contactDTOLast.getLastName())))
                .andReturn().getResponse().getContentAsString();


        assertEquals(1,secondPageContent.split("fullName").length - 1);

        mvc.perform(delete("/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("user").roles("ADMIN")))
                .andExpect(status().isOk());

        secondPageContent = mvc.perform(
                        get("/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page","1")
                                .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertEquals("[]", secondPageContent);

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
        ApiError apiError = objectMapper.readValue(content, ApiError.class);
        assertEquals("firstName: ContactDTO.firstName.Required", apiError.getErrors().get(0));

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
        ApiError apiError = objectMapper.readValue(content, ApiError.class);
        assertEquals("emailAddress: ContactDTO.emailAddress.Email format required", apiError.getErrors().get(0));
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
        ApiError apiError = objectMapper.readValue(content, ApiError.class);
        assertEquals("phoneNumber: Contact.PhoneNumber wrong format",apiError.getErrors().get(0));

    }

    @Test
    public void invalidCompanyTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);
        contactDTO.setCompanyId(4L);

        String body = objectMapper.writeValueAsString(contactDTO);

        MvcResult result = mvc.perform(post("/contacts")
                        .contentType("application/json")
                        .content(body))
                        .andExpect(status().isBadRequest())
                        .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiError apiError = objectMapper.readValue(content, ApiError.class);
        assertEquals("companyId: Contact.Company does not exists", apiError.getErrors().get(0));
    }

    @Test
    public void deleteContactTest() throws Exception
    {
        ContactDTO contactDTO = createValidContact(1L);

        mvc.perform(delete("/contacts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());

        mvc.perform(get("/contacts/1")
                        .contentType("application/json")
                        .with(user("admin").roles("USER")))
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
                        .content(body)
                        .with(user("user").roles("ADMIN")))
                .andExpect(status().isOk());

        mvc.perform(get("/contacts/1")
                        .contentType("application/json")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.lastName", is("Tamás")));

    }

    @Test
    public void getAllContactTest() throws Exception
    {
        ContactDTO contactDTOFirst = createValidContact(1L);
        ContactDTO contactDTOSecond = createValidContact(2L);

        mvc.perform(
                        get("/contacts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page","0")
                                )
                        .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].fullName", is(contactDTOFirst.getFirstName() + " " + contactDTOFirst.getLastName())))
                .andExpect(jsonPath("$.[1].fullName", is(contactDTOSecond.getFirstName() + " " + contactDTOSecond.getLastName())));
    }

    @Test
    void findContactByRealIdTest() throws Exception {
        ContactDTO contactDTO = createValidContact(1L);

        mvc.perform(get("/contacts/1")
                        .contentType("application/json")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    void findContactByFakeIdTest() throws Exception {

        mvc.perform(get("/contacts/{id}",300)
                        .contentType("application/json")
                        .with(user("user").roles("USER")))
                        .andExpect(status().isBadRequest())
                        .andDo(print());

    }

    @Test
    void addValidContactTest() throws Exception {

        ContactDTO contactDTO = createValidContact(1L);
        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts")
                        .contentType("application/json")
                        .content(body)
                        .with(user("user").roles("ADMIN")))
                .andExpect(jsonPath("$", is(1)));

    }

    @Test
    void createUserWithValidDataTest() throws Exception {

        CreateUserRequest createUserRequest = createValidUserProfile();

        String body = objectMapper.writeValueAsString(createUserRequest);

        mvc.perform(post("/user")
                        .contentType("application/json")
                        .content(body))
                .andExpect(jsonPath("$", is(1)));
    }

    @Test
    void createUserWithBlankDataTest() throws Exception {

        CreateUserRequest createUserRequest = createValidUserProfile();
        createUserRequest.setUsername("");

        String body = objectMapper.writeValueAsString(createUserRequest);

        MvcResult result = mvc.perform(post("/user")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiError apiError = objectMapper.readValue(content, ApiError.class);
        assertEquals("username: CreateUserRequest.username.Required", apiError.getErrors().get(0));
    }

    @Test
    void createUserWithInvalidEmailAddressTest() throws Exception {

        CreateUserRequest createUserRequest = createValidUserProfile();
        createUserRequest.setUsername("usernamegmail.com");

        String body = objectMapper.writeValueAsString(createUserRequest);

        MvcResult result = mvc.perform(post("/user")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ApiError apiError = objectMapper.readValue(content, ApiError.class);
        assertEquals("username: CreateUserRequest.username Email format required", apiError.getErrors().get(0));
    }

    @Test
    void addValidContactWithUserProfileTest() throws Exception {

        ContactDTO contactDTO = createValidContact(1L);
        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts")
                        .contentType("application/json")
                        .content(body)
                        .with(user("user").roles("USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void addValidContactWithoutProfileTest() throws Exception {

        ContactDTO contactDTO = createValidContact(1L);
        String body = objectMapper.writeValueAsString(contactDTO);

        mvc.perform(post("/contacts")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void findContactByIdWithAdminProfileTest() throws Exception {

        ContactDTO contactDTO = createValidContact(1L);

        mvc.perform(get("/contacts/1")
                        .contentType("application/json")
                        .with(user("user").roles("ADMIN")))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void findContactByIdWithoutProfileTest() throws Exception {
        ContactDTO contactDTO = createValidContact(1L);

        mvc.perform(get("/contacts/1")
                        .contentType("application/json")
                        )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private CreateUserRequest createValidUserProfile()
    {
        CreateUserRequest createUserRequest = new CreateUserRequest();

        createUserRequest.setUsername("username@gmail.com");
        createUserRequest.setFullName("fullname");
        createUserRequest.setPassword("password");
        createUserRequest.setRolesId(new HashSet<>(1));

        return createUserRequest;
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

package hu.futureofmedia.task.contactsapi;

import hu.futureofmedia.task.contactsapi.apierrors.ApiError;
import hu.futureofmedia.task.contactsapi.controllers.ContactController;
import hu.futureofmedia.task.contactsapi.dtos.CompanyDTO;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.services.CompanyService;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import org.springframework.transaction.TransactionSystemException;

import javax.validation.ConstraintViolationException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ServiceTest {

    /*
    @Autowired
    private ContactService contactService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;


    @BeforeEach
    public void onSetUp()
    {
        CompanyDTO companyDTO1 = createValidCompany("Company #1");
        CompanyDTO companyDTO2 = createValidCompany("Company #2");
        CompanyDTO companyDTO3 = createValidCompany("Company #3");

    }

    @Test
    void updateExistingContactTest()
    {
        ContactDTO input = createValidContact();
        contactService.addContact(input);

        assertEquals(input.getFirstName(), "FirstName");
        assertEquals(input.getLastName(), "LastName");

        input.setFirstName("updatedFirstName");
        input.setLastName("updatedLastName");

        try{
            contactService.updateContact(input, 1L);
        } catch (ContactNotFoundExcpetion e) {};


        GetContactByIdDTO output = new GetContactByIdDTO();
        try{
            output = contactService.findContactByID(input.getId());
        } catch (ContactNotFoundExcpetion e) {};


        assertEquals(output.getFirstName(), "updatedFirstName");
        assertEquals(output.getLastName(), "updatedLastName");


        ContactDTO input = createValidContact();
        when(mock.contactService.addContact(input)).thenReturn(input);
        assertEquals(input, contactService.addContact(input));
    }

    @Test
    void updateExistingContactWithBlankDataTest()
    {
        ContactDTO input = createValidContact();
        contactService.addContact(input);

        assertEquals(input.getFirstName(), "FirstName");
        assertEquals(input.getLastName(), "LastName");

        input.setFirstName("");
        input.setLastName("");

        try{
            contactService.updateContact(input, 1L);
        } catch (ContactNotFoundExcpetion e) {}

        //companyService.deleteAll();
    }

    @Test
    void findByValidIDTest(){
        Contact input = createValidContact();

        service.addContact(input);
        Contact output = service.findContactByID(input.getId()).get();

        assertEquals(input.getStatus(),output.getStatus());
        assertEquals(input.getFirstName(),output.getFirstName());
        assertEquals(input.getLastName(),output.getLastName());
        assertEquals(input.getCompany().getName(),output.getCompany().getName());
        assertEquals(input.getEmailAddress(),output.getEmailAddress());
    }

    @Test
    void findByInvalidIdTest(){
        Contact input = createValidContact();

        service.addContact(input);

        assertThrows(ConstraintViolationException.class, () -> {
            service.findContactByID(2L).get();
        });
    }
    @Test
    void addInvalidContactTest() {

        Contact input = createInValidContact();

        assertThrows(ConstraintViolationException.class, () -> {
            service.addContact(input);
        });

    }

    @Test
    void addValidContactTest()
    {
        Page<Contact> contactListBefore = service.findAllContacts(null);

        assertEquals(0, contactListBefore.getNumberOfElements());

        service.addContact(createValidContact());

        Page<Contact> contactListAfter = service.findAllContacts(null);

        assertEquals(1, contactListAfter.getNumberOfElements());
    }

    @Test
    void findAllContactsTest()
    {
        Contact exampleInputOne = createValidContact();

        service.addContact(exampleInputOne);

        assertEquals(1, service.findAllContacts(0).getNumberOfElements());
    }

    private Contact createInValidContact() {

        Contact contact = new Contact();

        contact.setFirstName("FirstName");
        contact.setLastName("");

        return contact;
    }

    private ContactDTO createValidContact() {

        ContactDTO contactDTO = new ContactDTO();

        contactDTO.setId(1L);
        contactDTO.setFirstName("FirstName");
        contactDTO.setLastName("LastName");
        contactDTO.setEmailAddress("emailAdress@gmail.com");
        contactDTO.setPhoneNumber("+36301234567");
        contactDTO.setCompany(companyService.findById(1L));
        contactDTO.setStatus("ACTIVE");

        return contactDTO;
    }

    private CompanyDTO createValidCompany(String name)
    {
        CompanyDTO companyDTO = new CompanyDTO(name);

        return companyDTO;
    }
    */
}

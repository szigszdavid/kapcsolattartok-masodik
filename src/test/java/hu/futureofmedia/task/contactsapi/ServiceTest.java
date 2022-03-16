package hu.futureofmedia.task.contactsapi;

import hu.futureofmedia.task.contactsapi.controllers.ContactController;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import javax.validation.ConstraintViolationException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private ContactService service;

    @Autowired
    private CompanyRepository companyRepository;

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

        assertThrows(NoSuchElementException.class, () -> {
            service.findContactByID(2L).get();
        });
    }
    @Test
    void testAddContactInvalid() {

        Contact input = createInValidContact();

        assertThrows(ConstraintViolationException.class, () -> {
            service.addContact(input);
        });

    }

    @Test
    void testAddContactValid()
    {
        Page<Contact> contactListBefore = service.findAllContacts(null);

        assertEquals(0, contactListBefore.getNumberOfElements());

        service.addContact(createValidContact());

        Page<Contact> contactListAfter = service.findAllContacts(null);

        assertEquals(1, contactListAfter.getNumberOfElements());
    }

    private Contact createInValidContact() {

        Contact contact = new Contact();

        contact.setFirstName("FirstName");
        contact.setLastName("");

        return contact;
    }

    private Contact createValidContact() {

        Contact contact = new Contact();

        contact.setId(1L);
        contact.setFirstName("FirstName");
        contact.setLastName("LastName");
        contact.setEmailAddress("emailAdress@gmail.com");
        companyRepository.save(new Company("Company #1"));
        contact.setCompany(companyRepository.findCompanyByName("Company #1"));
        contact.setStatus(Status.ACTIVE);

        return contact;
    }

}

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private ContactService service;

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void testAddContactError() {

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

        System.out.println(contactListBefore);

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

        contact.setFirstName("FirstName");
        contact.setLastName("LastName");
        contact.setEmailAddress("emailAdress@gmail.com");
        companyRepository.save(new Company("Company #1"));
        contact.setCompany(companyRepository.findCompanyByName("Company #1"));
        contact.setStatus(Status.ACTIVE);

        return contact;
    }

}

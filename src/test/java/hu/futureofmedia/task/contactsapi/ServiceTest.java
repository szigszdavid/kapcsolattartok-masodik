package hu.futureofmedia.task.contactsapi;

import hu.futureofmedia.task.contactsapi.controllers.ContactController;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private ContactService service;

    @Test
    void testAddContact() {
        Page<Contact> contactList = service.findAllContacts(null);

        service.addContact(createValidContact());


    }

    private Contact createValidContact() {
        //Valahogyan kellene készíteni egy entityt
        return null;
    }

}

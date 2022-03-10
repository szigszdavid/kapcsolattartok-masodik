package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.services.IContactService;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/contacts")
public class ContactController {

    IContactService contactService;

    public ContactController(IContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public Page<Contact> findAllContacts(@RequestParam(name = "page", required = false) Integer page)
    {
        return contactService.findAllContacts(page);
    }

    @PostMapping("/add")
    public void addContact(@Valid @RequestBody Contact contact)
    {
        contactService.addContact(contact);
    }
}

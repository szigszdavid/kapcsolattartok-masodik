package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public List<GetAllContactsDTO> findAllContacts(@RequestParam(name = "page", required = false) Integer page)
    {
        return contactService.findAllContacts(page);
    }

    @GetMapping("/{id}")
    public GetContactByIdDTO findContactById(@PathVariable Long id) throws ContactNotFoundExcpetion {
        return contactService.findContactByID(id);
    }

    @PostMapping
    public ContactDTO addContact(@Valid @RequestBody ContactDTO contactDTO)
    {
        return contactService.addContact(contactDTO);
    }

    @PutMapping("/{id}")
    public void updateContact(@Valid @RequestBody ContactDTO contactDTO, @PathVariable Long id) throws ContactNotFoundExcpetion {

        contactService.updateContact(contactDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) throws ContactNotFoundExcpetion {
        contactService.deleteContact(id);
    }




}

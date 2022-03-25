package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.OutputDTO;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public List<OutputDTO> findAllContacts(@RequestParam(name = "page", required = false) Integer page)
    {
        return contactService.findAllContacts(page);
    }

    @GetMapping("/{id}")
    public ContactDTO findContactById(@PathVariable Long id)
    {
        return contactService.findContactByID(id);
    }

    @PostMapping
    public void addContact(@Valid @RequestBody ContactDTO contactDTO)
    {
        contactService.addContact(contactDTO);
    }

    @PutMapping("/{id}")
    public void updateContact(@RequestBody ContactDTO contactDTO, @PathVariable Long id)
    {
        contactService.updateContact(contactDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id)
    {
        contactService.deleteContact(id);
    }




}

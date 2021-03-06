package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public List<GetAllContactsDTO> findAllContacts(@RequestParam(name = "page", required = false) Integer page)
    {
        log.info("findAllContacts on page: " + page + "in ContactController called");
        return contactService.findAllContacts(page);
    }

    @GetMapping("/{id}")
    public GetContactByIdDTO findContactById(@PathVariable Long id) throws ContactNotFoundExcpetion
    {
        log.info("findContactById wih id: " + id + "in ContactController called");
        return contactService.findContactByID(id);
    }

    @PostMapping
    public Long addContact(@Valid @RequestBody ContactDTO contactDTO)
    {
        log.info("addContact in ContactController called");
        return contactService.addContact(contactDTO);
    }

    @PutMapping("/{id}")
    public Long updateContact(@Valid @RequestBody ContactDTO contactDTO, @PathVariable Long id) throws ContactNotFoundExcpetion
    {
        log.info("updateContact with id: " + id + "in ContactController called");
        return contactService.updateContact(contactDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) throws ContactNotFoundExcpetion
    {
        log.info("deleteContact with id: " + id + "Controller called");
        contactService.deleteContact(id);
    }
}

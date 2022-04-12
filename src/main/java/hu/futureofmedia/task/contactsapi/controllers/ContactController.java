package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.util.ArrayList;
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
        log.info("findAllContacts on page: {} called", page);
        return contactService.findAllContacts(page);

    }

    @Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping("/{id}")
    public GetContactByIdDTO findContactById(@PathVariable Long id) throws ContactNotFoundExcpetion
    {
        log.info("findContactById wih id: {} called", id);
        return contactService.findContactByID(id);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public Long addContact(@Valid @RequestBody ContactDTO contactDTO)
    {
        log.info("addContact with ContactDTO: {} called", contactDTO);
        return contactService.addContact(contactDTO);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public Long updateContact(@Valid @RequestBody ContactDTO contactDTO, @PathVariable Long id) throws ContactNotFoundExcpetion
    {
        log.info("updateContact with id: {} and ContactDTO: {} called", id, contactDTO);
        return contactService.updateContact(contactDTO, id);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) throws ContactNotFoundExcpetion
    {
        log.info("deleteContact with id: {} called", id);
        contactService.deleteContact(id);
    }
}

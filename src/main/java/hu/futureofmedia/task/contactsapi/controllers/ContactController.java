package hu.futureofmedia.task.contactsapi.controllers;

import graphql.ExecutionResult;
import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsWithNumberOfContactsDto;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.exceptions.ContactNotFoundExcpetion;
import hu.futureofmedia.task.contactsapi.services.ContactService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Queue;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
@CrossOrigin("http://localhost:4200")
public class ContactController {

    private final ContactService contactService;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public GetAllContactsWithNumberOfContactsDto findAllContacts(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name= "orderby", required= false) String orderBy, @RequestParam(name= "orderway", required= false) String orderWay, @RequestParam(name= "contactsByPage", required= false) Integer size)
    {
        log.info("findAllContacts on page: {} called, sorted by {} and the way is {}", page, orderBy, orderWay);
        return contactService.findAllContacts(page, orderBy, orderWay, size);

    }

    //@Secured("ROLE_LIST")
    @GetMapping("/{id}")
    public GetContactByIdDTO findContactById(@PathVariable Long id) throws ContactNotFoundExcpetion
    {
        log.info("findContactById wih id: {} called", id);
        return contactService.findContactByID(id);
    }

    //@Secured("ROLE_CREATE")
    @PostMapping
    public Long addContact(@Valid @RequestBody ContactDTO contactDTO)
    {
        log.info("addContact with ContactDTO: {} called", contactDTO);

        return contactService.addContact(contactDTO);
    }

    @Secured("ROLE_MODIFY")
    @PutMapping("/{id}")
    public Long updateContact(@Valid @RequestBody ContactDTO contactDTO, @PathVariable Long id) throws ContactNotFoundExcpetion
    {
        log.info("updateContact with id: {} and ContactDTO: {} called", id, contactDTO);
        return contactService.updateContact(contactDTO, id);
    }

    @Secured("ROLE_DELETE")
    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) throws ContactNotFoundExcpetion
    {
        log.info("deleteContact with id: {} called", id);
        contactService.deleteContact(id);
    }

    @GetMapping("/search/name")
    public GetAllContactsWithNumberOfContactsDto findContactsByName(@RequestParam(name = "page", required = false) Integer page,@RequestParam(name = "name", required = false) String name)
    {
        log.info("findAllContacts on page: {} called, name : {}", page, name);
        return contactService.findContactsByName(page, name);
    }

    @GetMapping("/search/company")
    public GetAllContactsWithNumberOfContactsDto findContactsByCompany(@RequestParam(name = "page", required = false) Integer page,@RequestParam(name = "companyId", required = false) String companyId)
    {
        log.info("findAllContacts on page: {} called, company : {}", page, companyId);
        return contactService.findContactsByCompany(page, Long.parseLong(companyId));

    }
}

package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.services.IContactService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/contacts")
public class ContactController {

    IContactService contactService;

    CompanyRepository companyRepository;

    ContactMapper mapper = Mappers.getMapper(ContactMapper.class);

    public ContactController(IContactService contactService, CompanyRepository companyRepository) {
        this.contactService = contactService;
        this.companyRepository = companyRepository;
    }

    @GetMapping
    public Page<Contact> findAllContacts(@RequestParam(name = "page", required = false) Integer page)
    {
        return contactService.findAllContacts(page);
    }

    @GetMapping("/{id}")
    public Optional<Contact> findContactById(@PathVariable Long id)
    {
        return contactService.findContactByID(id);
    }

    @PostMapping("/add")
    public void addContact(@Valid @RequestBody ContactDTO contactDTO)
    {
        Contact contact = mapper.contactDTOToContact(contactDTO);

        contact.setCompany(companyRepository.findCompanyByName(contactDTO.getCompanyName()));
        contact.setStatus(Status.ACTIVE);

        contactService.addContact(contact);
    }

    @PutMapping("/update/{id}")
    public void updateContact(@Valid @RequestBody ContactDTO contactDTO, @PathVariable Long id)
    {
        contactDTO.setId(id);
        Contact contact = mapper.contactDTOToContact(contactDTO);

        contact.setCompany(companyRepository.findCompanyByName(contactDTO.getCompanyName()));

        contactService.updateContact(contact);
    }

    @PutMapping("/delete/{id}")
    public void deleteContact(@Valid @PathVariable Long id)
    {
        Contact contact = contactService.findContactByID(id).orElse(null);

        contact.setStatus(Status.DELETED);

        contactService.updateContact(contact);
    }


}

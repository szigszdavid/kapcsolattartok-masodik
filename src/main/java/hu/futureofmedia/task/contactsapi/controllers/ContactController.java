package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.services.IContactService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("/contacts")
public class ContactController {

    IContactService contactService;

    @Autowired
    CompanyRepository companyRepository;

    ContactMapper mapper = Mappers.getMapper(ContactMapper.class);

    public ContactController(IContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public Page<Contact> findAllContacts(@RequestParam(name = "page", required = false) Integer page)
    {
        return contactService.findAllContacts(page);
    }

    @PostMapping("/add")
    public void addContact(@Valid @RequestBody ContactDTO contactDTO)
    {
        Contact contact = mapper.contactDTOToContact(contactDTO);
        System.out.println(contactDTO.getCompanyName());
        contact.setCompany(companyRepository.findCompanyByName(contactDTO.getCompanyName()));
        System.out.println(companyRepository.findCompanyByName(contactDTO.getCompanyName()));
        System.out.println(companyRepository.findAll());
        contactService.addContact(contact);
    }
}

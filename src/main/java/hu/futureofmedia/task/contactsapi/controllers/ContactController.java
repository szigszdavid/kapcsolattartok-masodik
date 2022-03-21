package hu.futureofmedia.task.contactsapi.controllers;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.OutputDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.mapper.ContactMapper;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.services.IContactService;
import org.hibernate.validator.constraints.CodePointLength;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

import javax.validation.Valid;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<OutputDTO> findAllContacts(@RequestParam(name = "page", required = false) Integer page)
    {
        //return contactService.findAllContacts(page);
        Page<Contact> contacts = contactService.findAllContacts(page);

        return listTheSpecificFieldsWithDTO(contacts);
    }

    @GetMapping("/{id}")
    public Optional<Contact> findContactById(@PathVariable Long id)
    {
        return contactService.findContactByID(id);
    }

    @PostMapping("/add")
    public void addContact(@RequestBody ContactDTO contactDTO)
    {
        Contact contact = mapper.contactDTOToContact(contactDTO);

        contact.setCompany(companyRepository.findCompanyByName(contactDTO.getCompanyName()));
        contact.setStatus(Status.ACTIVE);

        contactService.addContact(contact);
    }

    @PutMapping("/update/{id}")
    public void updateContact(@RequestBody ContactDTO contactDTO, @PathVariable Long id)
    {
        contactDTO.setId(id);
        Contact contact = mapper.contactDTOToContact(contactDTO);

        contact.setCompany(companyRepository.findCompanyByName(contactDTO.getCompanyName()));
        contact.setCreatedDate(contactService.findContactByID(id).get().getCreatedDate());

        contactService.updateContact(contact);
    }

    @PutMapping("/delete/{id}")
    public void deleteContact(@PathVariable Long id)
    {
        Contact contact = contactService.findContactByID(id).orElse(null);

        contact.setStatus(Status.DELETED);

        contactService.updateContact(contact);
    }

    public List<OutputDTO> listTheSpecificFieldsWithDTO(Page<Contact> contacts)
    {
        List<Contact> list = contacts.toList();
        List<OutputDTO> outputDTOS = new ArrayList<>();

        Iterator itr = list.iterator();
        while(itr.hasNext()){

            OutputDTO outputDTO = new OutputDTO();
            Object[] obj = (Object[]) itr.next();

            outputDTO.setFullName(String.valueOf(obj[0]));
            outputDTO.setCompanyName(String.valueOf(obj[1]));
            outputDTO.setEmailAddress(String.valueOf(obj[2]));
            outputDTO.setPhoneNumber(String.valueOf(obj[3]));
            outputDTO.setId(Long.valueOf(String.valueOf(obj[4])));

            outputDTOS.add(outputDTO);
        }

        return outputDTOS;
    }


}

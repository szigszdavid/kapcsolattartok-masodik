package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Optional;

@Service
@Validated
public class ContactService implements IContactService {

    ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Page<Contact> findAllContacts(Integer page)
    {
        return contactRepository.findAll(createNewPageable(page));
    }

    @Override
    public void addContact(@Valid Contact contact) {
        contactRepository.save(contact);
    }


    @Override
    public void updateContact(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    public Optional<Contact> findContactByID(Long id) {
        return contactRepository.findById(id);
    }

    @Override
    public Pageable createNewPageable(Integer page)
    {
        return PageRequest.of(page == null ? 0 : page,10, Sort.by("firstName").and(Sort.by("lastName")));
    }
}

package hu.futureofmedia.task.contactsapi.mapper;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ContactMapper {

    @BeforeMapping
    default void contactDTOToContactBeforeMapping(ContactDTO dto, @MappingTarget Contact contact) {

        contact.setPhoneNumber(dto.getPhoneNumber());
    }

    Contact contactDTOToContact(ContactDTO dto);

    ContactDTO contactDTOToContactDTO(ContactDTO dto);

    void updateContactWithMapper(ContactDTO dto, @MappingTarget Contact contact);

    void updateContactDTOWithMapper(ContactDTO dto, @MappingTarget GetContactByIdDTO foundDTO);

    ContactDTO contactToContactDTO(Contact contact);

    GetContactByIdDTO contactToGetContactByDTO(Contact contact);

    Contact getContactByIdDTOToContact(GetContactByIdDTO findByIdDTO);

    GetAllContactsDTO contactToGetAllContactsDTO(Contact contact);
}

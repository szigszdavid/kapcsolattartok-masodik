package hu.futureofmedia.task.contactsapi.mapper;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetAllContactsDTO;
import hu.futureofmedia.task.contactsapi.dtos.GetContactByIdDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface ContactMapper {

    Contact contactDTOToContact(ContactDTO dto);

    void updateContactWithMapper(ContactDTO dto, @MappingTarget Contact contact);

    GetContactByIdDTO contactToGetContactByDTO(Contact contact);

    GetAllContactsDTO contactToGetAllContactsDTO(Contact contact);
}

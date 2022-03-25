package hu.futureofmedia.task.contactsapi.mapper;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.dtos.OutputDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import org.mapstruct.*;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.mapstruct.factory.Mappers;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    @BeforeMapping
    default void contactDTOToContactBeforeMapping(ContactDTO dto, @MappingTarget Contact contact) {

        contact.setPhoneNumber(dto.getPhoneNumber());
    }

    Contact contactDTOToContact(ContactDTO dto);

    ContactDTO contactDTOToContactDTO(ContactDTO dto);

    void updateContactWithMapper(ContactDTO dto, @MappingTarget Contact contact);

    OutputDTO contactToOutputDTO(Contact contact);

    ContactDTO contactToContactDTO(Contact contact);
}

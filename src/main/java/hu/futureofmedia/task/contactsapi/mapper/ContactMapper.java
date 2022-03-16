package hu.futureofmedia.task.contactsapi.mapper;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

@Mapper
public abstract class ContactMapper {

    @BeforeMapping
    protected void enrichDTOWithFuelType(ContactDTO dto, @MappingTarget Contact contact) {
        if (dto.getStatus() == "ACTIVE") {
            contact.setStatus(Status.ACTIVE);
        }
        if (dto.getStatus() == "DELETED") {
            contact.setStatus(Status.DELETED);
        }

        String contactDTOPhoneNumberString = dto.getPhoneNumber();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {

            Phonenumber.PhoneNumber contactDTOPhoneNumberProto = phoneUtil.parse(contactDTOPhoneNumberString, "HU");
            boolean isValid = phoneUtil.isValidNumber(contactDTOPhoneNumberProto);
            if(isValid)
            {
                dto.setPhoneNumber(phoneUtil.format(contactDTOPhoneNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164));
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }

    /*
    @AfterMapping
    protected void convertNameToUpperCase(ContactDTO dto, @MappingTarget Contact contact) {
        contact.setCompany(companyRepository.findByName(dto.getCompanyName()));
    }
        */
    public abstract Contact contactDTOToContact(ContactDTO dto);

    //ContactDTO contactToContactDTO(Contact contact);
}

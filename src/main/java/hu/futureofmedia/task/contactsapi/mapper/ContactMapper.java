package hu.futureofmedia.task.contactsapi.mapper;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import org.mapstruct.*;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

@Mapper
public abstract class ContactMapper {

    @BeforeMapping
    protected void contactDTOToContactBeforeMapping(ContactDTO dto, @MappingTarget Contact contact) {
        if (dto.getStatus().equals("ACTIVE")) {
            contact.setStatus(Status.ACTIVE);
        }
        if (dto.getStatus().equals("DELETED")) {
            contact.setStatus(Status.DELETED);
        }

        String contactDTOPhoneNumberString = dto.getPhoneNumber();
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {

            Phonenumber.PhoneNumber contactDTOPhoneNumberProto = phoneUtil.parse(contactDTOPhoneNumberString, "HU");
            boolean isValid = phoneUtil.isValidNumber(contactDTOPhoneNumberProto);

            if(isValid  && contactDTOPhoneNumberProto.getCountryCode() == 36 && String.valueOf(contactDTOPhoneNumberProto.getNationalNumber()).length() == 9)
            {
                contact.setPhoneNumber(phoneUtil.format(contactDTOPhoneNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164));
            }
            else
            {
                throw new NumberFormatException("Not a valid phone number");
            }
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e);
        }

    }

    public abstract Contact contactDTOToContact(ContactDTO dto);

}

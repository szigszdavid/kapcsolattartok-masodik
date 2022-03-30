package hu.futureofmedia.task.contactsapi.annotations;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation, String> {
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {

        String contactDTOPhoneNumberString = phoneNumber;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        try {

            Phonenumber.PhoneNumber contactDTOPhoneNumberProto = phoneUtil.parse(contactDTOPhoneNumberString, "HU");
            boolean isValid = phoneUtil.isValidNumber(contactDTOPhoneNumberProto);

            return isValid ;

        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e);
        }

        return false;
    }
}

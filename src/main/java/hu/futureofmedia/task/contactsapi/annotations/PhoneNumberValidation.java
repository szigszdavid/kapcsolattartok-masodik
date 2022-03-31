package hu.futureofmedia.task.contactsapi.annotations;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumberValidation {

    public String message() default "Contact.PhoneNumber wrong format";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}

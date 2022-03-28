package hu.futureofmedia.task.contactsapi.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusExistsValidator.class)
public @interface StatusExistsValidation {

    public String message() default "Contact.Status does not exists";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}

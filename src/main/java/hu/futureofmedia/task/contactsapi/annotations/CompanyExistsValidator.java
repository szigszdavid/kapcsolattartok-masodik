package hu.futureofmedia.task.contactsapi.annotations;

import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.services.CompanyService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class CompanyExistsValidator implements ConstraintValidator<CompanyExistsValidation, Long>  {

    private final CompanyService companyService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Company company = companyService.findById(value);

        return company != null;
    }
}

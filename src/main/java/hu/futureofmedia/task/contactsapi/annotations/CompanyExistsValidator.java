package hu.futureofmedia.task.contactsapi.annotations;

import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.services.CompanyService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class CompanyExistsValidator implements ConstraintValidator<CompanyExistsValidation, Company>  {

    private final CompanyService companyService;

    @Override
    public boolean isValid(Company value, ConstraintValidatorContext context) {
        Company company = companyService.findById(value.getId());

        return company != null && company.getName() == value.getName();
    }
}

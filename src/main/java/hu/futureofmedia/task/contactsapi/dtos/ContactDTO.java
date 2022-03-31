package hu.futureofmedia.task.contactsapi.dtos;

import hu.futureofmedia.task.contactsapi.annotations.CompanyExistsValidation;
import hu.futureofmedia.task.contactsapi.annotations.PhoneNumberValidation;
import hu.futureofmedia.task.contactsapi.entities.Company;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    private Long id;

    @NotBlank(message = "ContactDTO.firstName.Required")
    private String firstName;

    @NotBlank(message = "ContactDTO.lastName.Required")
    private String lastName;

    @NotBlank(message = "ContactDTO.emailAddress.Required")
    @Email(message = "ContactDTO.emailAddress.Email format required")
    private String emailAddress;

    @PhoneNumberValidation
    private String phoneNumber;

    private String comment;

    @NotNull(message = "ContactDTO.company.Required")
    @CompanyExistsValidation
    private Long companyId;

}

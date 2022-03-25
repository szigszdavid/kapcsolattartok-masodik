package hu.futureofmedia.task.contactsapi.dtos;

import hu.futureofmedia.task.contactsapi.annotations.CompanyExistsValidation;
import hu.futureofmedia.task.contactsapi.annotations.PhoneNumberValidation;
import hu.futureofmedia.task.contactsapi.entities.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    private Long id;

    @NotBlank(message = "ContactDTO.firstName.Required")
    private String firstName;

    @NotBlank(message = "ContactDTO.lastName.Required")
    private String lastName;

    @NotBlank(message = "ContactDTO.emailAddress.Required")
    private String emailAddress;

    @PhoneNumberValidation
    private String phoneNumber;

    @NotBlank(message = "ContactDTO.status.Required")
    private String status;

    private String comment;

    @NotNull(message = "ContactDTO.company.Required")
    @CompanyExistsValidation
    private Company company;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModifiedDate;


}

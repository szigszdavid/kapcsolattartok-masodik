package hu.futureofmedia.task.contactsapi.dtos;

import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    private Long id;

    @NotNull(message = "Vezetéknév megadása kötelező")
    @NotBlank(message = "Vezetéknév nem lehet üres")
    private String firstName;

    @NotNull(message = "Lastname can not be null")
    @NotBlank(message = "Lastname can not be blank")
    private String lastName;

    @NotNull(message = "Email address can not be null")
    @NotBlank(message = "Email address can not be blank")
    private String emailAddress;

    private String phoneNumber;

    @NotNull(message = "Status can not be null")
    @NotBlank(message = "Status can not be blank")
    private String status;

    private String comment;

    @NotNull(message = "Company can not be null")
    @NotBlank(message = "Company can not be blank")
    private String companyName;

    private Date createdDate;

    private Date lastModifiedDate;


}

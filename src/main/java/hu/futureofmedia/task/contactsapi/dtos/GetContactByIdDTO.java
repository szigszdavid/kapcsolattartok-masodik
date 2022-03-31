package hu.futureofmedia.task.contactsapi.dtos;

import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.entities.Status;
import lombok.*;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetContactByIdDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String phoneNumber;

    private Status status;

    private String comment;

    private CompanyDTO company;

    private ZonedDateTime createdDate;

    private ZonedDateTime lastModifiedDate;

}

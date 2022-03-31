package hu.futureofmedia.task.contactsapi.dtos;

import hu.futureofmedia.task.contactsapi.entities.Company;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllContactsDTO {

    private Long id;

    private String emailAddress;

    private String phoneNumber;

    private Company company;

    private String fullName;
}

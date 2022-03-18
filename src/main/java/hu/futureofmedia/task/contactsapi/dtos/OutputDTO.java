package hu.futureofmedia.task.contactsapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OutputDTO {

    private Long id;

    private String emailAddress;

    private String phoneNumber;

    private String companyName;

    private String fullName;
}

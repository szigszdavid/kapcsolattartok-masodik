package hu.futureofmedia.task.contactsapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotNull(message = "Keresztnév megadása kötelező")
    @NotBlank(message = "Keresztnév nem lehet üres")
    private String lastName;

    @NotNull(message = "E-mail cím megadása kötelező")
    @NotBlank(message = "E-mail cím nem lehet üres")
    private String emailAddress;

    private String phoneNumber;

    @NotNull(message = "Nem létező státusz lett beállítva, válasszon egyet a következőek közül: ACTIVE, DELETED")
    @NotBlank(message = "Nem létező státusz lett beállítva, válasszon egyet a következőek közül: ACTIVE, DELETED")
    private String status;

    private String comment;

    @NotNull(message = "A cég kiváalsztása kötelező")
    private String companyName;

    private Date createdDate;

    private Date lastModifiedDate;


}

package hu.futureofmedia.task.contactsapi.dtos;

import hu.futureofmedia.task.contactsapi.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank
    @Email
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String fullName;

    @ElementCollection
    private Set<Role> authorities = new HashSet<>();
}

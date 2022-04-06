package hu.futureofmedia.task.contactsapi.dtos;

import hu.futureofmedia.task.contactsapi.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "CreateUserRequest.username.Required")
    @Email
    private String username;

    @NotBlank(message = "CreateUserRequest.password.Required")
    private String password;

    @NotBlank(message = "CreateUserRequest.fullName.Required")
    private String fullName;

    @ToString.Exclude
    @NotEmpty(message = "CreateUserRequest.roles.Required")
    private Collection<Role> authorities = new ArrayList<>();
}

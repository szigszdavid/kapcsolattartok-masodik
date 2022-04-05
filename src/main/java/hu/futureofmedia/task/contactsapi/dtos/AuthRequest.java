package hu.futureofmedia.task.contactsapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest
{
    @NotBlank
    @Email
    private String username;

    @NotBlank
    private String password;
}
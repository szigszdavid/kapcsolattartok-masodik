package hu.futureofmedia.task.contactsapi.dtos;

import hu.futureofmedia.task.contactsapi.domain.Privilege;
import hu.futureofmedia.task.contactsapi.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserByUsernameDto {

        private LocalDateTime createdDate;

        private LocalDateTime lastModifiedDate;

        private String username;

        private String password;

        private String fullName;

        private Set<Privilege> privileges = new HashSet<>();


}

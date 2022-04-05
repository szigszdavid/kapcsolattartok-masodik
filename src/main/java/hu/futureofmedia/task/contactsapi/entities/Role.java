package hu.futureofmedia.task.contactsapi.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

    public static final String LIST = "LIST";
    public static final String CREATE = "CREATE";
    public static final String MODIFY = "MODIFY";
    public static final String DELETE = "DELETE";


    private String authority;
}

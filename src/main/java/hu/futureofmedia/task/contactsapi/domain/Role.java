package hu.futureofmedia.task.contactsapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotBlank
    private String name;

}
/*
public class Role implements GrantedAuthority {

    public static final String LIST = "LIST";
    public static final String CREATE = "CREATE";
    public static final String MODIFY = "MODIFY";
    public static final String DELETE = "DELETE";


    private String authority;
}*/

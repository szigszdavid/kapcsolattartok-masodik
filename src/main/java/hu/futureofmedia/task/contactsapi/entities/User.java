package hu.futureofmedia.task.contactsapi.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class User extends ComparableEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedAt;

    @Column
    private boolean enabled = true;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String fullName;

    @Column
    @ElementCollection
    private Set<Role> authorities = new HashSet<>();

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

}
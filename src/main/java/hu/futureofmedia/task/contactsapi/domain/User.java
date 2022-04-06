package hu.futureofmedia.task.contactsapi.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static javax.persistence.GenerationType.AUTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "my_user")
public class User /*extends ComparableEntity implements UserDetails*/ {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @CreatedDate
    @Column
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column
    private LocalDateTime lastModifiedDate;

    @Column
    private boolean enabled = true;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String fullName;

    @Column(name = "authorities")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Collection<Role> authorities = new ArrayList<>();


    /*
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
    */

}
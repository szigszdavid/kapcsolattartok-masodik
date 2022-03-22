package hu.futureofmedia.task.contactsapi.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    @NotNull(message = "Vezetéknév megadása kötelező")
    @NotBlank(message = "Vezetéknév nem lehet üres")
    private String firstName;

    @Column
    @NotNull(message = "Keresztnév megadása kötelező")
    @NotBlank(message = "Keresztnév nem lehet üres")
    private String lastName;

    @Column
    @Email
    @NotNull(message = "E-mail cím megadása kötelező")
    @NotBlank(message = "E-mail cím nem lehet üres")
    private String emailAddress;

    @Column
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "company_id")
    @NotNull(message = "A cég kiváalsztása kötelező")
    private Company company;

    @Column
    private String comment;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Nem létező státusz lett beállítva, válasszon egyet a következőek közül: ACTIVE, DELETED")
    private Status status;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date createdDate = new Date(System.currentTimeMillis());

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date lastModifiedDate = new Date(System.currentTimeMillis());

    @PrePersist
    private void setCreatedDatePrePersist()
    {
        createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    private void setLastModifiedDatePreUpdate()
    {
        lastModifiedDate = new Date(System.currentTimeMillis());
    }

}

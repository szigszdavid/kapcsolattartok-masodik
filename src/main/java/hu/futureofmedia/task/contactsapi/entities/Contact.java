package hu.futureofmedia.task.contactsapi.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotNull(message = "Lastname can not be null")
    @NotBlank(message = "Lastname can not be blank")
    private String lastName;

    @Column
    @Email
    @NotNull(message = "Email address can not be null")
    @NotBlank(message = "Email address can not be blank")
    private String emailAddress;

    @Column
    @NotBlank(message = "Phone number can not be blank")
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "company_id")
    @NotNull(message = "Company can not be null")
    private Company company;

    @Column
    private String comment;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status can not be null")
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

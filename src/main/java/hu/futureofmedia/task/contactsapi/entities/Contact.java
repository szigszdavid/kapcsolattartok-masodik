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

    @Column(nullable = false)
    @NotBlank
    private String firstName;

    @Column(nullable = false)
    @NotBlank
    private String lastName;

    @Column(nullable = false)
    @Email
    @NotBlank
    private String emailAddress;

    @Column
    @NotBlank
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "company_id")
    @NotNull
    private Company company;

    @Column
    private String comment;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    @Column
    private Date createdDate = new Date(System.currentTimeMillis());

    @LastModifiedDate
    @Temporal(TemporalType.DATE)
    @Column
    private Date lastModifiedDate = new Date(System.currentTimeMillis());

    @Column
    private String fullName;

}

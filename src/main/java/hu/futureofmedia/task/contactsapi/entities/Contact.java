package hu.futureofmedia.task.contactsapi.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
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
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @Email
    private String emailAddress;

    @Column
    private String phoneNumber;

    @Column(nullable = false)
    private Status status;

    @Column
    private String comment;

    @OneToOne
    private Company company;

    @Column
    private Date createdDate;

    @Column
    private Date lastModifiedDate;
}

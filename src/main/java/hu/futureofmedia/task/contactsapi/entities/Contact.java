package hu.futureofmedia.task.contactsapi.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.ZonedDateTime;

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
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String emailAddress;

    @Column
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @ToString.Exclude
    private Company company;

    @Column
    private String comment;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(columnDefinition = "TIMESTAMP")
    private ZonedDateTime createdDate;

    @Column
    private ZonedDateTime lastModifiedDate;

    @PrePersist
    private void setCreatedDatePrePersist()
    {
        createdDate = ZonedDateTime.now();
    }

    @PreUpdate
    private void setLastModifiedDatePreUpdate()
    {
        lastModifiedDate = ZonedDateTime.now();
    }

    public String getFullName() { return firstName + " " + lastName;}
}

package hu.futureofmedia.task.contactsapi.mapper;

import hu.futureofmedia.task.contactsapi.dtos.ContactDTO;
import hu.futureofmedia.task.contactsapi.entities.Contact;
import hu.futureofmedia.task.contactsapi.entities.Status;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import hu.futureofmedia.task.contactsapi.repositories.ContactRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public abstract class ContactMapper {

    @BeforeMapping
    protected void enrichDTOWithFuelType(ContactDTO dto, @MappingTarget Contact contact) {
        if (dto.getStatus() == "ACTIVE") {
            contact.setStatus(Status.ACTIVE);
        }
        if (dto.getStatus() == "DELETED") {
            contact.setStatus(Status.DELETED);
        }
    }

    /*
    @AfterMapping
    protected void convertNameToUpperCase(ContactDTO dto, @MappingTarget Contact contact) {
        contact.setCompany(companyRepository.findByName(dto.getCompanyName()));
    }
        */
    public abstract Contact contactDTOToContact(ContactDTO dto);

    //ContactDTO contactToContactDTO(Contact contact);
}

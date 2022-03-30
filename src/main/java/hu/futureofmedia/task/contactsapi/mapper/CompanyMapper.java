package hu.futureofmedia.task.contactsapi.mapper;

import hu.futureofmedia.task.contactsapi.dtos.CompanyDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    Company companyDTOToCompany(CompanyDTO dto);

}

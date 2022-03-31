package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.CompanyDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;

public interface CompanyService {

    Company findById(Long id);

    void addCompany(CompanyDTO dto);
}

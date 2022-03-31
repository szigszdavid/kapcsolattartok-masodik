package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.CompanyDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.mapper.CompanyMapper;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImp  implements CompanyService{

    private final CompanyRepository companyRepository;
    private final CompanyMapper mapper;

    @Override
    public Company findById(Long id) {

        log.info("findById in CompanyService called");

        Company company = companyRepository.findById(id).orElse(null);

        log.debug("Company {} found", company);

        return company;
    }

    @Override
    public void addCompany(CompanyDTO dto) {

        log.info("addCompany with CompanyDTO: {}", dto);

        Company company = mapper.companyDTOToCompany(dto);

        log.debug("Company before save: {}", company);

        companyRepository.save(company);

        log.debug("Company saved to database");
    }

}

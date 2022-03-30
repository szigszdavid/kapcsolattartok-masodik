package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.dtos.CompanyDTO;
import hu.futureofmedia.task.contactsapi.entities.Company;
import hu.futureofmedia.task.contactsapi.mapper.CompanyMapper;
import hu.futureofmedia.task.contactsapi.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyServiceImp  implements CompanyService{

    private final CompanyRepository companyRepository;
    private final CompanyMapper mapper;

    @Override
    public Company findById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public void addCompany(CompanyDTO dto) {

        Company company = mapper.companyDTOToCompany(dto);

        companyRepository.save(company);
    }

}

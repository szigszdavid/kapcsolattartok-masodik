package hu.futureofmedia.task.contactsapi.services;

import hu.futureofmedia.task.contactsapi.entities.Company;

import java.util.Optional;

public interface CompanyService {

    Company findById(Long id);
}

package com.liemily.company.service;

import com.liemily.company.domain.Company;
import com.liemily.company.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by Emily Li on 24/09/2017.
 */
@Service
@Lazy
public class CompanyService {
    private CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public void save(Company company) {
        companyRepository.save(company);
    }

    public Collection<Company> save(Collection<Company> companies) {
        return companyRepository.save(companies);
    }
}

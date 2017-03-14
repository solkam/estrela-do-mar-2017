/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.cb.mundo.model.entity.Company;

/**
 * DAO for company
 * @author Lancelot
 */
public class CompanyDAO{

    private DAO<Company> companyDAO;
    private EntityManager manager;

    public CompanyDAO(EntityManager manager) {
        this.manager = manager;
        companyDAO = new JavaxPersistenceDAO<Company>(manager, Company.class);
    }
    
    public Company saveCompany(Company company){
        return companyDAO.save(company);
    }
    
    public void removeCompany(Company company){
        companyDAO.remove(companyDAO.save(company));
    }
    
    public List<Company> searchAll(){
        return companyDAO.searchAll();
    }
    
    public List<Company> searchByIdentitySerie(String identitySerie){
        return manager.createNamedQuery("Company.searchByIdentitySerie", Company.class)
                .setParameter("cIdentitySerie", "%"+identitySerie+"%")
                .getResultList();
    }
    
    public List<Company> searchByName(String name){
        List<Company> companies = new ArrayList<Company>();
        if (name!=null && !name.isEmpty()) {
            companies =  manager.createNamedQuery("Company.searchByName", Company.class)
                    .setParameter("cName", "%"+name+"%")
                    .getResultList();
                    }
        return companies;
    }

    public Company findCompanyById(Long id) {
        return companyDAO.findById(id);
    }
    
}

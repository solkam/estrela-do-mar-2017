/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.service;

import com.cb.mundo.model.dao.CompanyDAO;
import com.cb.mundo.model.entity.Company;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Service Bean for company
 * @author Lancelot
 */
@Stateless
public class CompanyService {
    @PersistenceContext
    private EntityManager manager;
    
    private CompanyDAO companyDAO;
    
    @PostConstruct
    protected void init() {
        this.companyDAO = new CompanyDAO(manager);
    }
    
    public Company saveCompany(Company company){
        return companyDAO.saveCompany(company);
    }
    
    public void removeCompany(Company company){
        companyDAO.removeCompany(company);
    }
    
    public Company findCompanyById(Long id){
        return companyDAO.findCompanyById(id);
    }
    
    public List<Company> searchAll(){
        return companyDAO.searchAll();
    }
    
    public List<Company> searchByIdentitySerie(String identitySerie){
        return companyDAO.searchByIdentitySerie(identitySerie);
    }
    
    public List<Company> searchByName(String name){
        return companyDAO.searchByName(name);
    }
}

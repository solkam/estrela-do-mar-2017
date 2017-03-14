/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.controller.converter;

import com.cb.mundo.model.entity.Company;
import com.cb.mundo.model.service.CompanyService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Lancelot
 */
@ManagedBean
public class CompanyConverter implements Converter{
    
    @EJB CompanyService companyService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value==null || value.trim().isEmpty()) {
                return null;
        }
        Long id = Long.parseLong(value);
        Company company = companyService.findCompanyById(id);
        return company;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Company company = (Company) value;
        if (value==null || company.getId()==null) {
            return null;
        }
        return company.getId().toString();
    }
}

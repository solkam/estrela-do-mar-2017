/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.controller.converter;

import com.cb.mundo.model.entity.InscriptionType;
import com.cb.mundo.model.service.MasterTypeService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Lancelot
 */
@ManagedBean(name="inscriptionTypeConverter")
public class InscriptionTypeConverter implements Converter{

    @EJB MasterTypeService masterTypeService;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
                return null;
        }
        Long id = Long.parseLong( value );
        InscriptionType inscriptionType = new InscriptionType();
        inscriptionType.setId(id);
        inscriptionType  = masterTypeService.findById(inscriptionType);
        return inscriptionType;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        InscriptionType inscriptionType = (InscriptionType) value;
        if (value==null || inscriptionType.getId()==null) {
                return null;
        }
        return inscriptionType.getId().toString();
    }
    
}

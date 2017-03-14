/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.controller.converter;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.CreditCardType;
import com.cb.mundo.model.service.MasterTypeService;

/**
 *
 * @author Lancelot
 */
@ManagedBean(name="creditCardTypeConverter")
public class CreditCardTypeConverter implements Converter{

    @EJB MasterTypeService masterTypeService;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty()) {
                return null;
        }
        Long id = Long.parseLong( value );
        CreditCardType creditCardType = new CreditCardType();
        creditCardType.setId(id);
        creditCardType  = masterTypeService.findById(creditCardType);
        return creditCardType;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        CreditCardType creditCardType = (CreditCardType) value;
        if (value==null || creditCardType.getId()==null) {
                return null;
        }
        return creditCardType.getId().toString();
    }
    
}

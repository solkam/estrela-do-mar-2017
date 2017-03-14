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

import com.cb.mundo.model.entity.PaymentCurrency;
import com.cb.mundo.model.service.RegisterService;

/**
 * Converter para Payment Currency
 * @author Anthony
 */
@ManagedBean
public class PaymentCurrencyConverter implements Converter{

    @EJB RegisterService registerService;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value==null||value.isEmpty()){
            return null;
        }
        PaymentCurrency pc = registerService.findPaymentCurrencyById(Long.parseLong(value));
        return pc;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value==null) {
            return null;
        }
        PaymentCurrency pc = (PaymentCurrency) value;
        return pc.getId().toString();
    }
    
}

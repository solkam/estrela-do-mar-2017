/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.controller.converter;

import com.cb.mundo.model.entity.enumeration.PaymentType;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Lancelot
 */

@ManagedBean(name="paymentTypeConverter")
public class PaymentTypeConverter implements Converter{
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value==null || value.trim().isEmpty()) {
                return null;
        }
        PaymentType type = null;
        for (int i = 0; i < PaymentType.values().length; i++) {
            if(PaymentType.values()[i].name().equals(value)){
                type = PaymentType.values()[i];
                break;
            }
        }
        return type;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value==null) {
                return "";
        }
        PaymentType status = (PaymentType) value;
        return status.name();
    }

}

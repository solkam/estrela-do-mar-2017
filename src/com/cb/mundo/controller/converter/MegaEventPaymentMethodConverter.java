/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.controller.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;

/**
 *
 * @author Lancelot
 */
@ManagedBean(name="paymentMethodConverter")
public class MegaEventPaymentMethodConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value==null || value.trim().isEmpty()) {
                return null;
        }
        MegaEventPaymentMethod method = null;
        for (int i = 0; i < MegaEventPaymentMethod.values().length; i++) {
            if(MegaEventPaymentMethod.values()[i].name().equals(value)){
                method = MegaEventPaymentMethod.values()[i];
                break;
            }
        }
        return method;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value==null) {
            return "";
        }
        MegaEventPaymentMethod method = (MegaEventPaymentMethod) value;
        return method.name();
    }
    
}

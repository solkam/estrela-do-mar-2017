/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.controller.converter;

import com.cb.mundo.model.entity.enumeration.AccountReceivableStatus;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author Lancelot
 */
@ManagedBean(name="paymentStatusConverter")
public class PaymentStatusConverter implements Converter {
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value==null || value.trim().isEmpty()) {
                return null;
        }
        AccountReceivableStatus status = null;
        for (int i = 0; i < AccountReceivableStatus.values().length; i++) {
            if(AccountReceivableStatus.values()[i].name().equals(value)){
                status = AccountReceivableStatus.values()[i];
                break;
            }
        }
        return status;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        
        if (value==null) {
                return "";
        }
        AccountReceivableStatus status = (AccountReceivableStatus) value;
        return status.name();
    }
    
}

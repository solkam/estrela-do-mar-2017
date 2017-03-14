/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.searchfilter;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;
import com.cb.mundo.model.entity.enumeration.PaymentStatus;
import com.cb.mundo.model.entity.enumeration.PaymentType;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Lancelot
 */
public class AccountReceivableFilter {
    private static final long serialVersionUID = 4090031413603515280L;
    
    private Contact contact;
    private MegaEvent megaEvent;
    private List<Event> eventsList;
    private List<PaymentType> paymentTypeList;
    private List<PaymentStatus> paymentStatusList;
    private List<MegaEventPaymentMethod> paymentMethodList;
    private Date beginPaymentDate;
    private Date endPaymentDate;

    public AccountReceivableFilter() {
        this.contact = new Contact();
        this.megaEvent = new MegaEvent();
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public MegaEvent getMegaEvent() {
        return megaEvent;
    }

    public void setMegaEvent(MegaEvent megaEvent) {
        this.megaEvent = megaEvent;
    }

    public List<Event> getEventsList() {
        return eventsList;
    }

    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
    }

    public List<PaymentType> getPaymentTypeList() {
        return paymentTypeList;
    }

    public void setPaymentTypeList(List<PaymentType> paymentTypeList) {
        this.paymentTypeList = paymentTypeList;
    }

    public List<PaymentStatus> getPaymentStatusList() {
        return paymentStatusList;
    }

    public void setPaymentStatusList(List<PaymentStatus> paymentStatusList) {
        this.paymentStatusList = paymentStatusList;
    }

    public List<MegaEventPaymentMethod> getPaymentMethodList() {
        return paymentMethodList;
    }

    public void setPaymentMethodList(List<MegaEventPaymentMethod> paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }

    
    public Date getBeginPaymentDate() {
        return beginPaymentDate;
    }

    public void setBeginPaymentDate(Date beginPaymentDate) {
        this.beginPaymentDate = beginPaymentDate;
    }

    public Date getEndPaymentDate() {
        return endPaymentDate;
    }

    public void setEndPaymentDate(Date endPaymentDate) {
        this.endPaymentDate = endPaymentDate;
    }
}

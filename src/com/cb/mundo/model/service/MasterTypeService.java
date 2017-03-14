/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.service;

import com.cb.mundo.model.dao.MasterTypeDAO;
import com.cb.mundo.model.entity.CreditCardType;
import com.cb.mundo.model.entity.InscriptionType;
import com.cb.mundo.model.entity.PaymentCurrency;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Lancelot
 */
@Stateless
public class MasterTypeService {
    
    @PersistenceContext 
    private EntityManager manager;
    
    
    private MasterTypeDAO masterTypeDAO;
    
    @PostConstruct void init() {
        masterTypeDAO = new MasterTypeDAO(manager);
    }
    
    
    public CreditCardType save(CreditCardType creditCardType){
        return this.masterTypeDAO.save(creditCardType);
    }
    
    public void remove(CreditCardType creditCardType){
        this.masterTypeDAO.remove(creditCardType);
    }
    
    public CreditCardType findById(CreditCardType creditCardType){
        return this.masterTypeDAO.findById(creditCardType);
    }
    
    public List<CreditCardType> listAllCreditCardType(){
        return this.masterTypeDAO.listAllCreditCardType();
    }
    
    
    public InscriptionType save(InscriptionType inscriptionType){
        return this.masterTypeDAO.save(inscriptionType);
    }
    
    public void remove(InscriptionType inscriptionType){
        this.masterTypeDAO.remove(inscriptionType);
    }
    
    public InscriptionType findById(InscriptionType inscriptionType){
        return this.masterTypeDAO.findById(inscriptionType);
    }
    
    public List<InscriptionType> listAllInscriptionTypes(){
        return this.masterTypeDAO.listAllInscriptionTypes();
    }
    
    
    public PaymentCurrency save(PaymentCurrency paymentCurrency){
        return this.masterTypeDAO.save(paymentCurrency);
    }
    
    public void remove(PaymentCurrency paymentCurrency){
        this.masterTypeDAO.remove(paymentCurrency);
    }
    
    public PaymentCurrency findById(PaymentCurrency paymentCurrency){
        return this.masterTypeDAO.findById(paymentCurrency);
    }
    
    public List<PaymentCurrency> listAllPaymentCurrency(){
        return this.masterTypeDAO.listAllPaymentCurrency();
    }
    
    
}

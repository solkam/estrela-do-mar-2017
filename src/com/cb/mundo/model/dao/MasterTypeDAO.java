/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.dao;

import com.cb.mundo.model.entity.CreditCardType;
import com.cb.mundo.model.entity.InscriptionType;
import com.cb.mundo.model.entity.PaymentCurrency;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

/**
 *
 * @author Lancelot
 */
public class MasterTypeDAO {
	
    private EntityManager manager;
    
    private DAO<CreditCardType> creditCardDAO;
    private DAO<PaymentCurrency> paymentCurrencyDAO;
    private DAO<InscriptionType> inscriptionTypeDAO;

    public MasterTypeDAO(EntityManager manager) {
        this.manager = manager;
        this.creditCardDAO = new JavaxPersistenceDAO<CreditCardType>(manager, CreditCardType.class);
        this.paymentCurrencyDAO = new JavaxPersistenceDAO<PaymentCurrency>(manager, PaymentCurrency.class);
        this.inscriptionTypeDAO = new JavaxPersistenceDAO<InscriptionType>(manager, InscriptionType.class);
    }
    
    
    public CreditCardType save(CreditCardType creditCardType){
        return this.creditCardDAO.save(creditCardType);
    }
    
    public void remove(CreditCardType creditCardType){
        this.creditCardDAO.remove(creditCardType);
    }
    
    public CreditCardType findById(CreditCardType creditCardType){
        return this.creditCardDAO.findById(creditCardType.getId());
    }
    
    public List<CreditCardType> listAllCreditCardType(){
        return this.creditCardDAO.searchAll();
    }
    
    
    public InscriptionType save(InscriptionType inscriptionType){
        return this.inscriptionTypeDAO.save(inscriptionType);
    }
    
    public void remove(InscriptionType inscriptionType){
        this.inscriptionTypeDAO.remove(inscriptionType);
    }
    
    public InscriptionType findById(InscriptionType inscriptionType){
        return this.inscriptionTypeDAO.findById(inscriptionType.getId());
    }
    
    public List<InscriptionType> listAllInscriptionTypes(){
        return this.inscriptionTypeDAO.searchAll();
    }
    
    
    public PaymentCurrency save(PaymentCurrency paymentCurrency){
        return this.paymentCurrencyDAO.save(paymentCurrency);
    }
    
    public void remove(PaymentCurrency paymentCurrency){
        this.paymentCurrencyDAO.remove(paymentCurrency);
    }
    
    public PaymentCurrency findById(PaymentCurrency paymentCurrency){
        return this.paymentCurrencyDAO.findById(paymentCurrency.getId());
    }
    
    public List<PaymentCurrency> listAllPaymentCurrency(){
        return this.paymentCurrencyDAO.searchAll();
    }
    
    
}

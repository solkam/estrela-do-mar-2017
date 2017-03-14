/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.service;

import com.cb.mundo.model.dao.AccountReceivableDAO;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.RegisterDetailAccountReceivable;
import com.cb.mundo.model.entity.RegisterDetailAccountReceivableDetail;
import com.cb.mundo.model.entity.RegisterDetailAccountReceivableObservation;
import com.cb.mundo.model.searchfilter.AccountReceivableFilter;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class AccountReceivableService {
	
    @PersistenceContext 
    private EntityManager manager;
    
    private AccountReceivableDAO accountReceivableDAO;
	
    @PostConstruct void init() {
	accountReceivableDAO = new AccountReceivableDAO(manager);
    }
    
    public RegisterDetailAccountReceivable save(RegisterDetailAccountReceivable registerAccountReceivable){
        return accountReceivableDAO.save(registerAccountReceivable);
    }
    
    public void remove(RegisterDetailAccountReceivable registerAccountReceivable){
        accountReceivableDAO.remove(registerAccountReceivable);
    }
    
    public List<Register> listRegisters(MegaEvent megaEvent){
        return accountReceivableDAO.listRegisters(megaEvent);
    }
    
    
    public RegisterDetailAccountReceivableDetail save(RegisterDetailAccountReceivableDetail registerAccountReceivableDetail){
        return accountReceivableDAO.save(registerAccountReceivableDetail);
    }
    
    public void remove(RegisterDetailAccountReceivableDetail registerAccountReceivableDetail){
        accountReceivableDAO.remove(registerAccountReceivableDetail);
    }
    
    public List<RegisterDetailAccountReceivableDetail> listDetailsForAccount(RegisterDetailAccountReceivable registerAccountReceivable){
        return accountReceivableDAO.listDetailsForAccount(registerAccountReceivable);
    }
    
    
    public List<RegisterDetailAccountReceivable> searchUsingFilter(AccountReceivableFilter filter){
        return accountReceivableDAO.searchUsingFilter(filter);
    }
    
    public int[] calculateMaximumAmountOfFee(MegaEvent megaEvent){
        GregorianCalendar nowDate = new GregorianCalendar();  
        GregorianCalendar eventDate = new GregorianCalendar();   
        eventDate.setTimeInMillis(megaEvent.getBeginDate().getTime());
        
//        eventDate.set(GregorianCalendar.MONTH, eventDate.get(GregorianCalendar.MONTH)-1);
//        int result = nowDate.get(GregorianCalendar.MONTH) - eventDate.get(GregorianCalendar.MONTH);  
        int m1 = nowDate.get(GregorianCalendar.YEAR) * 12 + nowDate.get(GregorianCalendar.MONTH) ;
        int m2 = eventDate.get(GregorianCalendar.YEAR) * 12 + eventDate.get(GregorianCalendar.MONTH) ;
        int result = m2 - m1 - 1;
        
        if(result < 0){
            result = result * -1;
        }
        
        List<Integer> list = new ArrayList<Integer>();
        if(result == 0){
            list.add(0);
        }else{
            for (int i = 0; i < result; i++) {
                if(i < 12){
                    list.add(i+1);
                }else{
                    break;
                }
            }
        }
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
    
    public RegisterDetailAccountReceivableObservation save(RegisterDetailAccountReceivableObservation accountObservation){
        return accountReceivableDAO.save(accountObservation);
    }
    
    public void remove(RegisterDetailAccountReceivableObservation accountObservation){
        accountReceivableDAO.remove(accountObservation);
    }
    
    public List<RegisterDetailAccountReceivableObservation> listAccountObservations(RegisterDetailAccountReceivable registerAccountReceivable){
        return accountReceivableDAO.listAccountObservations(registerAccountReceivable);
    }
    
    public List<Register> listRegistersWithNoPaidStatusToSendEmail(Date upToProjectedPaymentDate, MegaEvent megaEvent){
        List<RegisterDetailAccountReceivableDetail> rdadList = accountReceivableDAO.listAccountDetailExcludingPaid(upToProjectedPaymentDate, megaEvent);
        
        if(rdadList != null && !rdadList.isEmpty()){
            List<RegisterDetailAccountReceivable> rdaList = new ArrayList<RegisterDetailAccountReceivable>();
            RegisterDetailAccountReceivable rda = null;
            for (int i = 0; i < rdadList.size(); i++) {
                if(rda == null || !rda.equals(rdadList.get(i).getAccountReceivable())
                        ){
                    if(rda != null){
                        rdaList.add(rda);
                    }
                    rda = rdadList.get(i).getAccountReceivable();
                    rda.setAccountReceivableDetailList(new ArrayList<RegisterDetailAccountReceivableDetail>());
                }
                rda.getRegisterDetail().setAccountReceivable(rda);
                rda.getAccountReceivableDetailList().add(rdadList.get(i));
            }
            if(rda != null){
                rdaList.add(rda);
            }

            List<Register> rList = new ArrayList<Register>();
            Register r = null;
            for (int i = 0; i < rdaList.size(); i++) {
                if(r == null || !r.equals(rdaList.get(i).getRegisterDetail().getRegister())){
                    if(r != null){
                        rList.add(r);
                    }
                    r = rdaList.get(i).getRegisterDetail().getRegister();
                    r.setDetails(new ArrayList<RegisterDetail>());
                }
                r.getDetails().add(rdaList.get(i).getRegisterDetail());
            }
            if(r != null){
                rList.add(r);
            }
            return rList;
        }else{
            return null;
        }
        
    }
}

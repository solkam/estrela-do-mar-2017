/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.dao;

import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetailAccountReceivable;
import com.cb.mundo.model.entity.RegisterDetailAccountReceivableDetail;
import com.cb.mundo.model.entity.RegisterDetailAccountReceivableObservation;
import com.cb.mundo.model.searchfilter.AccountReceivableFilter;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Lancelot
 */
public class AccountReceivableDAO {
    
    private DAO<RegisterDetailAccountReceivable> accountDAO;
    private DAO<RegisterDetailAccountReceivableDetail> accountDetailDAO;
    private DAO<RegisterDetailAccountReceivableObservation> accountObservationDAO;
    
    private EntityManager manager;

    public AccountReceivableDAO(EntityManager manager) {
        this.manager = manager;
        this.accountDAO = new JavaxPersistenceDAO<RegisterDetailAccountReceivable>(manager, RegisterDetailAccountReceivable.class);
        this.accountDetailDAO = new JavaxPersistenceDAO<RegisterDetailAccountReceivableDetail>(manager, RegisterDetailAccountReceivableDetail.class);
        this.accountObservationDAO = new JavaxPersistenceDAO<RegisterDetailAccountReceivableObservation>(manager, RegisterDetailAccountReceivableObservation.class);
    }
    
    public RegisterDetailAccountReceivable save(RegisterDetailAccountReceivable registerAccountReceivable){
        return accountDAO.save(registerAccountReceivable);
    }
    
    public void remove(RegisterDetailAccountReceivable registerAccountReceivable){
        accountDAO.remove(registerAccountReceivable);
    }
    
    public List<Register> listRegisters(MegaEvent megaEvent){
        return manager.createNamedQuery("AccountReceivable.listRegistersByMegaEvent", Register.class)
                .setParameter("pMegaEvent", megaEvent)
                .getResultList();
    }
    
    
    public RegisterDetailAccountReceivableDetail save(RegisterDetailAccountReceivableDetail registerAccountReceivableDetail){
        return accountDetailDAO.save(registerAccountReceivableDetail);
    }
    
    public void remove(RegisterDetailAccountReceivableDetail registerAccountReceivableDetail){
        accountDetailDAO.remove(registerAccountReceivableDetail);
    }
    
    public List<RegisterDetailAccountReceivableDetail> listDetailsForAccount(RegisterDetailAccountReceivable registerAccountReceivable){
        return manager.createNamedQuery("AccountReceivable.listDetails", RegisterDetailAccountReceivableDetail.class)
                .setParameter("pAccountReceivable", registerAccountReceivable)
                .getResultList();
    }
    
    private void addToWhereMultipleOrClause(StringBuilder strSentence, String var, String paramName, List params){
        if(params.size() == 1){
            strSentence.append(" ").append(var).append(" = :").append(paramName).append(0);
        }else{
            strSentence.append(" (");
            strSentence.append(" ").append(var).append(" = :").append(paramName).append(0);
            for (int i = 1; i < params.size() - 1; i++) {
                strSentence.append(" OR ").append(var).append(" = :").append(paramName).append(i);
            }
            strSentence.append(" OR ").append(var).append(" = :").append(paramName).append(params.size() - 1).append(")");
        }
    }
    
    public List<RegisterDetailAccountReceivable> searchUsingFilter(
            AccountReceivableFilter filter
    ){
        StringBuilder strSelect = new StringBuilder("SELECT rdar"),
                strFrom = new StringBuilder(" FROM RegisterDetailAccountReceivable rdar"), 
                strWhere = new StringBuilder(" WHERE");
        
        String pContactCivilName = "pContactCivilName",
                pMegaEvent = "pMegaEvent",
                pEvent = "pEvent", 
                pPaymentMethod = "pPaymentMethod", 
                pPaymentType = "pPaymentType", 
                pPaymentStatus = "pPaymentStatus";
        
        int whereLength = strWhere.length();
        boolean addContactCivilNameFilter = (filter.getContact() != null && filter.getContact().getCivilName()!= null && !filter.getContact().getCivilName().trim().isEmpty());
        boolean addEventNameFilter = (filter.getEventsList() != null && filter.getEventsList().size() > 0);
        boolean addPaymentMethodFilter = (filter.getPaymentMethodList() != null && filter.getPaymentMethodList().size() > 0);
        boolean addPaymentTypeFilter = (filter.getPaymentTypeList()!= null && filter.getPaymentTypeList().size() > 0);
        boolean addPaymentStatusFilter = (filter.getPaymentStatusList()!= null && filter.getPaymentStatusList().size() > 0);
        
        if(filter.getMegaEvent() != null){
            strWhere.append(" rdar.registerDetail.register.megaEvent = :").append(pMegaEvent);
        }
        if(addContactCivilNameFilter){
            if(strWhere.length() > whereLength) strWhere.append(" AND");
            strWhere.append(" rdar.registerDetail.register.contact.civilName like :").append(pContactCivilName);
        }
        if(addEventNameFilter){
            if(strWhere.length() > whereLength) strWhere.append(" AND");
            this.addToWhereMultipleOrClause(strWhere, "rdar.registerDetail.event.name", pEvent, filter.getEventsList());
        }
        if(addPaymentStatusFilter){
            if(strWhere.length() > whereLength) strWhere.append(" AND");
            this.addToWhereMultipleOrClause(strWhere, "rdar.status", pPaymentStatus, filter.getPaymentStatusList());
        }
        if(addPaymentTypeFilter){
            if(strWhere.length() > whereLength) strWhere.append(" AND");
            this.addToWhereMultipleOrClause(strWhere, "rdar.paymentType", pPaymentType, filter.getPaymentTypeList());
        }
        if(addPaymentMethodFilter){
            if(strWhere.length() > whereLength) strWhere.append(" AND");
            this.addToWhereMultipleOrClause(strWhere, "rdar.paymentMethod", pPaymentMethod, filter.getPaymentMethodList());
        }
        
        //date
        
        strSelect.append(strFrom.toString());
        if(strWhere.length() > whereLength){
            strSelect.append(strWhere.toString());
        }
        
        Query q = manager.createQuery(strSelect.toString());
        if(filter.getMegaEvent() != null){
            q.setParameter(pMegaEvent, filter.getMegaEvent());
        }
        if(addContactCivilNameFilter){
            q.setParameter(pContactCivilName, "%" + filter.getContact().getCivilName() + "%");
        }
        
        if(addEventNameFilter){
            for (int i = 0; i < filter.getEventsList().size(); i++) {
                q.setParameter(pEvent+i, filter.getEventsList().get(i).getName());
            }
        }
        if(addPaymentTypeFilter){
            for (int i = 0; i < filter.getPaymentTypeList().size(); i++) {
                q.setParameter(pPaymentType+i, filter.getPaymentTypeList().get(i));
            }
        }
        if(addPaymentStatusFilter){
            for (int i = 0; i < filter.getPaymentStatusList().size(); i++) {
                q.setParameter(pPaymentStatus+i, filter.getPaymentStatusList().get(i));
            }
        }
        if(addPaymentMethodFilter){
            for (int i = 0; i < filter.getPaymentMethodList().size(); i++) {
                q.setParameter(pPaymentMethod+i, filter.getPaymentMethodList().get(i));
            }
        }
        return q.getResultList();
    }
    
    public RegisterDetailAccountReceivableObservation save(RegisterDetailAccountReceivableObservation accountObservation){
        return accountObservationDAO.save(accountObservation);
    }
    
    public void remove(RegisterDetailAccountReceivableObservation accountObservation){
        accountObservationDAO.remove(accountObservation);
    }

    public List<RegisterDetailAccountReceivableObservation> listAccountObservations(RegisterDetailAccountReceivable registerAccountReceivable) {
        return manager.createNamedQuery("AccountReceivable.listObservations", RegisterDetailAccountReceivableObservation.class)
                .setParameter("pAccountReceivable", registerAccountReceivable)
                .getResultList();
    }
    
    public List<RegisterDetailAccountReceivableDetail> listAccountDetailExcludingPaid(Date upToProjectedPaymentDate, MegaEvent megaEvent){
        List<RegisterDetailAccountReceivableDetail> rdad = manager.createNamedQuery("AccountReceivable.listDetailNotPaidUpToProjectedPaymentDate", RegisterDetailAccountReceivableDetail.class)
            .setParameter("pUpToProjectedPaymentDate", upToProjectedPaymentDate, TemporalType.DATE)
            .setParameter("pMegaEvent", megaEvent)
            .getResultList();
        return rdad;
    }
    
}

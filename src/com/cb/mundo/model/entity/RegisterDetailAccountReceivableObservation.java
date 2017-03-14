/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.envers.Audited;

/**
 *
 * @author Lancelot
 */
@Entity 
@Table(name="register_detail_account_receivable_observation")
@Audited
public class RegisterDetailAccountReceivableObservation implements Serializable{
    private static final long serialVersionUID = 3244783347086529614L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 2000)
    private String text;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date obsSystemDate;
    
    @ManyToOne(optional = false)
    private RegisterDetailAccountReceivable accountReceivableOwner;
    
    @ManyToOne(optional = false)
    private Contact observationWritter;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getObsSystemDate() {
        return obsSystemDate;
    }

    public void setObsSystemDate(Date obsSystemDate) {
        this.obsSystemDate = obsSystemDate;
    }

    public RegisterDetailAccountReceivable getAccountReceivableOwner() {
        return accountReceivableOwner;
    }

    public void setAccountReceivableOwner(RegisterDetailAccountReceivable accountReceivableOwner) {
        this.accountReceivableOwner = accountReceivableOwner;
    }

    public Contact getObservationWritter() {
        return observationWritter;
    }

    public void setObservationWritter(Contact observationWritter) {
        this.observationWritter = observationWritter;
    }
}

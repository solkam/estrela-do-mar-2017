/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.entity;

import com.cb.mundo.model.entity.enumeration.AccountReceivableStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name="register_detail_account_receivable_detail")
@Audited
public class RegisterDetailAccountReceivableDetail implements Serializable{
    private static final long serialVersionUID = 3244783347086529611L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private RegisterDetailAccountReceivable accountReceivable;
    
    @Enumerated(EnumType.STRING)
    @Column(length=30, nullable=true)
    private AccountReceivableStatus status;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date paymentDateProjected;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date paymentDate;
    
    @Column
    private int feeNumber;
    
    @ManyToOne
    private PaymentCurrency paymentCurrency;
    
    @Column(nullable = false)
    private BigDecimal value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegisterDetailAccountReceivable getAccountReceivable() {
        return accountReceivable;
    }

    public void setAccountReceivable(RegisterDetailAccountReceivable accountReceivable) {
        this.accountReceivable = accountReceivable;
    }

    public AccountReceivableStatus getStatus() {
        return status;
    }

    public void setStatus(AccountReceivableStatus status) {
        this.status = status;
    }

    public Date getPaymentDateProjected() {
        return paymentDateProjected;
    }

    public void setPaymentDateProjected(Date paymentDateProjected) {
        this.paymentDateProjected = paymentDateProjected;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getFeeNumber() {
        return feeNumber;
    }

    public void setFeeNumber(int feeNumber) {
        this.feeNumber = feeNumber;
    }

    public PaymentCurrency getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(PaymentCurrency paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.accountReceivable != null ? this.accountReceivable.hashCode() : 0);
        hash = 37 * hash + (this.status != null ? this.status.hashCode() : 0);
        hash = 37 * hash + (this.paymentDateProjected != null ? this.paymentDateProjected.hashCode() : 0);
        hash = 37 * hash + (this.paymentDate != null ? this.paymentDate.hashCode() : 0);
        hash = 37 * hash + this.feeNumber;
        hash = 37 * hash + (this.paymentCurrency != null ? this.paymentCurrency.hashCode() : 0);
        hash = 37 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RegisterDetailAccountReceivableDetail other = (RegisterDetailAccountReceivableDetail) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.accountReceivable != other.accountReceivable && (this.accountReceivable == null || !this.accountReceivable.equals(other.accountReceivable))) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (this.paymentDateProjected != other.paymentDateProjected && (this.paymentDateProjected == null || !this.paymentDateProjected.equals(other.paymentDateProjected))) {
            return false;
        }
        if (this.paymentDate != other.paymentDate && (this.paymentDate == null || !this.paymentDate.equals(other.paymentDate))) {
            return false;
        }
        if (this.feeNumber != other.feeNumber) {
            return false;
        }
        if (this.paymentCurrency != other.paymentCurrency && (this.paymentCurrency == null || !this.paymentCurrency.equals(other.paymentCurrency))) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RegisterDetailAccountReceivableDetail{" + "id=" + id + ", accountReceivable=" + accountReceivable + ", status=" + status + ", paymentDateProjected=" + paymentDateProjected + ", paymentDate=" + paymentDate + ", feeNumber=" + feeNumber + ", paymentCurrency=" + paymentCurrency + ", value=" + value + '}';
    }
    
    
    
}

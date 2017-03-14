/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.entity;

import com.cb.mundo.model.entity.enumeration.AccountReceivableStatus;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;
import com.cb.mundo.model.entity.enumeration.PaymentType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * Represents an account receivable associated with a register detail.
 * @author Lancelot
 */
@Entity 
@Table(name="register_detail_account_receivable")
@Audited
public class RegisterDetailAccountReceivable implements Serializable{
    private static final long serialVersionUID = 3244783347086529610L;

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(length=30, nullable=true)
    private PaymentType paymentType;//contado, cuota automatico, cuota manual
    
    @Enumerated(EnumType.STRING)
    @Column(length=30, nullable=false)
    private MegaEventPaymentMethod paymentMethod;//cheque, efectivo, TC, transferencia, PAT (VCC), Web
    
    @ManyToOne(optional = true)
    private CreditCardType creditCardType;//VISA, MasterCard, Magna, American express, Dinner club
    
    @ManyToOne(optional = false)
    private InscriptionType inscriptionType;//normal, promocion, paquete 3 y 4, paquete 4 modulos
    
    @Column
    private BigInteger creditCardNumber;
    
    @Column
    private Date creditCardDueDate;
    
    @ManyToOne
    private UserCB createdBy;
    
    @OneToOne
    @JoinColumn(unique=true)
    private RegisterDetail registerDetail;
    
    @Column
    private int paymentDay;
    
    @Column
    private int amountOfFees;
    
    @Column(nullable = false)
    private BigDecimal totalValue;
    
    @Enumerated(EnumType.STRING)
    @Column(length=30, nullable=true)
    private AccountReceivableStatus status;
    
    @OneToMany(mappedBy = "accountReceivable")
    private List<RegisterDetailAccountReceivableDetail> accountReceivableDetailList;
    
    @OneToMany(mappedBy = "accountReceivableOwner", orphanRemoval = true)
    private List<RegisterDetailAccountReceivableObservation> observationList;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public MegaEventPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(MegaEventPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public CreditCardType getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(CreditCardType creditCardType) {
        this.creditCardType = creditCardType;
    }

    public InscriptionType getInscriptionType() {
        return inscriptionType;
    }

    public void setInscriptionType(InscriptionType inscriptionType) {
        this.inscriptionType = inscriptionType;
    }

    public UserCB getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserCB createdBy) {
        this.createdBy = createdBy;
    }

    public RegisterDetail getRegisterDetail() {
        return registerDetail;
    }

    public void setRegisterDetail(RegisterDetail registerDetail) {
        this.registerDetail = registerDetail;
    }

    public int getPaymentDay() {
        return paymentDay;
    }

    public void setPaymentDay(int paymentDay) {
        this.paymentDay = paymentDay;
    }

    public int getAmountOfFees() {
        return amountOfFees;
    }

    public void setAmountOfFees(int amountOfFees) {
        this.amountOfFees = amountOfFees;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public AccountReceivableStatus getStatus() {
        return status;
    }

    public void setStatus(AccountReceivableStatus status) {
        this.status = status;
    }
    
    public List<RegisterDetailAccountReceivableDetail> getAccountReceivableDetailList() {
        return accountReceivableDetailList;
    }

    public void setAccountReceivableDetailList(List<RegisterDetailAccountReceivableDetail> accountReceivableDetailList) {
        this.accountReceivableDetailList = accountReceivableDetailList;
    }

    public List<RegisterDetailAccountReceivableObservation> getObservationList() {
        return observationList;
    }

    public void setObservationList(List<RegisterDetailAccountReceivableObservation> observationList) {
        this.observationList = observationList;
    }

    public BigInteger getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(BigInteger creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Date getCreditCardDueDate() {
        return creditCardDueDate;
    }

    public void setCreditCardDueDate(Date creditCardDueDate) {
        this.creditCardDueDate = creditCardDueDate;
    }
       
    public BigDecimal getCalculatedTotalForAccountReceivable(){
        Register reg = this.registerDetail.getRegister();
        reg.getDetails().size();
        BigDecimal total = new BigDecimal(0);
        for (int i = 0; i < reg.getAllDetails().size(); i++) {
            total = total.add(
                reg.getAllDetails().get(i).getAccountReceivable().getTotalValue()
            );
        }
        return total;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.paymentType != null ? this.paymentType.hashCode() : 0);
        hash = 37 * hash + (this.paymentMethod != null ? this.paymentMethod.hashCode() : 0);
        hash = 37 * hash + (this.creditCardType != null ? this.creditCardType.hashCode() : 0);
        hash = 37 * hash + (this.inscriptionType != null ? this.inscriptionType.hashCode() : 0);
        hash = 37 * hash + (this.creditCardNumber != null ? this.creditCardNumber.hashCode() : 0);
        hash = 37 * hash + (this.creditCardDueDate != null ? this.creditCardDueDate.hashCode() : 0);
        hash = 37 * hash + (this.createdBy != null ? this.createdBy.hashCode() : 0);
        hash = 37 * hash + (this.registerDetail != null ? this.registerDetail.hashCode() : 0);
        hash = 37 * hash + this.paymentDay;
        hash = 37 * hash + this.amountOfFees;
        hash = 37 * hash + (this.totalValue != null ? this.totalValue.hashCode() : 0);
        hash = 37 * hash + (this.status != null ? this.status.hashCode() : 0);
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
        final RegisterDetailAccountReceivable other = (RegisterDetailAccountReceivable) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.paymentType != other.paymentType) {
            return false;
        }
        if (this.paymentMethod != other.paymentMethod) {
            return false;
        }
        if (this.creditCardType != other.creditCardType && (this.creditCardType == null || !this.creditCardType.equals(other.creditCardType))) {
            return false;
        }
        if (this.inscriptionType != other.inscriptionType && (this.inscriptionType == null || !this.inscriptionType.equals(other.inscriptionType))) {
            return false;
        }
        if (this.creditCardNumber != other.creditCardNumber && (this.creditCardNumber == null || !this.creditCardNumber.equals(other.creditCardNumber))) {
            return false;
        }
        if (this.creditCardDueDate != other.creditCardDueDate && (this.creditCardDueDate == null || !this.creditCardDueDate.equals(other.creditCardDueDate))) {
            return false;
        }
        if (this.createdBy != other.createdBy && (this.createdBy == null || !this.createdBy.equals(other.createdBy))) {
            return false;
        }
        if (this.registerDetail != other.registerDetail && (this.registerDetail == null || !this.registerDetail.equals(other.registerDetail))) {
            return false;
        }
        if (this.paymentDay != other.paymentDay) {
            return false;
        }
        if (this.amountOfFees != other.amountOfFees) {
            return false;
        }
        if (this.totalValue != other.totalValue && (this.totalValue == null || !this.totalValue.equals(other.totalValue))) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RegisterDetailAccountReceivable{" + "id=" + id + ", paymentType=" + paymentType + ", paymentMethod=" + paymentMethod + ", creditCardType=" + creditCardType + ", inscriptionType=" + inscriptionType + ", creditCardNumber=" + creditCardNumber + ", creditCardDueDate=" + creditCardDueDate + ", userCB=" + createdBy + ", registerDetail=" + registerDetail + ", paymentDay=" + paymentDay + ", amountOfFees=" + amountOfFees + ", totalValue=" + totalValue + ", status=" + status + '}';
    }
    
    

    
}
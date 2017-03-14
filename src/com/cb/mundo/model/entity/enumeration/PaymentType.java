/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.entity.enumeration;

/**
 *
 * @author Lancelot
 */
public enum PaymentType {
    IC("label_payment_type_in_cash", true)
    , AF("label_payment_type_automated_fee", false, true, true)
    , MF("label_payment_type_manual_fee", false)
    , NO("label_payment_type_none", false)
    ;
    
    private String key;
    
    private boolean cashPayment;
    private boolean automated;
    private boolean sendEmail;

    private PaymentType(String key, boolean cashPayment) {
        this.key = key;
        this.cashPayment = cashPayment;
        this.automated = false;
        this.sendEmail = false;
    }

    private PaymentType(String key, boolean cashPayment, boolean automated, boolean sendEmail) {
        this.key = key;
        this.cashPayment = cashPayment;
        this.automated = automated;
        this.sendEmail = sendEmail;
    }

    
    public String getKey() {
        return key;
    }
    
    public boolean getCashPayment() {
        return cashPayment;
    }

    public boolean getAutomated() {
        return automated;
    }

    public boolean getSendEmail() {
        return sendEmail;
    }

}

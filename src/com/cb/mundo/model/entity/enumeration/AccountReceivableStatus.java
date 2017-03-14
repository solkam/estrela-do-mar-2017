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
public enum AccountReceivableStatus {

    ATV("account_receivable_active")
    , PDT("account_receivable_pendant")
    , RMD("account_receivable_reminder")
    , SPV("account_receivable_supervisor")
    , DCT("account_receivable_director")
    , ANL("account_receivable_anuled")
    , PWC("account_receivable_pay_when_can")
    , TSP("account_receivable_traspased")
    , PAD("account_receivable_paid")
    ;
    
    private final String key;

    private AccountReceivableStatus(String key) {
            this.key = key;
    }

    public String getKey() {
       return key;
    }

    @Override
    public String toString() {
        return  name();
    }
    
}

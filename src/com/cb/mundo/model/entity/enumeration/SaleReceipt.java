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
public enum SaleReceipt {
	
    TICKET("label_sale_receipt_ticket", false)
    ,INVOICE("label_sale_receipt_invoice", true);
    
    private final String key;
    private final boolean forCompanies;

    private SaleReceipt(String key, boolean forCompanies) {
        this.key = key;
        this.forCompanies = forCompanies;
    }

    public String getKey() {
        return key;
    }

    public boolean isForCompanies() {
        return forCompanies;
    }
}

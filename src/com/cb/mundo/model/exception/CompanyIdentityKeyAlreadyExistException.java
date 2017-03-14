/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Company;

/**
 *
 * @author Lancelot
 */
public class CompanyIdentityKeyAlreadyExistException  extends BusinessException {
	private static final long serialVersionUID = 5918631941428796409L;

	public CompanyIdentityKeyAlreadyExistException(Company company) {
		super("msg_company_identity_key_already_exist", null);
	}
}

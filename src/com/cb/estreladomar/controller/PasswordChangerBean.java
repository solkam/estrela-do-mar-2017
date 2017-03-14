package com.cb.estreladomar.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.cb.estreladomar.controller.security.LoginBean;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.exception.PasswordNotConfirmedException;
import com.cb.mundo.model.service.UserService;

/**
 * Managed Bean para alteracao de senha
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="passChangerBean")
public class PasswordChangerBean implements Serializable {
	private static final long serialVersionUID = -6003147340994220585L;

	@EJB UserService userService;
	
	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;
	
	private String currentPass;
	private String pass1;
	private String pass2;
	

	
	public void change() {
		try {
			validatePass1EqualsPass2();
			validateCurrentPass();
			validateNewPassEqualOldPass();
			
			UserCB user = loginBean.getAuthenticatedUser();
			user.setPassword(pass1);
			
			userService.changePassword(user, loginBean.getAuthenticatedUser() );
			
			JSFUtil.addInfoMessage("msg_pass_change_sucess", null);
			
		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
	private void validatePass1EqualsPass2() {
		if (!pass1.equals(pass2)) {
			throw new PasswordNotConfirmedException();
		}
	}
	
	private void validateCurrentPass() {
		UserCB user = loginBean.getAuthenticatedUser();
		userService.validatePasswordEqualTheCurrentOne(user, currentPass);//pode lancar PasswordInvalidException
	}

	private void validateNewPassEqualOldPass() {
		UserCB user = loginBean.getAuthenticatedUser();
		userService.validateNewPasswordEqualTheCurrentOne(user, pass1);//pode lancar PasswordNewEqualsOldException
	}
	

//acessores

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}


	public String getCurrentPass() {
		return currentPass;
	}


	public void setCurrentPass(String currentPass) {
		this.currentPass = currentPass;
	}


	public String getPass1() {
		return pass1;
	}


	public void setPass1(String pass1) {
		this.pass1 = pass1;
	}


	public String getPass2() {
		return pass2;
	}


	public void setPass2(String pass2) {
		this.pass2 = pass2;
	}
	
	
	

}

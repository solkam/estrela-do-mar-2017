package com.cb.estreladomar.controller;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.service.UserService;

@ManagedBean
@ViewScoped
public class UsuarioBean {
	
	private UserCB user = new UserCB();
	
	@EJB
	private UserService service;
	
	public void save() {
		try {
			service.saveUser(user, null);
			
			JSFUtil.addInfoMessage("msg_user_saved_sucess", null);
			
		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
}

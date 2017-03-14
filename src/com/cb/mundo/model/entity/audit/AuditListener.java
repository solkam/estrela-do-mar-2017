package com.cb.mundo.model.entity.audit;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.envers.RevisionListener;

import com.cb.mundo.model.util.Constants;

/**
 * Listener do Envers para adicionar o login do usuario na auditoria.
 * No processo de login, eh colocado no escopo da session o login (email)
 * do usuario autenticado). 
 *  
 * @author vitor
 * @since 02 MAI 2014
 */
public class AuditListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionObject) {
		Audit audit = (Audit) revisionObject;
		
		audit.setUserEmail( getUserEmail() );
		audit.setRemoteIP(  getRemoteIP()  );
	}
	
	
	private String getUserEmail() {
		String username = (String) getHttpSession().getAttribute( Constants.SESSION_ATTRIBUTE_AUTENTICATED_USERNAME );
		return username;
	}
	
	private String getRemoteIP() {
		return getHttpRequest().getRemoteHost();
	}
	
	
	
//utils...	
	private HttpSession getHttpSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (HttpSession) context.getExternalContext().getSession(false);
	}
	

	private HttpServletRequest getHttpRequest() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (HttpServletRequest) context.getExternalContext().getRequest();
	}
	
	
}

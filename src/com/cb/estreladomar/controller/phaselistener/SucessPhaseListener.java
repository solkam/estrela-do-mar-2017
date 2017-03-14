package com.cb.estreladomar.controller.phaselistener;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.primefaces.context.RequestContext;

/**
 * Listener que indica se durante um processamente de
 * request, aconteceu um erro
 * 
 * @author Solkam
 * @since 11 AGO 2013
 */
public class SucessPhaseListener implements PhaseListener {
	
	private static final String SUCESS_ONLY = "sucessOnly";
	

	@Override
	public void beforePhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		
		RequestContext requestContext = RequestContext.getCurrentInstance();

		FacesMessage.Severity severity = facesContext.getMaximumSeverity();
		
		/*
		 * 0: info (unico que nao eh erro)
		 * 1: warn 
		 * 2: error
		 * 3: fatal
		 */
		if (severity!=null && severity.getOrdinal() > 0) {
			requestContext.addCallbackParam(SUCESS_ONLY, Boolean.FALSE );
		} else {
			requestContext.addCallbackParam(SUCESS_ONLY, Boolean.TRUE );
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	
	
	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getMessageList();
	}

	
	private static final long serialVersionUID = 11336915613882414L;
}

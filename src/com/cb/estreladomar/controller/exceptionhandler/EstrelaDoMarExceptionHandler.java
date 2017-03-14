package com.cb.estreladomar.controller.exceptionhandler;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.exception.BusinessException;




/**
 * Manipulador de Exception do JSF2. 
 * 
 * @author vitor
 * @since 24 JUN 2013
 */
public class EstrelaDoMarExceptionHandler extends ExceptionHandlerWrapper {
	
	private ExceptionHandler wrapped;
	
	public EstrelaDoMarExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}
	
	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}
	

	/**
	 * Metodo que efetivamente manipula a exception.
	 */
	@Override
	public void handle() throws FacesException {
		//iterage para pegar todas as exceptions geradas
		for (ExceptionQueuedEvent event : getUnhandledExceptionQueuedEvents() ) {
			
			ExceptionQueuedEventContext context = event.getContext();
		
			Throwable throwable         = context.getException();   // tipo FacesException
			
			//alternativa
			Throwable essenciaException = getAlternativeRootCause(throwable);
			
			if (essenciaException instanceof BusinessException) {
				JSFUtil.addErrorMessage( essenciaException );
			
			} else if (essenciaException instanceof ViewExpiredException) {
				handleErrorViewExpired();
				
			} else {
				JSFUtil.addFatalMessage(essenciaException);
			}
		}
	}
	
	
	/**
	 * Adiciona msg de erro sobre a view expirada e  for�a a 
	 * navega��o para a pagina de erro correspondente.
	 */
	private void handleErrorViewExpired() {
		JSFUtil.addErrorMessage("msg_view_expired_error", null);

		FacesContext context = FacesContext.getCurrentInstance();
		NavigationHandler handler = context.getApplication().getNavigationHandler();
		handler.handleNavigation(context, null, "/error-view-expired?faces-redirect=true");
		context.renderResponse();
	}
	
	
	private Throwable getAlternativeRootCause(Throwable t) {
		Throwable result = t;
		Throwable previousResult = null;
		
		while (result.getCause() != null && !result.equals(previousResult)) {
			previousResult = result;
			result = result.getCause();
		}
		return result;
	}


	
}

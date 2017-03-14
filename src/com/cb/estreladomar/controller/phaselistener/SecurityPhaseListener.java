package com.cb.estreladomar.controller.phaselistener;

import javax.faces.application.NavigationHandler;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import com.cb.estreladomar.controller.security.LoginBean;
import com.cb.estreladomar.controller.util.Navigation;

/**
 * Listener do JSF para autorizacao
 * 
 * @author Solkam
 * @since 20 mai 2012
 */
public class SecurityPhaseListener implements PhaseListener {
	
	// array paginas que sao ignorados pelo listener...
//	private static final String[] PUBLIC_PAGES = {
//		 "login.xhtml"
//		,"signup-search.xhtml"
//		,"signup-form.xhtml"
//		,"loader/ref.xhtml"
//		,"notes.xhtml"
//		,"MyRegisterPaymentFail.do"
//		,"MyRegisterPaymentSucess.do"
//	};


	private boolean isPublicPage(String page) {
//		for (String byPassedPage : BY_PASSED_PAGES) {
//			if (page.endsWith(byPassedPage))
//				return true;
//		}
//		return false;
		return true;
	}
	
	@Override
	public void afterPhase(PhaseEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		
		// somente paginas 'publicas' serao ignoradas...
		if ( isPublicPage( context.getViewRoot().getViewId() )) {
			return;
		}
		
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		
		if (!loginBean.isLogged()) {
			NavigationHandler handler = context.getApplication().getNavigationHandler();
			handler.handleNavigation(context, null, Navigation.LOGIN);
			context.renderResponse();
		}
	}


	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
	
	
	@Override
	public void beforePhase(PhaseEvent arg0) {
		
	}

	
	
	private static final long serialVersionUID = -3167653354782346383L;
}

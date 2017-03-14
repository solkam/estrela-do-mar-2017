package com.cb.estreladomar.controller.util;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import com.cb.mundo.model.exception.MundoCBException;
import com.cb.mundo.model.exception.SeverityWarningExceptionType;
import com.cb.mundo.model.util.Constants;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

/**
 * Utilitario para JSF: 
 * - adiciona messagem internacionalizada
 * - trata locale
 * 
 * 21 out 2012: [solkam] tentei refatora para mundocb-core para servir para todos as webapps futuras,
 * mas nao estava encontrando o arquivo application.properties mesmo com getRealPath.
 * Entao fiz a mesma copia e coloquei em seminario e inscricao, como sera antes so que agora eh exatamente
 * a mesma classe java.
 * 
 * @author solkam
 * @since 02 out 2011
 */
public class JSFUtil {
	
//	private static Logger logger = LogManager.getLogger(JSFUtil.class);
	
	public static void addInfoMessage(String key, Object[] params) {
		handleMessage(key, params, FacesMessage.SEVERITY_INFO);
	}
	
	public static void addInfoMessage(String key) {//versao sobre-carregada
		addInfoMessage(key, null);
	}

	public static void addErrorMessage(Throwable e) {
		if (e instanceof MundoCBException) {
			MundoCBException mcbe = (MundoCBException)e;
			
			if (mcbe instanceof SeverityWarningExceptionType) {
				addFormatedMessage(mcbe.getI18nMessage(), null, FacesMessage.SEVERITY_WARN );
			} else {
				addFormatedMessage(mcbe.getI18nMessage(), null, FacesMessage.SEVERITY_ERROR );
			}

		} else {
			handleMessage("msg_general_error", null, FacesMessage.SEVERITY_FATAL);			
//			sendEmailToEngineer(e);
		}
	}
	
	public static void addErrorMessage(String key, Object[] params) {
		handleMessage(key, params, FacesMessage.SEVERITY_ERROR);
	}
	
	public static void addFatalMessage(Throwable throwable) {
		if (throwable==null) {//seguran�a
			return;
		}
		
		String summaryMsg = getFormatedMessage("msg_general_error", null);
		String detailMsg  = throwable.getMessage();
		
		FacesMessage fm = new FacesMessage( FacesMessage.SEVERITY_FATAL, summaryMsg, detailMsg );
		FacesContext.getCurrentInstance().addMessage(null, fm);
	}
	
	
	public static void addWarnMessage(String key) {
		handleMessage(key, null, FacesMessage.SEVERITY_WARN);
	}
	
	
	public static void addMessageAboutResult(List<?> result) {
		if (result==null || result.isEmpty()) {
			handleMessage("msg_no_result_found", null, FacesMessage.SEVERITY_WARN);

		} else {
			Object[] params = {result.size()};
			handleMessage("msg_results_found", params, FacesMessage.SEVERITY_INFO);
		}
	}
	
	public static Locale getFacesLocale() {
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		if (viewRoot != null) {
			return viewRoot.getLocale();
		} else {
			return Constants.DEFAULT_LOCALE;
		}
	}
	
//private methods...
	
	private static void handleMessage(String key, Object[] params, Severity severity) {
		String formatedMessage = getFormatedMessage(key, params);
		addFormatedMessage(formatedMessage, null, severity);
	}
	
	private static void addFormatedMessage(String formatedSummary, String formatedDetail, Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage fm = new FacesMessage(severity, formatedSummary, formatedDetail);
		context.addMessage(null, fm );
	}
	
	private static ResourceBundle getBundle() {
//      Tentei desta maneira, mas nao deu certo...
//		String bundleRelativePath = "/WEB-INF/classes/resources/application";
//		String bundleRealPath = fc.getExternalContext().getRealPath( bundleRelativePath );

		FacesContext fc = FacesContext.getCurrentInstance();
		String bundleName = fc.getApplication().getMessageBundle();
		
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, getCurrentLocale());
		return bundle;
	}
	
	public static String getFormatedMessage(String key, Object[] params) {
		String message = getBundle().getString(key); 
		return formatMessage(message, params);
	}
	
	private static Locale getCurrentLocale() {
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		if (viewRoot != null) {
			return viewRoot.getLocale();
		} else {
			return Constants.DEFAULT_LOCALE;
		}
	}

	private static String formatMessage(String message, Object[] params) {
		return MessageFormat.format(message, params);
	}
	
	
}

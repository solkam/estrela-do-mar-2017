package com.cb.mundo.model.exception;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.ejb.ApplicationException;
import javax.faces.context.FacesContext;

@ApplicationException
public abstract class MundoCBException extends RuntimeException {
	private static final long serialVersionUID = -7937889008487550883L;
	
	private Object[] params;
	
	private static ResourceBundle bundle;
	
	
	/**
	 * Carrega o properties de messagens de exception numa varival static
	 * apenas uma vez
	 */
	private void loadResourceBundle() {
		if (bundle==null) {
			bundle = ResourceBundle.getBundle("com/cb/mundo/model/exception/exception_message", getCurrentLocale() );
		}
	}
	
	/**
	 * Testa dois possiveis locale vindo do FacesContext: viewRoot ou Applicaction
	 * Se FacesContext ou viewRoot ou Application forem invalidos, assume o locale pt.
	 * @return locale do faces context ou pt.
	 */
	private Locale getCurrentLocale() {
		Locale currentLocale = new Locale("pt");
		
		FacesContext context = FacesContext.getCurrentInstance();
		if (context!=null) {
			//testa dois possiveis locales: do viewRoot e do Application
			Locale auxLocale = null;
			
			auxLocale = context.getViewRoot().getLocale();
			if (auxLocale!=null) {
				currentLocale = auxLocale;
			} else {
				auxLocale = context.getApplication().getDefaultLocale();
				if (auxLocale!=null) {
					currentLocale = auxLocale;
				}
			}
		}
		return currentLocale;
	}
	
	
	
	protected MundoCBException(String key, Object[] params, Throwable cause) {
		super(key, cause);
		this.params = params;
		loadResourceBundle();
	}
	protected MundoCBException(String key, Object[] params) {
		super(key);
		this.params = params;
		loadResourceBundle();
	}

	protected MundoCBException(Throwable cause) {
		super(cause);
		loadResourceBundle();
	}
	
	
	
	public Object[] getParams() {
		return params;
	}

	public String getI18nMessage() {
		String value = bundle.getString( getMessage() );
		return MessageFormat.format(value, getParams() ); 
	}
	
	

}

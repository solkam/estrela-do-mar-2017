package com.cb.estreladomar.controller;

import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import com.cb.mundo.model.entity.enumeration.Idiom;

/**
 * Managed Bean para definicao do idioma
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="idiomBean")
@RequestScoped
public class IdiomBean {
	
	private Locale locale;
	
	
	public IdiomBean() {
		defineInitialLocale();
	}
	
	private void defineInitialLocale() {
		this.locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
		if (this.locale==null) {
			this.locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		}
		if (this.locale == null) {
			this.locale = new Locale("pt");
		}
	}
	
	public void changeTo(String langCode) {
		this.locale = new Locale(langCode);
		
		FacesContext.getCurrentInstance().getApplication().setDefaultLocale(locale);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}
	
	public Idiom getIdiom() {
		return Idiom.valueOf(locale.getLanguage());
	}
	
	public String getLangCode() {
		return this.locale.getLanguage();
	}

}

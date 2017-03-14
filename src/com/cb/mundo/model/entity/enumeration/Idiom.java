package com.cb.mundo.model.entity.enumeration;

import java.util.Locale;


/**
 * Idiomas suportados pelo sistema.
 * 
 * @author Solkam
 * @since 25 set 2011
 */
public enum Idiom {
	
	pt("Português", "icon_idiom_pt_32.png", "pt") 
	,
	es("Espanhol" , "icon_idiom_es_32.png", "es")
	,
	en("English"  , "icon_idiom_en_32.png", "en")
	,
	it("Italiano" , "icon_idiom_it_32.png", "it")
	,
	fr("Francês"  , "icon_idiom_fr_32.png", "fr")
	;
	

	private final String description;
	private final String image;
	private final String sigla;
	
	private Idiom(String description, String image, String s) {
		this.description = description;
		this.image = image;
		this.sigla = s;
	}
	

	
	public String getDescription() {
		return description;
	}

	public String getImage() {
		return image;
	}

	public String getSigla() {
		return sigla;
	}

	public Locale getLocale() {
		return new Locale( this.name() );
	}
	

}

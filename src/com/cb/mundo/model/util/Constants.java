package com.cb.mundo.model.util;

import java.util.Locale;

import com.cb.mundo.model.entity.Contact;

/**
 * Constantes do sistema
 * 
 * @author Solkam
 * @since 20 nov 2011
 */
public interface Constants {
	
	/**
	 * Chave para guardar o nome do usuario autenticado na sessao.
	 * (usado pelo auditoria)
	 */
	String SESSION_ATTRIBUTE_AUTENTICATED_USERNAME = "AUTENTICATED_USERNAME";


	
	//contatos padroes para envio de email
	
	Contact INSCRIPTION_MOUNTAIN_CONTACT = new Contact("Inscripcion Condor Blanco", "inscripcion@condorblanco.com", "");
	
	Contact INSCRIPTION_ESTRELAMAR_CONTACT = new Contact("Inscrição Estrela do Mar CB", "estreladomarfinanceiro@gmail.com", "");
	
	Contact TECNOLOGY_CONTACT = new Contact("Solkam", "solkam.cb@gmail.com", "(35)9 9107 1855");
	
	Locale DEFAULT_LOCALE = new Locale("pt");
	
}

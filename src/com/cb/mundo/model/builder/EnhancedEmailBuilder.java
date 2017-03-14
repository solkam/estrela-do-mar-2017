package com.cb.mundo.model.builder;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.util.Constants;
import com.cb.mundo.model.util.FormatUtil;
import com.cb.mundo.model.util.NumberUtil;

/**
 * Strategy para construtor de emails para os seminarios
 * 
 * @author Solkam
 * @since 20 ABR 2014
 */
public abstract class EnhancedEmailBuilder {
	
	public abstract Set<Contact> getNormalContactRecipients();
	
	public abstract Set<Contact> getBlindContactRecipients();
	
	public abstract String getReplyTO();
	
	public abstract String getEmailSubject();

	public abstract String getEmailBody();
	
	
	
	//html content symbols...
	
	public static final String OPEN_PARENTESIS  = "(";
	public static final String CLOSE_PARENTESIS = ")";
	public static final String WHITESPACE       = " ";
	public static final String ONE_POINT        = ". ";
	public static final String TWO_POINTS       = ": ";
	public static final String COMA             = ", ";
	public static final String POINT_COMA       = "; ";
	public static final String ASCII_BREAKLINE  = "\n";
	
	public static final String HTML_FILE_HEADER     = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	public static final String HTML_BREAKLINE       = "<br />";
	public static final String HTML_OPEN_LIST       = "<ul>";
	public static final String HTML_CLOSE_LIST      = "</ul>";
	public static final String HTML_OPEN_LIST_ITEM  = "<li>";
	public static final String HTML_CLOSE_LIST_ITEM = "</li>";
	

	//html functions...
	
	public String toStrong(String txt) {
		return "<strong>"+txt+"</strong>";
	}

	public String toStrong(Integer txt) {
		return "<strong>"+txt+"</strong>";
	}
	
	public String toStrong(Double txt) {
		String formatedCurrency = NumberUtil.roundTwoDecimalsAsString(txt);
		return "<strong>"+formatedCurrency+"</strong>";
	}

	public String toStrong(Date d) {
		String txt = FormatUtil.toString(d);
		return "<strong>"+txt+"</strong>";
	}

	public String toItalic(String txt) {
		return "<i>"+txt+"</i>";
	}
	
	public String toIndented(String txt) {
		return "<span style='padding-left: 10px;'>"+txt+"</span>";
	}
	
	public String toH3(String txt) {
		return "<h3>"+txt+"</h3>";
	}


	/**
	 * Monta uma string contendo todos os 
	 * destinatarios normais.
	 * @return
	 */
	public String getNormalRecipientsAsString() {
		StringBuilder builder = new StringBuilder();
		for (Contact contact : getNormalContactRecipients()) {
			builder.append( contact.getMailingDesc() ).append(", ");
		}
		return builder.toString();
	}
	
	
	
	public Set<Contact> getTecnologyContacts() {
		Set<Contact> tecContacts = new TreeSet<>();
		tecContacts.add( Constants.TECNOLOGY_CONTACT );
		return tecContacts;
	}

}

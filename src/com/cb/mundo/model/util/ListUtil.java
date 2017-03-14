package com.cb.mundo.model.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import com.cb.mundo.model.entity.Contact;

/**
 * Utilitarios para listas e arrays
 * 
 * @author Solkam
 * @since 14 MAI 2012
 */
public class ListUtil {
	private static final String EMPTY_STRING = "";
	
	
	/**
	 * Recives array of string (representing emails) and 
	 * convert to a set of contact
	 * 
	 * @param emailsSelected
	 * @return set of contacts
	 */
	public Set<Contact> toSetOfContact(final String[] emailsSelected) {
		Set<Contact> contacts = new TreeSet<Contact>();
		for (String email : emailsSelected ) {
			Contact contact = new Contact();
			contact.setEmail(email);
			contacts.add(contact);
		}
		return contacts;
	}
	
	/**
	 * Receives a set of contact and convert to a array of string
	 * with their emails.
	 * 
	 * @param recipients
	 * @return array of email strings
	 */
	public String[] toArrayOfString(final Set<Contact> recipients) {
		String[] emails = new String[recipients.size()];
		int i=0;
		for (Contact contact : recipients) {
			emails[i++] = contact.getEmail();
		}
		return emails;
	}
	

	/**
	 * Builds a string delimited by special string from a array of string
	 * 
	 * @param ARRAY
	 * @param DELIM
	 * @return a single string representing the array
	 */
	public String buildStringFromArrayDelimBy(final String[] ARRAY, final String DELIM) {
		if (ARRAY==null) return null;
		if (ARRAY.length==0) return EMPTY_STRING;
		
		StringBuilder builderAux = new StringBuilder();
		for (String element : ARRAY) {
			builderAux.append(element);
			builderAux.append( DELIM );
		}
		return builderAux.toString();
	}
	
	/**
	 * Builds a array of string from a string tokenized by a special caracter from a single string
	 * @param TEXT
	 * @param DELIM
	 * @return array of string
	 */
	public String[] buildArrayFromStringDelimBy(final String TEXT, final String DELIM) {
		List<String> listAux = new ArrayList<String>();
		
		if (TEXT !=null) {
			StringTokenizer tokenizer = new StringTokenizer(TEXT, DELIM);
			while(tokenizer.hasMoreElements()) {
				String element = tokenizer.nextToken();
				if (element!=null && !element.isEmpty()) {
					listAux.add( element );
				}
			}
		}
		
		String[] arrayOfString = new String[listAux.size()];
		int i=0;
		for (String email : listAux) {
			arrayOfString[i++] = email;
		}
		return arrayOfString;
	}
	
	
	

}

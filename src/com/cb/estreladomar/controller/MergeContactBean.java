package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.enumeration.DocumentType;
import com.cb.mundo.model.service.MergeService;

@ManagedBean(name="mergeContactBean")
@ViewScoped
public class MergeContactBean implements Serializable {
	private static final long serialVersionUID = 3366686887196029351L;

	@EJB MergeService mservice;
	
	private List<Contact> duplicatedContactsByEmail;
	private List<Contact> duplicatedContactsByCivilName;
	private List<Contact> duplicatedContactsByName;
	
	private Contact contact1;
	private Contact contact2;
	
	//init cap automatico
	private boolean initCapNewName = true;
	private boolean initCapCivilName = true;
	private boolean initCapCity = true;
	private boolean initCapCountry = true;
	
	private boolean lastMergeWasByEmail     = false;
	private boolean lastMergeWasByCivilName = false;
	
	public void searchDuplicatecContactsByEmail() {
		duplicatedContactsByEmail = mservice.searchDuplicatedContactByEmail();
		JSFUtil.addMessageAboutResult( duplicatedContactsByEmail) ;
	}
	
	public void searchDuplicatedContactsByCivilName() {
		duplicatedContactsByCivilName = mservice.searchDuplicatedContactByCivilName();
		JSFUtil.addMessageAboutResult( duplicatedContactsByCivilName );
	}

	public void searchDuplicatedContactsByName() {
		duplicatedContactsByName = mservice.searchDuplicatedContactByName();
		JSFUtil.addMessageAboutResult( duplicatedContactsByName );
	}

	
	/**
	 * Verifica qual foi a ultima pesquisa para recarrega-la
	 * Se ainda houver resultado, ja seleciona o proximo contato 
	 * a ser mergeado.
	 */
	private void reloadDuplicatedContactsAndSelectNext() {
		Contact nextContact = null;
		if (lastMergeWasByEmail) {
			searchDuplicatecContactsByEmail();
			
			if (!duplicatedContactsByEmail.isEmpty()) {
				nextContact = duplicatedContactsByEmail.get(0);
				detailContactByEmail( nextContact.getEmail() );
			}
			
		} else if (lastMergeWasByCivilName) {
			searchDuplicatedContactsByCivilName();
			
			if (!duplicatedContactsByCivilName.isEmpty()) {
				nextContact = duplicatedContactsByCivilName.get(0);
				detailContactByCivilName( nextContact.getCivilName() );
			}
		
		} else {//by name
			searchDuplicatedContactsByName();
			if (!duplicatedContactsByName.isEmpty()) {
				nextContact = duplicatedContactsByName.get(0);
				detailContactByName( nextContact.getName() );
			}
		}

		JSFUtil.addInfoMessage("msg_next_contact_already_selected_to_merge", null);
	}
	
	
	public void detailContactByEmail(String email) {
		List<Contact> contacts = mservice.searchContactByEmail(email);
		lastMergeWasByEmail = true;
		lastMergeWasByCivilName = false;
		if (contacts.size() > 1) {
			contact1 = contacts.get(0);
			contact2 = contacts.get(1);
		}
	}

	public void detailContactByCivilName(String civilName) {
		List<Contact> contacts = mservice.searchContactByCivilName(civilName);
		lastMergeWasByEmail = false;
		lastMergeWasByCivilName = true;
		if (contacts.size() > 1) {
			contact1 = contacts.get(0);
			contact2 = contacts.get(1);
		}
	}

	public void detailContactByName(String name) {
		List<Contact> contacts = mservice.searchContactByName(name);
		lastMergeWasByEmail = false;
		lastMergeWasByCivilName = false;
		if (contacts.size() > 1) {
			contact1 = contacts.get(0);
			contact2 = contacts.get(1);
		}
	}
	
	public void considerContact1() {
		contact1 = mservice.mergeContacts(contact2, contact1);
		contact2 = null;
		JSFUtil.addInfoMessage("msg_contact_merged_sucess", null);
		reloadDuplicatedContactsAndSelectNext();
	}
	
	public void considerContact2() {
		contact2 = mservice.mergeContacts(contact1, contact2);
		contact1 = null;
		JSFUtil.addInfoMessage("msg_contact_merged_sucess", null);
		reloadDuplicatedContactsAndSelectNext();
	}
	
	
	public void copyFrom1To2() {
		copy(contact1, contact2);
	}
	
	public void copyFrom2To1() {
		copy(contact2, contact1);
	}
	
	private void copy(Contact source, Contact target) {
		target.setEmail(                source.getEmail()                );
		target.setName(                 source.getName()                 );
		target.setCivilName(            source.getCivilName()            );
		target.setTelephone1(           source.getTelephone1()           );
		target.setIdentityDocumentType( source.getIdentityDocumentType() );
		target.setIdentityDocument(     source.getIdentityDocument()     );
		target.setBirthDate(            source.getBirthDate()            );
		target.setCity(                 source.getCity()                 );
		target.setAddress(              source.getAddress()              );
		target.setCountry(              source.getCountry()              );
		target.setZipPostal(            source.getZipPostal()            );
		target.setTrainnerContact(      source.getTrainnerContact()      );
		target.setTrainnerType(         source.getTrainnerType()         );
		target.setProductorContact(     source.getProductorContact()     );
	}
	
	
	public int getMatchingRate() {
		if (contact1==null || contact2==null) {
			return 0;
		} else {

			double matchingItems = 0;
			//1
			if (isEqual(contact1.getName(), contact2.getName())) {
				matchingItems++;
			}
			//2
			if (isEqual(contact1.getCivilName(), contact2.getCivilName() )) {
				matchingItems++;
			}
			//3
			if (isEqual(contact1.getEmail(), contact2.getEmail() )) {
				matchingItems++;
			}
			//4
			if (isEqual(contact1.getTelephone1(), contact2.getTelephone1() )) {
				matchingItems++;
			}
			//5
			if (isEqual(contact1.getIdentityDocumentType(), contact2.getIdentityDocumentType() )) {
				matchingItems++;
			}
			//6
			if (isEqual(contact1.getIdentityDocument(), contact2.getIdentityDocument() )) {
				matchingItems++;
			}
			//7
			if (isEqual(contact1.getBirthDate(), contact2.getBirthDate() )) {
				matchingItems++;
			}
			//8
			if (isEqual(contact1.getAddress(), contact2.getAddress() )) {
				matchingItems++;
			}
			//9
			if (isEqual(contact1.getCity(), contact2.getCity() )) {
				matchingItems++;
			}
			//10
			if (isEqual(contact1.getCountry(), contact2.getCountry() )) {
				matchingItems++;
			}
			//11
			if (isEqual(contact1.getZipPostal(), contact2.getZipPostal() )) {
				matchingItems++;
			}
			return (int)((matchingItems / 11) * 100);
		}
		
	}
	
	
	private boolean isEqual(String s1, String s2) {
		if (s1==null && s2==null) {
			return true;
		}
		if (s1!=null && s1.equals(s2)) {
			return true;
		}
		return false;
	}
	
	private boolean isEqual(Date d1, Date d2) {
		if (d1==null && d2==null) {
			return true;
		}
		if (d1!=null && d1.equals(d2)) {
			return true;
		}
		return false;
	}
	
	private boolean isEqual(DocumentType t1, DocumentType t2) {
		if (t1==null && t2==null) {
			return true;
		}
		if (t1!=null && t1.equals(t2)) {
			return true;
		}
		return false;
	}

	
	public Contact getContact1() {
		return contact1;
	}

	public void setContact1(Contact contact1) {
		this.contact1 = contact1;
	}

	public Contact getContact2() {
		return contact2;
	}

	public void setContact2(Contact contact2) {
		this.contact2 = contact2;
	}

	public List<Contact> getDuplicatedContactsByEmail() {
		return duplicatedContactsByEmail;
	}

	public List<Contact> getDuplicatedContactsByCivilName() {
		return duplicatedContactsByCivilName;
	}

	public boolean isInitCapNewName() {
		return initCapNewName;
	}

	public void setInitCapNewName(boolean initCapNewName) {
		this.initCapNewName = initCapNewName;
	}

	public boolean isInitCapCivilName() {
		return initCapCivilName;
	}

	public void setInitCapCivilName(boolean initCapCivilName) {
		this.initCapCivilName = initCapCivilName;
	}

	public boolean isInitCapCity() {
		return initCapCity;
	}

	public void setInitCapCity(boolean initCapCity) {
		this.initCapCity = initCapCity;
	}

	public boolean isInitCapCountry() {
		return initCapCountry;
	}

	public void setInitCapCountry(boolean initCapCountry) {
		this.initCapCountry = initCapCountry;
	}

	public List<Contact> getDuplicatedContactsByName() {
		return duplicatedContactsByName;
	}


	

}

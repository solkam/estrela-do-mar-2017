package com.cb.mundo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.cb.mundo.model.entity.Account;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Facilitator;
import com.cb.mundo.model.entity.HostingSugested;
import com.cb.mundo.model.entity.Integrant;
import com.cb.mundo.model.entity.Outcoming;
import com.cb.mundo.model.entity.Participant;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.Staff;
import com.cb.mundo.model.entity.UserCB;

/**
 * DAO para realizar merge de entidades duplicadas
 * 
 * @author vitor
 * @since 26 FEV 2013
 */
public class MergeDAO {
	
	private EntityManager manager;
	
	public MergeDAO(EntityManager manager) {
		this.manager = manager;
	}
	
/* ***********************
 * Pesquisas de duplicados
 *************************/
	
	public List<Contact> searchDuplicatedContactByEmail() {
		return manager.createNamedQuery("searchDuplicatedContactByEmail", Contact.class)
				.getResultList();
	}
	
	public List<Contact> searchDuplicatedContactByCivilName() {
		return manager.createNamedQuery("searchDuplicatedContactByCivilName", Contact.class)
				.getResultList();
	}
	
	public List<Contact> searchDuplicatedContactByName() {
		return manager.createNamedQuery("searchDuplicatedContactByName", Contact.class)
				.getResultList();
	}
	
/* *****************
 * Pesquisas normais
 *******************/
	
	public List<Contact> searchContactByEmail(String email) {
		return manager.createNamedQuery("searchContactByEmail", Contact.class)
				.setParameter("pEmail", email)
				.getResultList();
	}
	
	
	public List<Contact> searchContactByCivilName(String civilName) {
		return manager.createNamedQuery("searchContactByCivilName", Contact.class)
				.setParameter("pCivilName", civilName)
				.getResultList();
	}
	
	public List<Contact> searchContactByName(String name) {
		return manager.createNamedQuery("searchContactByName", Contact.class)
				.setParameter("pName", name)
				.getResultList();
	}

/* *************************************
 * Pesquisas das dependencias de contact	
 ***************************************/
	
	//1.user
	public List<UserCB> searchUserByContact(Contact contact) {
		return manager.createNamedQuery("User.searchByContact_2", UserCB.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	//2.integrant
	public List<Integrant> searchIntegrantByContact(Contact contact) {
		return manager.createNamedQuery("Integrant.searchByContact_2", Integrant.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	//3.outcoming
	public List<Outcoming> searchOutcomingByContact(Contact contact) {
		return manager.createNamedQuery("Outcoming.searchByContact_2", Outcoming.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	//4.staff
	public List<Staff> searchStaffByContact(Contact contact) {
		return manager.createNamedQuery("Staff.searchByContact_2", Staff.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	//5.facilitator
	public List<Facilitator> searchFacilitatorByContact(Contact contact) {
		return manager.createNamedQuery("Facilitator.searchByContact_2", Facilitator.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	//6.participant
	public List<Participant> searchParticipantByContact(Contact contact) {
		return manager.createNamedQuery("Participant.searchByContact_2", Participant.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	//7.register
	public List<Register> searchRegisterByContact(Contact contact) {
		return manager.createNamedQuery("Register.searchByContact_2", Register.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	//8.trainned
	public List<Contact> searchTrainedContactByContact(Contact contact) {
		return manager.createNamedQuery("ContactTrainned.searchByContact", Contact.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	//9.producted
	public List<Contact> searchProductedContactByContact(Contact contact) {
		return manager.createNamedQuery("ProductedContact.searchByContact", Contact.class)
				.setParameter("pContact", contact)
				.getResultList();
	}

	//10.account
	public Account findAccountByContact(Contact contact) {
		try {
			return manager.createNamedQuery("searchAccountByContact", Account.class)
					.setParameter("pContact", contact)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Account findOrCreateAccountByContact(Contact contact) {
		Account account = findAccountByContact(contact);
		if (account==null) {
			account = new Account();
			account.setContact(contact);
			account.setId( contact.getId() );//o ID de account é o mesmo ID de contact
			account = manager.merge( account );
		}
		return account;
	}

	
	public List<HostingSugested> searchHostingSugestedByContact(Contact contact) {
		return manager.createNamedQuery("searchHostingSugestedByContact", HostingSugested.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	
	
	

}

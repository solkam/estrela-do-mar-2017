package com.cb.mundo.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.cb.mundo.model.dao.MergeDAO;
import com.cb.mundo.model.entity.Account;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.util.CalendarUtil;

/**
 * EJB responsavel pelo merge de contatos e usuarios
 * 
 * @author vitor
 * @since 26 FEV 2013
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class MergeService {
	
	@PersistenceContext
	private EntityManager manager;
	
	@Resource
	private UserTransaction tx;
	
	private MergeDAO mergeDAO;


	@PostConstruct void init() {
		mergeDAO = new MergeDAO(manager);
	}


	
	public List<Contact> searchDuplicatedContactByEmail() {
		return mergeDAO.searchDuplicatedContactByEmail();
	}


	
	public List<Contact> searchDuplicatedContactByCivilName() {
		return mergeDAO.searchDuplicatedContactByCivilName();
	}

	
	public List<Contact> searchDuplicatedContactByName() {
		List<Contact> contacts = mergeDAO.searchDuplicatedContactByName();
		return contacts;
	}

	
	
	public List<Contact> searchContactByEmail(String email) {
		return mergeDAO.searchContactByEmail(email);
	}
	
	
	public List<Contact> searchContactByCivilName(String civilName) {
		return mergeDAO.searchContactByCivilName(civilName);
	}
	
	
	public List<Contact> searchContactByName(String name) {
		return mergeDAO.searchContactByName(name);
	}
	

	
	
	
	
	/**
	 * Pesquisa por todas as dependencias do contato velho e 
	 * substitui pelo contato novo
	 * 
	 * @param oldContact
	 * @param newContact
	 * @return
	 */
	public Contact mergeContacts(Contact oldContact, Contact newContact) {
		
		try {
			
			//1.user
			tx.begin();
			manager.createQuery("update UserCB set contact=:pNewContact where contact=:pOldContact")
				.setParameter("pOldContact", oldContact)
				.setParameter("pNewContact", newContact)
				.executeUpdate();
			tx.commit();
			
			//2.integrant
			tx.begin();
			manager.createQuery("update Integrant set contact=:pNewContact where contact=:pOldContact")
				.setParameter("pOldContact", oldContact)
				.setParameter("pNewContact", newContact)
				.executeUpdate();
			tx.commit();
			
			
			//3.outcoming
			tx.begin();
			manager.createQuery("update Outcoming set responsable=:pNewContact where responsable=:pOldContact")
				.setParameter("pOldContact", oldContact)
				.setParameter("pNewContact", newContact)
				.executeUpdate();
			tx.commit();
			
			
			//4.staff
			tx.begin();
			manager.createQuery("update Staff set contact=:pNewContact where contact=:pOldContact")
			.setParameter("pOldContact", oldContact)
			.setParameter("pNewContact", newContact)
			.executeUpdate();
			tx.commit();
			
			//5.facilitator
			tx.begin();
			manager.createQuery("update Facilitator set contact=:pNewContact where contact=:pOldContact")
			.setParameter("pOldContact", oldContact)
			.setParameter("pNewContact", newContact)
			.executeUpdate();
			tx.commit();
			
			
			//6.1.participant
			tx.begin();
			manager.createQuery("update Participant set contact=:pNewContact where contact=:pOldContact")
			.setParameter("pOldContact", oldContact)
			.setParameter("pNewContact", newContact)
			.executeUpdate();
			
			//6.2.participant (quem registrou)
			manager.createQuery("update Participant set registerContact=:pNewContact where registerContact=:pOldContact")
			.setParameter("pOldContact", oldContact)
			.setParameter("pNewContact", newContact)
			.executeUpdate();
			tx.commit();
	
			
			//7.contact trainner
			tx.begin();
			manager.createQuery("update Contact set trainnerContact=:pNewContact where trainnerContact=:pOldContact")
			.setParameter("pOldContact", oldContact)
			.setParameter("pNewContact", newContact)
			.executeUpdate();
			tx.commit();
			
			
			//8.contact productor
			tx.begin();
			manager.createQuery("update Contact set productorContact=:pNewContact where productorContact=:pOldContact")
			.setParameter("pOldContact", oldContact)
			.setParameter("pNewContact", newContact)
			.executeUpdate();
			tx.commit();
			
			
			//9.contact creator
			//(nao tem mais esta coluna)
			
	
			//10.Hosting Suggested
			tx.begin();
			manager.createQuery("update HostingSugested set contact=:pNewContact where contact=:pOldContact")
			.setParameter("pOldContact", oldContact)
			.setParameter("pNewContact", newContact)
			.executeUpdate();
			tx.commit();
		
	
			//11.city team member
			tx.begin();
			manager.createQuery("update CityTeamMember set contact=:pNewContact where contact=:pOldContact")
				.setParameter("pOldContact", oldContact)
				.setParameter("pNewContact", newContact)
				.executeUpdate();
			tx.commit();
	
	
			//12.1.register
			tx.begin();
			manager.createQuery("update Register set contact=:pNewContact where contact=:pOldContact")
			.setParameter("pOldContact", oldContact)
			.setParameter("pNewContact", newContact)
			.executeUpdate();
			tx.commit();
	
			
			//12.2.account e register e credits
			try {
				//busca conta antiga (se existir)
				Account oldAccount = manager.createQuery("select a from Account a where a.contact=:pOldContact", Account.class)
								.setParameter("pOldContact", oldContact)
								.getSingleResult();//se nao existir,lancar ex
				

				//busca conta nova e se nao existir, cria
				Account newAccount = null;
				tx.begin();
				try {
					newAccount = manager.createQuery("select a from Account a where a.contact=:pNewContact", Account.class)
							.setParameter("pNewContact", newContact)
							.getSingleResult();
				}catch(NoResultException e) {
					newAccount = new Account();
					newAccount.setContact(newContact);
					newAccount = manager.merge( newAccount );
				}
				
				//atualiza register...
				manager.createQuery("update Register set account=:pNewAccount where account=:pOldAccount")
					.setParameter("pOldAccount", oldAccount)
					.setParameter("pNewAccount", newAccount)
					.executeUpdate();
	
				//atualiza credits...
				manager.createQuery("update RegisterCredit set account=:pNewAccount where account=:pOldAccount")
					.setParameter("pOldAccount", oldAccount)
					.setParameter("pNewAccount", newAccount)
					.executeUpdate();
				tx.commit();
				
				//finalmente, remove conta antiga
				tx.begin();
				manager.createQuery("delete from Account where id=:pId")
					.setParameter("pId", oldAccount.getId() )
					.executeUpdate();
				tx.commit();
	
			}catch(NoResultException e) {
				//nao existe conta antiga e nao nada para fazer
			}

			
			//FINAL: remove contact antigo
			tx.begin();
			manager.remove( manager.merge(oldContact) );
			tx.commit();

			//retorna
			return newContact;
			
		}catch(Exception e) {
			try {
				tx.rollback();
			}catch(Exception e2) {
			}
			
			throw new RuntimeException(e);
		}

	}
	
	
	
//	public Contact mergeContacts(Contact oldContact, Contact newContact) {
//		//tras os dois para gerenciado...
//		oldContact = manager.find(Contact.class, oldContact.getId() );
//		newContact = manager.find(Contact.class, newContact.getId() );
//		
//		//1.user
//		for (UserCB user : mergeDAO.searchUserByContact(oldContact) ){
//			user.setContact(newContact);
//		}
//		manager.joinTransaction();
//		
//		
//		//2.integrant
//		for (Integrant integrant : mergeDAO.searchIntegrantByContact(oldContact) ){
//			integrant.setContact(newContact);
//		}
//		manager.joinTransaction();
//		
//		
//		//3.outcoming
//		for (Outcoming outcoming : mergeDAO.searchOutcomingByContact(oldContact) ) {
//			outcoming.setResponsable(newContact);
//		}
//		manager.joinTransaction();
//		
//		
//		//4.staff
//		for (Staff staff : mergeDAO.searchStaffByContact(oldContact) ) {
//			staff.setContact(newContact);
//		}
//		manager.joinTransaction();
//		
//		//5.facilitator
//		for (Facilitator facilitator : mergeDAO.searchFacilitatorByContact(oldContact) ) {
//			facilitator.setContact(newContact);
//		}
//		manager.joinTransaction();
//		
//		
//		//6.participant
//		for (Participant participant : mergeDAO.searchParticipantByContact(oldContact) ) {
//			participant.setContact(newContact);
//		}
//		manager.joinTransaction();
//		
//		
//		//7.trainner
//		for (Contact trainnee : mergeDAO.searchTrainedContactByContact(oldContact)) {
//			trainnee.setTrainnerContact(newContact);
//		}
//		manager.joinTransaction();
//		
//		
//		//8.Producted
//		for (Contact producted : mergeDAO.searchProductedContactByContact(oldContact)) {
//			producted.setProductorContact(newContact);
//		}
//		manager.joinTransaction();
//		
//		
//		//9.Contact creator
//		for (Contact created : mergeDAO.searchCreatedContactByContact(oldContact)) {
//			created.setCreatedByContact( newContact );
//		}
//		manager.joinTransaction();
//		
//		//10.Account, Register e Credits
//		
//		//10.1 Acount
//		Account oldAccount = mergeDAO.findAccountByContact(oldContact);
//		Account newAccount = mergeDAO.findOrCreateAccountByContact(newContact);
//		if (oldAccount!=null) {
//			//transfere todos os creditos do velho contato para o novo
//			for (RegisterCredit credit : oldAccount.getCredits()) {
//				credit.setAccount(newAccount);
//			}
//		}
//
//		//10.1.register
//		for (Register register : mergeDAO.searchRegisterByContact(oldContact) ) {
//			register.setContact( newContact );
//			register.setAccount( newAccount );//para pode apagar a velha conta
//		}
//		
//		//10.2.remove a velha conta para poder remover o velho contato
//		if (oldAccount!=null) {
//			manager.remove( oldAccount );
//		}
//		manager.joinTransaction();
//		
//		
//		//11.Hosting Suggested
//		for (HostingSugested hostingSugested : mergeDAO.searchHostingSugestedByContact(oldContact)) {
//			hostingSugested.setContact( newContact );
//		}
//		manager.joinTransaction();
//			
//		//remove contact antigo
//		manager.remove( oldContact );
//		
//		
//		//retorna
//		return newContact;
//	}
	
	
	
	
	/**
	 * Classe auxiliar para slide contact
	 * @author Solkam
	 *
	 */
	private class SlipeContactHelper {
		private final Contact oldContact;
		private final Contact newContact;

		public SlipeContactHelper(Contact oldContact, Contact newContact) {
			this.oldContact = oldContact;
			this.newContact = newContact;
		}

		public Contact getOldContact() {
			return oldContact;
		}

		public Contact getNewContact() {
			return newContact;
		}
	}//fim class
	
	/**
	 * Seleciona contatos dentro de um range de ID (id_inicial e id_final) e 
	 * @param idInitial
	 * @param idFinal
	 * @param newIdInicial
	 */
	
	public void slideContacts(Long idInitial, Long idFinal) {
		verifyParameters(idInitial, idFinal);
		
		System.out.println("Iniciando Slide de Contact: "+CalendarUtil.getTimeAsString() );

		List<SlipeContactHelper> helpers = new ArrayList<>();
		
		//seleciona os contatos
		List<Contact> contactsRanged = pesquisarContactByRangeIds(idInitial, idFinal);

		//para cada contato:
			//criar um novo o id = newIdInicial++
			//cria instancia do helper
			//adcionar helper na lista de helpers
		
		int countClone = 0;
		System.out.println("..clonando contactos: "+CalendarUtil.getTimeAsString() );
		for (Contact oldContact : contactsRanged) {
			
			Contact newContact = oldContact.builtTransientClone();
			newContact = manager.merge( newContact );
			
			SlipeContactHelper helper = new SlipeContactHelper(oldContact, newContact);
			helpers.add( helper );

			if (++countClone % 10 == 0) {
				System.out.println("....10 contatos terminaram de ser clonados: "+CalendarUtil.getTimeAsString() );
			}
		}

		
		//para cada helper da lista
			//invoca o merge
		int countMerge = 0;
		System.out.println("..mergeando antigos contactos: "+CalendarUtil.getTimeAsString() );
		for (SlipeContactHelper helper : helpers) {
			mergeContacts(helper.getOldContact(), helper.getNewContact());

			if (--countMerge % 10 == 0) {
				System.out.println("....10 contatos acabaram de ser mergeados: "+CalendarUtil.getTimeAsString() );
			}
		}
		
		System.out.println("Finalizado Slide de Contact: "+CalendarUtil.getTimeAsString() );
	}
	
	private void verifyParameters(Long idInitial, Long idFinal) {
//		//1.
//		if (idFinal >= newIdInicial) {
//			throw new IllegalArgumentException("ID final esta conflitando com novo ID inicial");
//		}
		
		//2.se novo id eh menor que o max(id)
//		Long maxIDContact = findMaxIDContact();
//		if (newIdInicial <= maxIDContact) {
//			throw new IllegalArgumentException("Novo ID eh menor que o MAX de id no banco");
//		}
	}
		
//	
//	private Long findMaxIDContact() {
//		String jpql = "select max(c.id) from Contact c";
//		return manager.createQuery(jpql, Long.class)
//				.getSingleResult();
//	}
//	
//	
//	

	public List<Contact> pesquisarContactByRangeIds(Long idInitial, Long idFinal) {
		String jqpl = "select c from Contact c where c.id >= :pIdInitial and c.id <= :pIdFinal order by c.id";
		return manager.createQuery(jqpl, Contact.class)
				.setParameter("pIdInitial", idInitial)
				.setParameter("pIdFinal", idFinal)
				.getResultList();
	}
	
	

}

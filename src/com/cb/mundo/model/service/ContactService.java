package com.cb.mundo.model.service;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.dao.ContactDAO;
import com.cb.mundo.model.dao.FacilitatorDAO;
import com.cb.mundo.model.dao.ProductionDAO;
import com.cb.mundo.model.dao.RegisterDAO;
import com.cb.mundo.model.dao.UserDAO;
import com.cb.mundo.model.entity.Account;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.ContactObservation;
import com.cb.mundo.model.entity.Profession;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.exception.BusinessException;
import com.cb.mundo.model.exception.ContactEmailAlreadyExistException;
import com.cb.mundo.model.exception.ContactHasAccountAndCreditsException;
import com.cb.mundo.model.exception.ContactNewNameAlreadExistException;
import com.cb.mundo.model.exception.ContactReferenceByProductedException;
import com.cb.mundo.model.exception.ContactReferencedByFacilitatorException;
import com.cb.mundo.model.exception.ContactReferencedByIntegrantException;
import com.cb.mundo.model.exception.ContactReferencedByOutcomingException;
import com.cb.mundo.model.exception.ContactReferencedByParticipantException;
import com.cb.mundo.model.exception.ContactReferencedByRegisterException;
import com.cb.mundo.model.exception.ContactReferencedByStaffException;
import com.cb.mundo.model.exception.ContactReferencedByTrainnedException;
import com.cb.mundo.model.exception.ContactReferencedByUserException;
import com.cb.mundo.model.exception.ProfessionNameAlreadyExistsException;

/**
 * Servicos de Negocio para Contact
 * @author Solkam
 * @since 11 OUT 2011
 */
@Stateless
public class ContactService  {

	@PersistenceContext
	private EntityManager manager;

	private ContactDAO contactDAO;

	private UserDAO userDAO;

	private FacilitatorDAO facilitatorDAO;

	private ProductionDAO productionDAO;
	
	private RegisterDAO registerDAO;

	@PostConstruct
	protected void init() {
		contactDAO = new ContactDAO(manager);
		productionDAO = new ProductionDAO(manager);
		userDAO = new UserDAO(manager);
		facilitatorDAO = new FacilitatorDAO(manager);
		registerDAO = new RegisterDAO(manager);
	}

	/**
	 * Salva contact aplicando RNs e definido 
	 * quem criou ou atualizou
	 * @param contact
	 * @return
	 */
	public Contact saveContact(Contact contact, UserCB autenticatedUser) {
		//RNs
		verifyNameIsNotNullAndUnique(contact);
		verifyEmailIsUnique(contact);
		//logs
		applyContactLogs( contact, autenticatedUser );
		//e salva!
		return manager.merge( contact );
	}

	
	
	/**
	 * Define as informacoes de log qdo se salva o usuario.
	 * Existe um unico caso onde autenticatedUser vem null que eh
	 * quando se está criado o proprio usuario.
	 * @param contact
	 * @param autenticatedUser
	 */
	private void applyContactLogs(Contact contact, UserCB autenticatedUser) {
		String contactShortDesc = null;
		if (autenticatedUser!=null){
			contactShortDesc = autenticatedUser.getContact().getShortDesc();
			
		} else {//qdo se está criado o proprio usuario
			contactShortDesc = contact.getShortDesc();
		}
		
		if (contact.isTransient()) {
			contact.setCreatedBy( contactShortDesc );
			contact.setCreateDate( new Date() );
		} else {
			contact.setUpdatedBy( contactShortDesc );
			contact.setUpdateDate( new Date() );
		}
	}

	/**
	 * Se nome novo for preenchido, ele devera ser inico.
	 * @throws ContactNewNameAlreadExistException
	 */
	private void verifyNameIsNotNullAndUnique(Contact c) {
		if (c.getName() != null && !c.getName().trim().isEmpty() ) {
			
			Contact alreadyExistContact = contactDAO.findContactByNameUnsafely(c.getName());
			if (alreadyExistContact != null && !alreadyExistContact.equals(c)) {
				throw new ContactNewNameAlreadExistException();
			}
		}
	}

	/**
	 * Verify if email is unique
	 * 
	 * @param c
	 */
	private void verifyEmailIsUnique(Contact c) {
		Contact alreadExistContact = contactDAO.findContactByEmail(c.getEmail());

		if (alreadExistContact != null && !alreadExistContact.equals(c)) {
			throw new ContactEmailAlreadyExistException(c.getEmail());
		}
	}


	/**
	 * Remove um contact verificando RNs das associacoes
	 * @param contact
	 * @throws BusinessException
	 */
	public void removeContact(Contact contact) throws BusinessException {
		//1.traz para gerenciado
		contact = manager.find(Contact.class, contact.getId() );
		//2.RNs
		checkStaffReference(contact);
		checkIntegrantReference(contact);
		checkParticipantReference(contact);
		checkTrainnedContactReference(contact);
		checkProductedContactReference(contact);
		checkOutcomingReference(contact);
		checkUserReference(contact);
		checkFacilitatorReference(contact);
		checkRegisterContact(contact);
		checkAccountAndCredits(contact);
		//3.se tudo ok, remove
		manager.remove( contact );
	}

	
	/**
	 * Verifica se tem creditos. Caso nao tenha, eliminada a conta.
	 * @param c
	 */
	private void checkAccountAndCredits(Contact c) {
		Account account = registerDAO.findAccountByContact(c);
		
		if (account!=null) {
			if (account.getCredits().isEmpty()) {
				manager.remove( account );
			} else {
				throw new ContactHasAccountAndCreditsException(c);
			}
		}
	}


	// business rules for remove contact:
	
	private void checkStaffReference(Contact contact) {
		if (!contact.getStaffs().isEmpty() ) {
			throw new ContactReferencedByStaffException();
		}
	}

	private void checkIntegrantReference(Contact contact) {
		if (!contact.getIntegrants().isEmpty()) {
			throw new ContactReferencedByIntegrantException();
		}
	}

	private void checkParticipantReference(Contact contact) {
		if (!contact.getParticipants().isEmpty()) {
			throw new ContactReferencedByParticipantException();
		}
	}

	private void checkProductedContactReference(Contact contact) {
		if (!contact.getProdutedOnes().isEmpty()) {
			throw new ContactReferenceByProductedException();
		}
	}
	
	private void checkTrainnedContactReference(Contact contact) {
		if (!contact.getTrainnedOnes().isEmpty() ) {
			throw new ContactReferencedByTrainnedException();
		}
	}


	private void checkOutcomingReference(Contact c) {
		if (!productionDAO.searchOutcomingByContact(c).isEmpty()) {
			throw new ContactReferencedByOutcomingException();
		}
	}

	private void checkUserReference(Contact c) {
		if (!userDAO.searchUserByContact(c).isEmpty()) {
			throw new ContactReferencedByUserException();
		}
	}

	private void checkFacilitatorReference(Contact c) {
		if (!facilitatorDAO.searchFacilitatorByContact(c).isEmpty()) {
			throw new ContactReferencedByFacilitatorException();
		}
	}
	
	private void checkRegisterContact(Contact c) {
		if (!registerDAO.searchRegistersByContact(c).isEmpty() ) {
			throw new ContactReferencedByRegisterException();
		}
	}

	/**
	 * Removendo contact de maneira forçada
	 * 1) Seta todos os campos como 'Contato Excluido'
	 * 2) Remove o User
	 * 3) Set null os produzidos
	 * 4) Set null os trainados
	 * 5) Remove as observacoes
	 * Nota: para staff, participant e integrant, NAO mexe na referencia
	 * @param contact
	 */
	public void removeContactForced(Contact contact) {
		final String TXT_EXCLUDED = "Contato EXCLUIDO";
		
		//seta todos os campos
		contact.setCivilName(TXT_EXCLUDED);
		contact.setEmail(TXT_EXCLUDED);
		contact.setTelephone1(TXT_EXCLUDED);
		
		contact.setName( null );
		contact.setIdentityDocument( null );
		contact.setIdentityDocumentType( null );
		contact.setBirthDate( new Date() );
		contact.setGender( null );
		contact.setProfessions( null );
		
		contact.setAddress( null );
		contact.setNeighborhood( null );
		contact.setCity( null );
		contact.setState( null );
		contact.setCountry( null );
		contact.setZipPostal( null );
		contact.setTelephone2( null );
		contact.setTelephone3( null );
		contact.setSkype( null );
		contact.setIndicateBy( null );
		contact.setTrainnerContact( null );
		contact.setProductorContact( null );
		contact.setTrainnerType( null );
		contact.setFlagProductor( false );
		contact.setFlagConsultant( false );
		contact.setTshirtSize( null );
		contact.setCompanyName( null );
		contact.setRootSchool( null );
		contact = manager.merge( contact );
	}
	
	public void removeUserForcedByContact(Contact contact) {
		UserCB user = userDAO.findUserByContact(contact);
		if (user!=null) {
			manager.remove( user );
		}
	}
	
	public void removeProducedOneForcedByContact(Contact contact) {
		contact = manager.find(Contact.class, contact.getId() );
		for (Contact produzedVar : contact.getProdutedOnes()) {
			produzedVar.setProductorContact( null );
		}
	}

	public void removeTrainnedOnesForcedByContact(Contact contact) {
		contact = manager.find(Contact.class, contact.getId() );
		for (Contact trainedVar : contact.getTrainnedOnes()) {
			trainedVar.setTrainnerContact( null );
		}
	}
	
	public void removeObservationsForcedByContact(Contact contact) {
		contact = manager.find(Contact.class, contact.getId() );
		for (ContactObservation obsVar : contact.getObservations()) {
			manager.remove( obsVar );
		}
	}
	
	
	
	public Contact findContactByName(String name) {
		return contactDAO.findContactByName(name);
	}

	
	public Contact findContactById(Long id) {
		return contactDAO.findContactById(id);
	}

	
	public Contact findContactByEmail(String email) {
		return contactDAO.findContactByEmail(email);
	}

	
	public List<Contact> searchAllContacts() {
		return contactDAO.searchAllContacts();
	}

	
	public List<Contact> searchContactByNameAndCivilNameAndEmail(String name,
			String civilName, String email) {
		return contactDAO.searchContactByNameAndCivilNameAndEmail(name,
				civilName, email);
	}

	
	public List<Contact> searchContactByNameOrCivilNameOrEmail(String name,	String civilName, String email) {
		return contactDAO.searchContactByNameOrCivilNameOrEmail(name, civilName, email);
	}

	
	public List<Contact> searchContactByCivilNameOrNewName(String name) {
		return contactDAO.searchContactByCivilNameOrNewName(name);
	}

	
	public List<Contact> searchContactByCivilNameOrNameOrEmailOrCity(String keyword) {
		return contactDAO.searchContactByCivilNameOrNameOrEmailOrCity(keyword);
	}

	
	public List<Contact> searchProductorContactByCivilNameOrNewName(String name) {
		return contactDAO.searchProductorContactByCivilNameOrNewName(name);
	}

	
	public List<Contact> searchTrainnerContactByCivilNameOrNewName(String name) {
		return contactDAO.searchTrainnerContactByCivilNameOrNewName(name);
	}

	/**
	 * Cria e salva um usuario pre-definido (com os campos obrigatorios
	 * preenchidos e email) Usado para inscricoes onde se necesita de um contato
	 * mas seus dados ainda serao fornecidos num wizard.
	 * 
	 * @return contact
	 */
	
	public Contact createPredefinedContact(String email) {
		Contact predefinedContact = new Contact();
		predefinedContact.fillRequiredAttributes();
		predefinedContact.setEmail(email);
		return manager.merge( predefinedContact );
	}

	
	public Contact refreshContact(Contact contact) {
		//traz para gerenciado
		contact = manager.find(Contact.class,  contact.getId() );
		//forca carga dos produzidos
		contact.getProdutedOnes().size();
		contact.getObservations().size();
		//account só criado qdo um contato se inscrive num mega evento
		if (contact.getAccount()!=null) {
			contact.getAccount().getCredits().size();
		}
		return contact;
	}

/*
 * Profession	
 ************/

	
	public Profession saveProfession(Profession profession) {
		verifyProfessionNameIsUnique(profession);
		return manager.merge( profession );
	}
	

	/**
	 * Verifica se nome da profissao ja foi cadastrado
	 * @param profession
	 */
	private void verifyProfessionNameIsUnique(Profession profession) {
		Profession professionFound = contactDAO.findProfessionByName(profession.getName());
		if (professionFound!=null && !profession.equals(professionFound)) {
			throw new ProfessionNameAlreadyExistsException(profession);
		}
	}

	
	public List<Profession> searchActiveProfession() {
		return contactDAO.searchActiveProfession();
	}
	
	
	
	public List<Profession> searchProfession() {
		return contactDAO.searchProfession();
	}

	
	public Profession findProfessionById(Long id) {
		return manager.find(Profession.class, id);
	}
	
	
	
	public List<Profession> searchProfessionByContact(Contact contact) {
		return contactDAO.searchProfessionByContact(contact);
	}
	
        

	/* ***********
	 * Observacoes
	 *************/
	
	/**
	 * Salva uma observacao de contato
	 * @param obs
	 * @return
	 */
    public ContactObservation saveContactObservation(ContactObservation obs){
        return manager.merge(obs);
    }
        
        
    /**
     * Remove um observacao
     * @param obs
     */
    public void removeContactObservation(ContactObservation obs){
        obs = manager.find(ContactObservation.class, obs.getId() );
        manager.remove(obs);
    }

    /**
     * Seleciona os contatos que estão configurados como produtores de si proprio,
     * seja como produtor 1 ou 2.
     * (usado como pre-processamento do relatorio de comissões)
     * @return
     */
	public int removeAutoProductor() {
		List<Contact> contacts = manager.createNamedQuery("searchContactAutoProductor", Contact.class)
			.getResultList();
		
		for (Contact contactVar : contacts) {
			if (contactVar.equals( contactVar.getProductorContact() )) {
				contactVar.setProductorContact( null );
			}
			if (contactVar.equals( contactVar.getProductorContact2() )) {
				contactVar.setProductorContact2( null );
			}
		}
		return contacts.size();
	}
    
    

}

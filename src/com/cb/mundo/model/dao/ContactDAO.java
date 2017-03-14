package com.cb.mundo.model.dao;

import static com.cb.mundo.model.util.QueryUtil.isNotBlank;
import static com.cb.mundo.model.util.QueryUtil.toLikeMatchModeANY;
import static com.cb.mundo.model.util.QueryUtil.toLikeMatchModeSTART;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Profession;

public class ContactDAO {
	
	private DAO<Contact> contactDao;
	private EntityManager manager;
	
	public ContactDAO(EntityManager manager) {
		this.manager = manager;
		contactDao = new JavaxPersistenceDAO<Contact>(manager, Contact.class);
	}

	
//contacts...
	
	public Contact findContactById(Serializable id) {
		return contactDao.findById(id);
	}
	

	
	/**
	 * De maneira seguro, busca contact pelo nome.
	 * @param name
	 * @return
	 */
	public Contact findContactByName(String name) {
		Contact contact = new Contact(); //sempre retorna um instancia valida
		if (name!=null && !name.isEmpty()) {
			try {
				contact = manager.createNamedQuery("Contact.findByName", Contact.class)
						.setParameter("pName", name.trim().toLowerCase())
						.getSingleResult();
				
			} catch (NoResultException e) {	}
		}
		return contact;
	}
	
	
	/**
	 * Busca contact pelo nome civil retornando null caso nao encontre
	 * (por isso unsafely)
	 * @param civilName
	 * @return
	 */
	public Contact findContactByCivilNameUnsafely(String civilName) {
		Contact contact = null;//se nao encontrar nada, retorna null
		if (civilName!=null && !civilName.isEmpty()) {
			try {
				contact = manager.createNamedQuery("Contact.findByCivilName", Contact.class)
							.setParameter("pCivilName", civilName.toLowerCase())
							.getSingleResult();
			
			}catch(NoResultException e) {}
		}
		return contact;
	}
	
	
	/**
	 * Busca contact pelo email retornando null caso nao encontre
	 * @param email
	 * @return
	 */
	public Contact findContactByEmail(String email) {
		Contact contact = null;
		if (email!=null && !email.isEmpty()) {
			try {
				contact = manager.createNamedQuery("Contact.findByEmail", Contact.class)
							.setParameter("pEmail", email.trim().toLowerCase() )
							.getSingleResult();
				
			}catch(NoResultException e) { }
		}
		return contact;
	}
	
	/**
	 * Busca contact pelo nome novo retornando null se nao encontrar
	 * @param name
	 * @return contact
	 */
	public Contact findContactByNameUnsafely(String name) {
		Contact contact = null;
		if (name!=null && !name.isEmpty()) {
			try {
				contact = manager.createNamedQuery("Contact.findByName", Contact.class)
							.setParameter("pName", name)
							.getSingleResult();
				
			}catch(NoResultException e) {}
		}
		return contact;
	}
	


	/**
	 * Pesquisar contatos por trechos do nome
	 * @param name
	 * @return
	 */
	public List<Contact> searchContactByCivilNameOrNewName(String name) {
		List<Contact> contacts = new ArrayList<Contact>();
		if (name!=null && !name.isEmpty()) {
			name = name.toLowerCase();
			contacts = manager.createNamedQuery("Contact.searchByCivilNameOrNewName", Contact.class)
						.setParameter("pNewName"  , name+'%' )
						.setParameter("pCivilName", name+'%' )
						.getResultList();
		}
		return contacts;
	}
	
	/**
	 * Pesquisa contatos pelo nome civil ou nome novo ou email ou cidade
	 * Funcionalidade: pesquisar por palavra chave em config de contact
	 * @param keyword
	 * @return
	 */
	public List<Contact> searchContactByCivilNameOrNameOrEmailOrCity(String keyword) {
		List<Contact> contacts = new ArrayList<Contact>();
		if (keyword!=null && !keyword.trim().isEmpty()) {
			keyword = keyword.toLowerCase();
			contacts = manager.createNamedQuery("Contact.searchByCivilNameOrNameOrEmailOrCity", Contact.class)
						.setParameter("pCivilName", keyword+"%")
						.setParameter("pName"     , keyword+"%")
						.setParameter("pEmail"    , keyword+"%")
						.setParameter("pCity"     , keyword+"%")
						.getResultList();
		}
		return contacts;
	}
	
	/**
	 * Pesquisa por produtores (contact com flag productor) usando o nome civil ou novo
	 * @param name
	 * @return
	 */
	public List<Contact> searchProductorContactByCivilNameOrNewName(String name) {
		List<Contact> contacts = new ArrayList<Contact>();
		if (name!=null && !name.isEmpty()) {
			contacts = manager.createNamedQuery("Contact.searchProductorByCivilNameOrNewName", Contact.class)
						.setParameter("pNewName"  , name.toLowerCase()+'%' )
						.setParameter("pCivilName", name.toLowerCase()+'%' )
						.getResultList();
		}
		return contacts;
	}
	
	
	/**
	 * Seleciona os treinado (contact com trainnerType not null).
	 * @param name
	 * @return
	 */
	public List<Contact> searchTrainnerContactByCivilNameOrNewName(String name) {
		List<Contact> contacts = new ArrayList<Contact>();
		if (name!=null && !name.isEmpty()) {
			contacts = manager.createNamedQuery("Contact.searchTrainnerByCivilNameOrNewName", Contact.class)
						.setParameter("pNewName"  , name.toLowerCase()+'%' )
						.setParameter("pCivilName", name.toLowerCase()+'%' )
						.getResultList();
		}
		return contacts;
	}

	
	
	/**
	 * Usando criteria, pesquisar por contatos 
	 * @param name
	 * @param civilName
	 * @param email
	 * @return
	 */
	public List<Contact> searchContactByNameAndCivilNameAndEmail(String name, String civilName, String email) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Contact> criteria = builder.createQuery(Contact.class);
		
		Root<Contact> root = criteria.from(Contact.class);
		Predicate conjunction = builder.conjunction();
		if (isNotBlank(name)) {
			conjunction = builder.and(conjunction,
				builder.like(root.<String>get("name"), toLikeMatchModeANY(name))	
			);
		}
		if (isNotBlank(civilName)) {
			conjunction = builder.and(conjunction,
				builder.like(root.<String>get("civilName"), toLikeMatchModeANY(civilName))	
			);
		}
		if (isNotBlank(email)) {
			conjunction = builder.and(conjunction,
				builder.like(root.<String>get("email"), toLikeMatchModeSTART(email))	
			);
		}
		criteria.where(conjunction);
		return manager.createQuery(criteria).getResultList();
	}
	

	/**
	 * Pesquisa contact pelo nome novo ou nome civil ou email fazendo um distinct
	 * @param name
	 * @param civilName
	 * @param email
	 * @return
	 */
	public List<Contact> searchContactByNameOrCivilNameOrEmail(String name, String civilName, String email) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Contact> criteria = builder.createQuery(Contact.class);
		
		Root<Contact> root = criteria.from(Contact.class);
		Predicate disjunction = builder.disjunction();
		if (isNotBlank(name)) {
			disjunction = builder.or(disjunction,
				builder.like(root.<String>get("name"), toLikeMatchModeANY(name))	
			);
		}
		if (isNotBlank(civilName)) {
			disjunction = builder.or(disjunction,
				builder.like(root.<String>get("civilName"), toLikeMatchModeANY(civilName))	
			);
		}
		if (isNotBlank(email)) {
			disjunction = builder.or(disjunction,
				builder.like(root.<String>get("email"), toLikeMatchModeSTART(email))	
			);
		}
		
		criteria.select(root).distinct(true);
		criteria.where(disjunction);
		return manager.createQuery(criteria).getResultList();
	}

	
	public List<Contact> searchAllContacts() {
		return manager.createNamedQuery("Contact.searchAll", Contact.class)
					.getResultList();
	}
	
	
	/**
	 * Pesquisa contatos treinados pelo treinador
	 * @param c
	 * @return
	 */
	public List<Contact> searchContactByTrainner(Contact trainner) {
		return manager.createNamedQuery("Contact.searchByTrainner", Contact.class)
				.setParameter("pTrainner", trainner)
				.getResultList();
	}
	
	/**
	 * Pesquisa os contatos sob um produtor
	 * @param productor
	 * @return
	 */
	public List<Contact> searchContactByProductor(Contact productor) {
		return manager.createNamedQuery("Contact.searchByProductor", Contact.class)
				.setParameter("pProductor", productor)
				.getResultList();
	}
	
/*
 * Profession
 */
	public List<Profession> searchProfession() {
		return manager.createNamedQuery("searchProfession", Profession.class)
				.getResultList();
	}

	public List<Profession> searchActiveProfession() {
		return manager.createNamedQuery("searchActiveProfession", Profession.class)
				.getResultList();
	}

	
	public Profession findProfessionByName(String name) {
		try {
			return manager.createNamedQuery("findProfessionByName", Profession.class)
					.setParameter("pName", name)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}


	public List<Profession> searchProfessionByContact(Contact contact) {
		return manager.createNamedQuery("searchProfessionByContact", Profession.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	
}

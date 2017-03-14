package com.cb.mundo.model.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.dao.ProductionDAO;
import com.cb.mundo.model.dto.FinancialAdjustSummary;
import com.cb.mundo.model.dto.IncomingSummary;
import com.cb.mundo.model.dto.OutcomingSummary;
import com.cb.mundo.model.entity.Certificate;
import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.CityTeamMember;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Country;
import com.cb.mundo.model.entity.Facilitator;
import com.cb.mundo.model.entity.Incoming;
import com.cb.mundo.model.entity.Integrant;
import com.cb.mundo.model.entity.Outcoming;
import com.cb.mundo.model.entity.Participant;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.entity.Staff;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.entity.enumeration.FinancialStatus;
import com.cb.mundo.model.entity.enumeration.IncomingCategory;
import com.cb.mundo.model.entity.enumeration.OutcomingCategory;
import com.cb.mundo.model.entity.enumeration.ProductionStatus;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.entity.enumeration.SeminarPaymentMethod;
import com.cb.mundo.model.exception.BusinessException;
import com.cb.mundo.model.util.ObjectUtil;

/**
 * EJB para Produções de Seminarios
 * 
 * @author Solkam
 * @since 10 out 2011
 */
@Stateless
public class ProductionService {
	
	@PersistenceContext EntityManager manager;
	
	@EJB ContactService contactService;
	
	@EJB CityTeamService cityTeamService;
	
	@EJB FeedbackService feedbackService;
	
	@EJB RendicionService rendicionService;

	
	private ProductionDAO productionDao;
	
	@PostConstruct
	void init() {
		productionDao = new ProductionDAO(manager);
	}


	/*
	 * Production
	 ************/
	
	public Production saveProduction(Production prod) {
		boolean isTransient = prod.isTransient();
		prod = manager.merge( prod );
		handleProductorsByCityTeamMember(prod, isTransient);
		return prod;
	}
	
	
	/**
	 * Busca os produtores cadastrados em CityTeamMember e incluir
	 * como produtores na producao
	 * @param prod
	 * @param isTransient
	 */
	private void handleProductorsByCityTeamMember(Production prod, boolean isTransient) {
		if (isTransient) {
			List<CityTeamMember> members = cityTeamService.searchCityTeamMemberByCityAndSchool(prod.getCity(), prod.getSchool());
			for (CityTeamMember member : members) {
				Integrant integrant = new Integrant();
				integrant.setProduction( prod );
				integrant.setContact( member.getContact() );
				integrant.setRole( member.getRole() );
				integrant.setFlagInformeFeedback( member.getFlagInformeFeedback() );
				integrant.setFlagInformeRendicion( member.getFlagInformeRendicion() );
				manager.merge( integrant );
			}
		}
	}

	
	
	public void saveProductions(List<Production> productions) {
		for (Production productionVar : productions) {
			manager.merge( productionVar );
		}
	}
	
	


//	/**
//	 * Verifica se um facilitator já está alocado em outra cidade na mesma data.
//	 * @param p
//	 */
// VALIDACAO EH FEITO PELA CLASSE PLANNINGVALIDATOR	
//	private void verifyIfFacilitatorAlocation(Production p) {
//		Date date1 = p.getFirstDate();
//		Date date2 = p.getLastDate();
//		
//		List<Production> productionsFound = null;
//		
//		//f1
//		Facilitator facilitator1 = p.getFacilitator1();
//		productionsFound = searchProductionByFacilitatorAndDates(facilitator1, date1, date2);
//		throwsExceptionIfListNotContainsProduction(productionsFound, p, facilitator1);
//
//		//f2
//		Facilitator facilitator2 = p.getFacilitator2();
//		productionsFound = searchProductionByFacilitatorAndDates(facilitator2, date1, date2);
//		throwsExceptionIfListNotContainsProduction(productionsFound, p, facilitator2);
//
//		//f3
//		Facilitator facilitator3 = p.getFacilitator3();
//		productionsFound = searchProductionByFacilitatorAndDates(facilitator3, date1, date2);
//		throwsExceptionIfListNotContainsProduction(productionsFound, p, facilitator3);
//
//		//f4
//		Facilitator facilitator4 = p.getFacilitator4();
//		productionsFound = searchProductionByFacilitatorAndDates(facilitator4, date1, date2);
//		throwsExceptionIfListNotContainsProduction(productionsFound, p, facilitator4);
//	}
	
//	/**
//	 * Se a lista de produções que o facilitator estiver alocado tiver produção diferente
//	 * da que está sendo salva, é sinal que o facilitator está sendo alocado para duas produções
//	 * distintas na mesma data e isso é um erro.
//	 * @param productionsList
//	 * @param production
//	 */
//	private void throwsExceptionIfListNotContainsProduction(List<Production> productionsList, Production production, Facilitator facilitator) {
//		if (productionsList!=null && !productionsList.isEmpty()) {
//			for (Production productionVar : productionsList) {
//				if (!productionVar.equals(production)) {
//					Object[] params = {facilitator.getContact().getName(), productionVar.getCity().getName(), production.getCity().getName()};
//					throw new BusinessException("msg_facilitator_already_alocated_in_other_production_with_same_date", params);
//				}
//			}
//		}
//	}
	
//	/**
//	 * Pesquisa produções do facilitator entre data
//	 * (usado para verificar a alocação de facilitatores)
//	 * @param facilitator
//	 * @param date1
//	 * @param date2
//	 * @return
//	 */
//	private List<Production> searchProductionByFacilitatorAndDates(Facilitator facilitator, Date date1, Date date2) {
//		if (facilitator==null || date1==null || date2==null) {//se alguem for null, nada a fazer
//			return null;
//		}
//		
//		return manager.createNamedQuery("searchProductionByFacilitatorAndDates", Production.class)
//				.setParameter("pFacilitator", facilitator)
//				.setParameter("pDate1", date1)
//				.setParameter("pDate2", date2)
//				.getResultList();
//	}


	public Production defineProductionAsActive(Production production) {
		//1.coloca como planejado quem esta ativa
		productionDao.updateProductionAsPlannedBySchoolAndCity(production.getSchool(), production.getCity());
		
		//2.coloca a producao como ativa
		production = manager.find(Production.class, production.getId() );
		production.changeToStatus( ProductionStatus.ACTIVE );
		
		return production;
	}
	
	
	public void removeProduction(Production production) {
		production = manager.find(Production.class, production.getId() );
		
		verifyProductionAssociations(production);
		
		manager.remove( production );
	}
	
	

	/**
	 * Verifica todas as associações de produções
	 * @param production
	 */
	private void verifyProductionAssociations(Production production) {
		//integrantes
		if (!production.getIntegrants().isEmpty() ) {
			throw new BusinessException("msg_production_has_integrants", null);
		}
		//staffs
		if (!production.getStaffs().isEmpty()) {
			throw new BusinessException("msg_production_has_staffs", null);
		}
		//participantes
		if (!production.getParticipants().isEmpty()) {
			throw new BusinessException("msg_production_has_participants", null);
		}
		//receitas
		if (!production.getIncomings().isEmpty()) {
			throw new BusinessException("msg_production_has_incomings", null);
		}
		//despesas
		if (!production.getOutcomings().isEmpty()) {
			throw new BusinessException("msg_production_has_outcomings", null);
		}
		//informe de feedback
		if (feedbackService.findFeedbackByProduction(production)!=null) {
			throw new BusinessException("msg_production_has_feedback", null);
		}
		//informe de rendição
		if (rendicionService.findRendicionByProduction(production)!=null) {
			throw new BusinessException("msg_production_has_rendicion", null);
		}
	}


	public List<Production> searchProduction(School school, City city) {
		return productionDao.searchProductionBySchoolAndCity(school, city);
	}
	
	
	public Production refreshActiveProduction(School school, City city) {
		//busca a producao ativa...
		Production activeProduction = productionDao.findProductionBySchoolAndCityAndStatus(school, city, ProductionStatus.ACTIVE);
		
		//se tiver production ativa, forca a carga das associacoes
		if (activeProduction!=null) {
			activeProduction.getStaffs().size();
			activeProduction.getIntegrants().size();
			activeProduction.getParticipants().size();
			activeProduction.getIncomings().size();
			activeProduction.getOutcomings().size();
		}
		//ok
		return activeProduction;
	}
	
	
	public Production findOldestProdution() {
		return productionDao.findOldestProdution();
	}
	
	
	/**
	 * Recarrega production somente com as referencias de pessoas
	 * @param production
	 * @return
	 */
	public Production refreshProductionOnlyPeople(Production production) {
		//traz para estado gerenciado
		production = manager.find(Production.class, production.getId() );

		//forca carga das referencias de pessoas
		production.getIntegrants().size();
		production.getStaffs().size();
		production.getParticipants().size();
		
		return production;
	}
	
	
	/**
	 * Recarrega production e todas as referencias
	 * @param production
	 * @return
	 */
	public Production refreschProductionAll(Production production) {
		//traz para managed
		production = manager.find(Production.class, production.getId() );

		//forca carga das referencias
		production.getIntegrants().size();
		production.getStaffs().size();
		production.getParticipants().size();
		production.getIncomings().size();
		production.getOutcomings().size();
		
		return production;
	}
	


	/**
	 * Pesquisa produções segundo criterios
	 * (usado no planejamento de produções)
	 * @param school
	 * @param countries
	 * @param cities
	 * @param year
	 * @param months
	 * @return
	 */
	public List<Production> searchProductionByFilters(School school, List<Country> countries, List<City> cities, int year, List<Integer> months) {
		return productionDao.searchProductionByFilters(school, countries, cities, year, months);
	}
	

	/**
	 * Pesquisa por todos os contatos de produtores a partir de uma lista de produções
	 * @param productions
	 * @return
	 */
	public Set<Contact> searchProductorContactByProductions(List<Production> productions) {
		Set<Contact> contacts = new TreeSet<>();
		for (Production p : productions) {
			//traz para Managed
			p = manager.find(Production.class, p.getId() );
			//iterage por todos os integrantes
			for (Integrant integrant : p.getIntegrants()) {
				//adiciona o contato
				contacts.add( integrant.getContact() );
			}
		}
		return contacts;
	}
	

	/**
	 * Pesquisa por todos os contatos de faciliator a partir
	 * das produções.
	 * @param productions
	 * @return
	 */
	public Set<Contact> searchFacilitatorContactByProduction(List<Production> productions) {
		Set<Contact> contacts = new TreeSet<>();
		for (Production p : productions) {
			//interage por todos os facilitatores
			for (Facilitator f : p.getFacilitatorsList()) {
				contacts.add( f.getContact() );
			}
		}
		return contacts;
	}

	
	
	
	
	
	
	
//acontecendo LIE
//	public Production refreshProductionWithIntegrants(Production production) {
//		production = manager.find(Production.class, production.getId() );
//		production.getIntegrants().size();
//		return production;
//	}
//	
//	public Production refreshProductionWithStaffs(Production production) {
//		production = manager.find(Production.class, production.getId() );
//		production.getStaffs().size();
//		return production;
//	}
//	
//	public Production refreshProductionWithParticipants(Production production) {
//		production = manager.find(Production.class, production.getId() );
//		production.getParticipants().size();
//		return production;
//	}
//	
//	public Production refreshProductionWithIncomings(Production production) {
//		production = manager.find(Production.class, production.getId() );
//		production.getIncomings().size();
//		return production;
//	}
//	
//	public Production refreshProductionWithOutcomings(Production production) {
//		production = manager.find(Production.class, production.getId() );
//		production.getOutcomings().size();
//		return production;
//	}
	
	
	
	
	

	
	
	
	/*
	 * Facilitator
	 *************/
	
	
	public List<Facilitator> searchFacilitatorByProduction(Production p) {
		List<Facilitator> facilitators = new ArrayList<Facilitator>();
		
		if (ObjectUtil.isValid(p.getFacilitator1() )) {
			facilitators.add( p.getFacilitator1() );
		}
		if (ObjectUtil.isValid(p.getFacilitator2() )) {
			facilitators.add( p.getFacilitator2() );
		}
		if (ObjectUtil.isValid(p.getFacilitator3() )) {
			facilitators.add( p.getFacilitator3() );
		}
		if (ObjectUtil.isValid(p.getFacilitator4() )) {
			facilitators.add( p.getFacilitator4() );
		}
		return facilitators;
	}
	
	
//	//NAO PRECISA POR NA COMBO DE FACILITATORES TEM CONVERTER
//	/**
//	 * Verifica os facilitadores selecionados (onde somente o ID esta valido) e traz
//	 * do banco de dados o objeto completo.
//	 * A partir da versão 3.0, todos os facilitatores são opcionais
//	 * @param prod
//	 */
//	private void handleProductionFacilitators(Production prod) {
//		if (ObjectUtil.isValid( prod.getFacilitator1() )) {
//			Facilitator f1 = facilitatorDAO.findFacilitatorById( prod.getFacilitator1().getId() );
//			prod.setFacilitator1( f1 );
//		} else {
//			prod.setFacilitator1( null );
//		}
//		
//		if (ObjectUtil.isValid(prod.getFacilitator2() )) {
//			Facilitator f2 = facilitatorDAO.findFacilitatorById( prod.getFacilitator2().getId() );
//			prod.setFacilitator2( f2 );
//		} else {
//			prod.setFacilitator2( null );
//		}
//		if (ObjectUtil.isValid(prod.getFacilitator3() )) {
//			Facilitator f3 = facilitatorDAO.findFacilitatorById( prod.getFacilitator3().getId() );
//			prod.setFacilitator3( f3 );
//		} else {
//			prod.setFacilitator3( null );
//		}
//		if (ObjectUtil.isValid(prod.getFacilitator4() )) {
//			Facilitator f4 = facilitatorDAO.findFacilitatorById( prod.getFacilitator4().getId() );
//			prod.setFacilitator4( f4 );
//		} else {
//			prod.setFacilitator4( null );
//		}
//	}	
//	
	
	


	/*
	 * Participant
	 *************/
	
	public List<Participant> searchParticipantByProduction(Production p) {
		return productionDao.searchParticipantByProduction(p);
	}
	
	
	
	public Participant saveParticipant(Participant participant, UserCB autenticatedUser) {
		//always save contact
		Contact contact = contactService.saveContact( participant.getContact(), autenticatedUser );
		participant.setContact(contact);

		boolean isNewParticipant = participant.isTransient();
		participant = productionDao.saveParticipant(participant);
	
		if (isNewParticipant) {
			createIncomingForNewParticipant(participant);
		}
		return participant;
	}
	
	
	public void removeParticipant(Participant participant) {
		productionDao.removeIncomingByParticipant(participant);
		productionDao.removeParticipant(participant);
	}
	
	
	
	/*
	 * Incoming	
	 **********/

	/**
	 * Create a incoming for new participant summing the paidValue and pendenteValue 
	 * @param participant
	 */
	private void createIncomingForNewParticipant(Participant participant) {
		Double totalPaid = participant.getAlreadyPaidValue() + participant.getPendentValue();
		
		Incoming incomingForNewParticipant = new Incoming();
		incomingForNewParticipant.setCategory(IncomingCategory.P);
		incomingForNewParticipant.setDate( new Date() );
		incomingForNewParticipant.setProduction( participant.getProduction() );
		incomingForNewParticipant.setStatus( FinancialStatus.PE );
		incomingForNewParticipant.setParticipant( participant );
		incomingForNewParticipant.setStaff( null );
		
		incomingForNewParticipant.setValue( totalPaid );
		incomingForNewParticipant.setPaymentMethod( participant.getPaymentMethod() );
		incomingForNewParticipant.setPaymentQuota( participant.getPaymentQuota() );

		productionDao.saveIncoming(incomingForNewParticipant);
	}
	
	/**
	 * Para novos staffs, cria um registro de receita.
	 * Levando em conta que uma pessoa pode exercer várias funcoes de staff,
	 * verifica também se o contact já existem como staff naquela producao.
	 * @param staff
	 */
	private void createIncomingForNewStaff(Staff staff) {
		Contact contact = staff.getContact();
		Production production = staff.getProduction();
		
		if (isContactFirstAsStaff(contact, production)) {
			Incoming incomingForNewStaff = new Incoming();
			incomingForNewStaff.setCategory( IncomingCategory.S );
			incomingForNewStaff.setStatus( FinancialStatus.PE );
			incomingForNewStaff.setDate( new Date() );
			incomingForNewStaff.setProduction( staff.getProduction() );
			incomingForNewStaff.setStaff( staff );
			incomingForNewStaff.setParticipant( null );
			
			incomingForNewStaff.setPaymentMethod( SeminarPaymentMethod.CASH );
			incomingForNewStaff.setPaymentQuota( 1 );
			incomingForNewStaff.setValue( staff.getValuePaid() );
			
			productionDao.saveIncoming( incomingForNewStaff );
		}
	}
	
	
	


	/* ***********
	 * Certificate
	 *************/
	/**
	 * Pesquisa todos os certificados ja gerados de uma producao.
	 * Pesquisa tambem os participantes desta producao.
	 * Se o numero nao bater, resetar todos os certificados.
	 */
	
	public List<Certificate> searchOrCreateCertificateByProduction(Production production) {
		List<Certificate> certificates = productionDao.searchCertificateByProduction(production); 
		List<Participant> participants = productionDao.searchParticipantByProduction(production);

		//1.se o numero de certificados for diferente do numero de participantes...
		if (certificates.size() != participants.size()) {
			//...reseta todos os certificados
			productionDao.removeCertificateByProduction(production);
			certificates.clear();
		}

		//1.se ainda nao tem certificados, cria a partir dos participantes
		if (certificates.isEmpty()) {
			for (Participant participant : participants) {
				Certificate c = new Certificate();
				c.setNameOnCertificate( participant.getContact().getCivilName() );
				c.setProduction( production );
				certificates.add( c );
			}
		}
		return certificates;
	}
	
	
	public Certificate saveCertificate(Certificate certificate) {
		return productionDao.saveCertificate(certificate);
	}
	
	
	public void removeCertificate(Certificate certificate) {
		productionDao.removeCertificate(certificate);
	}
	
	
	public List<Certificate> assignCertificateNumbers(Production prod, List<Certificate> certificates) {
		return productionDao.assignCertificateNumbers(prod, certificates);
	}

	

	/* ********* 
	 * Integrant
	 ***********/
	
	public List<Integrant> searchIntegrantByProduction(Production production) {
		return productionDao.searchIntegrantByProduction(production);
	}
	
	public Integrant saveIntegrant(Integrant pi) {
		return productionDao.saveIntegrant(pi);
	}
	
	public void removeIntegrant(Integrant pi) {
		productionDao.removeIntegrant(pi);
	}

	
	
	
	/* ***** 
	 * Staff
	 *******/
	
	public Staff saveStaff(Staff staff, boolean flagCreateIncoming) {
		staff = manager.merge( staff );
				
		if (flagCreateIncoming) {
			createIncomingForNewStaff( staff );
		}
		return staff;
	}
	
	/**
	 * Verifica se é o primeiro registro de staff para um dado contact e uma producao
	 * @param contact
	 * @param production
	 * @return true se for o primeiro registro do contact como staff
	 */
	private boolean isContactFirstAsStaff(Contact contact, Production production) {
		List<Staff> staffs = productionDao.searchStaffByContactAndProduction(contact, production);
		return staffs.size() <= 1;
	}

	
	public List<Staff> searchStaffByProduction(Production p) {
		return productionDao.searchStaffByProduction(p); 
	}
	
	
	/**
	 * Reomve staff e receitas associadas
	 * @param staff
	 */
	public void removeStaff(Staff staff) {
		productionDao.removeIncomingByStaff(staff);
		manager.remove( manager.merge(staff) );
	}
	
	

	/* *********
	 * Incomings
	 ***********/
	
	public List<Incoming> searchIncomingsByProduction(Production production) {
		return productionDao.searchIncomingByProduction(production);
	}
	
	
	public Incoming saveIncoming(Incoming incoming) {
		return productionDao.saveIncoming( incoming );
	}
	
	
	
	public void removeIncoming(Incoming incoming) {
		productionDao.removeIncoming( incoming );
	}
	
	public Set<Participant> searchParticipantsForIncoming(Production production) {
		production = manager.find(Production.class, production.getId() );
		
		Set<Participant> participantsForIncoming = new HashSet<>();
		for (Participant participant : production.getParticipants()) {
			participantsForIncoming.add( participant );
		}
		return participantsForIncoming;
	}
	
	
	public Set<Staff> searchStaffForIncoming(Production production) {
		production = manager.find(Production.class, production.getId() );
		
		Set<Staff> staffsForIncoming = new HashSet<>();
		for (Staff staff : production.getStaffs() ){
			staffsForIncoming.add( staff );
		}
		return staffsForIncoming;
	}
	


	
	/* ********** 
	 * Outcomings
	 ************/
	
	public List<Outcoming> searchOutcomingsByProduction(Production production) {
		return productionDao.searchOutcomingByProduction(production);
	}
	
	
	public Set<Contact> searchContactsForOutcoming(Production production) {
		production = manager.find(Production.class, production.getId() );

		Set<Contact> contactsForOutcoming = new HashSet<>();
		//1.integrants
		for (Integrant integrant : production.getIntegrants()) {
			contactsForOutcoming.add( integrant.getContact() );
		}
		//2.staffs
		for (Staff staff : production.getStaffs()) {
			contactsForOutcoming.add( staff.getContact() );
		}
		return contactsForOutcoming;
	}
	
	
	public Outcoming saveOutcoming(Outcoming outcoming) {
		handleOutcomingPaidByProduction(outcoming);
		outcoming = productionDao.saveOutcoming(outcoming);
		return outcoming;
	}


	/**
	 * if a outcoming were paid by production, there no 
	 * responsable contact.
	 * @param outcoming
	 */
	private void handleOutcomingPaidByProduction(Outcoming outcoming) {
		if (outcoming.getPaidByProduction() == true) {
			outcoming.setResponsable( null );
		}
	}
	
	
	
	public void removeOutcoming(Outcoming outcoming) {
		productionDao.removeOutcoming(outcoming);
	}
	
	
	
	public List<OutcomingSummary> searchOutcomingSummaryByProdution(Production p) {
		List<OutcomingSummary> summaries = new ArrayList<OutcomingSummary>();
		for (OutcomingCategory cat : OutcomingCategory.values() ) {
			List<Outcoming> outcomings = productionDao.searchOutcomingByProductionAndCategory(p, cat);
			OutcomingSummary summary = new OutcomingSummary();
			summary.setProduction(p);
			summary.setCategory(cat);
			summary.setOutcomings(outcomings);
			
			summaries.add(summary);
		}
		return summaries;
	}
	

	
	
	public List<IncomingSummary> searchIncomingSummaryByProduction(Production production) {
		List<IncomingSummary> summaries = new ArrayList<IncomingSummary>();
		for (IncomingCategory category : IncomingCategory.values() ) {
			List<Incoming> incomings = productionDao.searchIncomingByProductionAndCategory(production, category);
			
			IncomingSummary summary = new IncomingSummary();
			summary.setProduction(production);
			summary.setCategory(category);
			summary.setIncomings(incomings);
			
			summaries.add( summary );
		}
		return summaries;
	}

	
	
	
	/* *******
	 * Informe
	 *********/
	

	
	//financial adjustment summary
	
	public List<FinancialAdjustSummary> searchFinancialAdjustSummaryByProduction(Production production) {
		List<FinancialAdjustSummary> summaries = new ArrayList<FinancialAdjustSummary>();
		
		List<Contact> productionContacts = productionDao.searchStaffContactByProduction(production);
		for (Contact prodContact : productionContacts) {
			List<Incoming> incomings = productionDao.searchIncomingByProductionAndStaffContact(production, prodContact);
			List<Outcoming> outcoming = productionDao.searchOutcomingByProductionAndContact(production, prodContact);
			
			FinancialAdjustSummary summary = new FinancialAdjustSummary(prodContact, incomings, outcoming);
			summaries.add( summary );
		}
		return summaries;
	}


	
}
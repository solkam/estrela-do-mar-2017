package com.cb.mundo.model.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.entity.Certificate;
import com.cb.mundo.model.entity.CertificateNumber;
import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Country;
import com.cb.mundo.model.entity.Facilitator;
import com.cb.mundo.model.entity.Incoming;
import com.cb.mundo.model.entity.Integrant;
import com.cb.mundo.model.entity.Module;
import com.cb.mundo.model.entity.Outcoming;
import com.cb.mundo.model.entity.Participant;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.entity.Staff;
import com.cb.mundo.model.entity.enumeration.IncomingCategory;
import com.cb.mundo.model.entity.enumeration.OutcomingCategory;
import com.cb.mundo.model.entity.enumeration.ProductionStatus;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.exception.BusinessException;
import com.cb.mundo.model.util.QueryUtil;

/**
 * DAO responsavel pela fronteira de entidades:
 * - Production
 * - Integrant
 * - Staff
 * - Participant
 * - Incoming
 * - Outcoming
 * - Informes (financeiro e feedback)
 * 
 * @author Solkam
 * @since 02 MAR 2013
 */
public class ProductionDAO {
	
	private EntityManager manager;
	
//	private DAO<Production> prodDao;
	private DAO<Participant> participantDao;
	private DAO<Certificate> certificateDao;
	private DAO<Integrant> integrantDao;
//	private DAO<Staff> staffDao;
	private DAO<Incoming> incomingDao;
	private DAO<Outcoming> outcomingDao;
//	private DAO<RecipientInforme> recipientInformeDao;
//	private DAO<zzzInformeFeedback> informeFeedbackDao;
//	private DAO<zzzInforme16Points> informe16PointsDao;
//	private DAO<zzzInformeFinancial> informeFinancialDao;
	

	public ProductionDAO(EntityManager manager) {
		super();
		this.manager = manager;
//		this.prodDao = new JavaxPersistenceDAO<Production>(manager, Production.class);
		this.participantDao = new JavaxPersistenceDAO<Participant>(manager, Participant.class);
		this.certificateDao = new JavaxPersistenceDAO<Certificate>(manager, Certificate.class);
		this.integrantDao = new JavaxPersistenceDAO<Integrant>(manager, Integrant.class);
//		this.staffDao = new JavaxPersistenceDAO<Staff>(manager, Staff.class);
		this.incomingDao = new JavaxPersistenceDAO<Incoming>(manager, Incoming.class);
		this.outcomingDao = new JavaxPersistenceDAO<Outcoming>(manager, Outcoming.class);
//		this.recipientInformeDao = new JavaxPersistenceDAO<RecipientInforme>(manager, RecipientInforme.class);
//		this.informeFeedbackDao = new JavaxPersistenceDAO<zzzInformeFeedback>(manager, zzzInformeFeedback.class);
//		this.informe16PointsDao = new JavaxPersistenceDAO<zzzInforme16Points>(manager, zzzInforme16Points.class);
//		this.informeFinancialDao = new JavaxPersistenceDAO<zzzInformeFinancial>(manager, zzzInformeFinancial.class);
	}

/* **********
 * Production	
 ************/
	
	/**
	 * Seleciona as producoes da escola e cidade que estao ACTIVE e as coloca como PLANNED
	 * (usado qdo se mudar a producao ativa)
	 * @param school
	 * @param city
	 */
	public void updateProductionAsPlannedBySchoolAndCity(School school, City city) {
		//1.seleciona todas as producoes ativas da cidade e da escola
		List<Production> activeProductions = manager.createNamedQuery("searchProductionBySchoolAndCityAndStatus", Production.class)
			.setParameter("pSchool", school)
			.setParameter("pCity", city)
			.setParameter("pStatus", ProductionStatus.ACTIVE)
			.getResultList();
		
		//2.coloca como planejada
		for (Production production : activeProductions) {
			production.changeToStatus( ProductionStatus.PLANNED );
		}
		
	}


	
	public List<Production> searchProductionBySchoolAndCity(School school, City city) {
		return manager.createNamedQuery("Production.searchBySchoolAndCity", Production.class)
				.setParameter("pSchool", school)
				.setParameter("pCity", city)
				.getResultList();
	}
	
	public Production findProductionBySchoolAndCityAndStatus(School school, City city, ProductionStatus status) {
		List<Production> prods = manager.createNamedQuery("findProductionBySchoolAndCityAndStatus", Production.class)
				.setParameter("pSchool", school)
				.setParameter("pCity", city)
				.setParameter("pStatus", status)
				.getResultList();
		
		int numberOfCurrent = prods.size();

		//ok
		if (numberOfCurrent==1) {
			return prods.get(0);
		}
		
		//erro de configuracao
		if (numberOfCurrent > 1) {
			Object[] params = {school.name(), city.getName()};
			throw new BusinessException("msg_too_many_active_production", params);
		}
		
		//ok, apenas cidade ou escola nova
		return null;
	}
	
	
	public Production findOldestProdution() {
		try {
			List<Production> lista = manager.createNamedQuery("Production.searchOrderByFirstDate", Production.class)
				.getResultList();
			return lista.get(0);
			
		}catch(PersistenceException e) {
			return new Production();
		}
	}
	
	public List<Production> searchProductionByFacilitator(Facilitator f) {
		return manager.createNamedQuery("Production.searchByFacilitator", Production.class)
				.setParameter("pFacilitator", f)
				.getResultList();
	}
	
	public List<Production> searchProductionByCity(City city) {
		return manager.createNamedQuery("Production.searchByCity", Production.class)
				.setParameter("pCity", city)
				.getResultList();
	}
	
	
	/**
	 * Realiza pesquisa por producoes usando criteria
	 * @param school
	 * @param city
	 * @param year
	 * @param months
	 * @return
	 */
	public List<Production> searchProductionByFilters(School school, List<Country> countries, List<City> cities, int year, List<Integer> months) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Production> criteria = builder.createQuery(Production.class);
		Root<Production> root = criteria.from(Production.class);
		
		Predicate conjuntion = builder.conjunction();
		//school
		if (QueryUtil.isNotNull(school)) {
			conjuntion = builder.and(conjuntion, 
				builder.equal(
					root.<School>get("school"), 
					school)
			);
		}
		//countries
		if (QueryUtil.isNotEmpty(countries)) {
			conjuntion = builder.and(conjuntion, 
				root.<City>get("city").<Country>get("country").in( countries ) 
			);
		}
		//cities
		if (QueryUtil.isNotEmpty(cities)) {
			conjuntion = builder.and(conjuntion, 
				root.<City>get("city").in( cities ) 
			);
		}
		//year
		if (QueryUtil.isNotNegative(year)) {
			conjuntion = builder.and(conjuntion, 
				builder.equal(
					builder.function("YEAR", Integer.class, root.<Date>get("firstDate")),
					year)
			);
		}
		//month
		if (QueryUtil.isNotEmpty(months)) {
			conjuntion = builder.and(conjuntion, 
				builder.function("MONTH", Integer.class, root.<Date>get("firstDate")).
					in( months )
			);
		}
		criteria.where( conjuntion );
		
		criteria.orderBy( 
			builder.asc(root.<City>get("city")),
			builder.asc(root.<Date>get("firstDate"))
		);
		
		return manager.createQuery(criteria).getResultList();
	}

	
		
/* ***********
 * Participant
 *************/
	
	public List<Participant> searchParticipantByProduction(Production p) {
		return manager.createNamedQuery("Participant.searchByProduction", Participant.class)
				.setParameter("pProduction", p)
				.getResultList();
	}

	public Participant saveParticipant(	Participant pp) {
		return participantDao.save( pp );
	}

	public void removeParticipant(Participant pp) {
		participantDao.remove(pp);
	}
	
	public List<Participant> searchParticipantByContact(Contact c) {
		return manager.createNamedQuery("Participant.searchByContact", Participant.class)
				.setParameter("pContact", c)
				.getResultList();
	}
	

	
/* ***********
 * certificate
 *************/

	public Certificate saveCertificate(Certificate certificate) {
		return certificateDao.save(certificate);
	}
	
	public void removeCertificate(Certificate certificate) {
		certificateDao.remove(certificate);
	}
	
	public List<Certificate> searchCertificateByProduction(Production production) {
		return manager.createNamedQuery("searchCertificateByProduction", Certificate.class)
				.setParameter("pProduction", production)
				.getResultList();
	}
	
	public List<Certificate> assignCertificateNumbers(Production prod, List<Certificate> certificates) {
		//recupera o atual numero de certificado
		School school = prod.getSchool();
		Module module = prod.getModule();
		CertificateNumber certificateNumber = findCertificateNumberBySchoolAndModule(school, module);

		//atribuir para todos os certificados
		Long currentNumber = certificateNumber.getNumber();
		for (Certificate cert : certificates) {
			cert.setNumber( Long.toString(++currentNumber) ); 
		}
		//redefine o atual numero
		certificateNumber.setNumber(currentNumber);
		
		return certificates;
	}
	
	private CertificateNumber findCertificateNumberBySchoolAndModule(School school, Module module) {
		return manager.createNamedQuery("findCertificateNumberBySchoolAndModule", CertificateNumber.class)
				.setParameter("pSchool", school)
				.setParameter("pModule", module)
				.getSingleResult();
	}
	
	public void removeCertificateByProduction(Production prodution) {
		manager.createNamedQuery("removeCertificateByProduction")
			.setParameter("pProduction", prodution)
			.executeUpdate();
	}

	
/* *********
 * Integrant
 ***********/
	
	public Integrant saveIntegrant(Integrant pi) {
		return integrantDao.save(pi);
	}

	public void removeIntegrant(Integrant pi) {
		integrantDao.remove(pi);
	}

	public List<Integrant> searchIntegrantByProduction(Production production) {
		return manager.createNamedQuery("Integrant.searchByProduction", Integrant.class)
				.setParameter("pProduction", production)
				.getResultList();
	}
	
	public List<Integrant> searchIntegrantByContact(Contact c) {
		return manager.createNamedQuery("Integrant.searchByContact", Integrant.class)
				.setParameter("pContact", c)
				.getResultList();
	}
	

/* *****
 * Staff
 *******/
	
	public List<Staff> searchStaffByProduction(Production p) {
		return manager.createNamedQuery("Staff.searchByProduction", Staff.class)
				.setParameter("pProduction", p)
				.getResultList();
	}
	
	public List<Staff> searchStaffByContactAndProduction(Contact c, Production p) {
		return manager.createNamedQuery("Staff.searchByContactAndProduction", Staff.class)
				.setParameter("pContact", c)
				.setParameter("pProduction", p)
				.getResultList();
		
	}
	
	public List<Staff> searchStaffByContact(Contact c) {
		return manager.createNamedQuery("Staff.searchByContact", Staff.class)
				.setParameter("pContact", c)
				.getResultList();
	}
	


/* ********
 * Incoming
 **********/
	
	public Incoming saveIncoming(Incoming incoming) {
		return incomingDao.save(incoming);
	}
	
	public void removeIncoming(Incoming incoming) {
		incomingDao.remove(incoming);
	}

	public void removeIncomingByParticipant(Participant p) {
		manager.createNamedQuery("Incoming.removeByParticipant")
		.setParameter("pParticipant", p)
		.executeUpdate();
	}
	
	public void removeIncomingByStaff(Staff staff) {
		manager.createNamedQuery("Incoming.removeByStaff")
			.setParameter("pStaff", staff)
			.executeUpdate();
	}

	public List<Incoming> searchIncomingByProduction(Production production) {
		return manager.createNamedQuery("Incoming.searchByProduction", Incoming.class)
				.setParameter("pProduction", production)
				.getResultList();
	}
	
	public List<Incoming> searchIncomingByProductionAndCategory(Production p, IncomingCategory cat) {
		return manager.createNamedQuery("Incoming.searchByProductionAndCategory", Incoming.class)
				.setParameter("pProduction", p)
				.setParameter("pCategory", cat)
				.getResultList();
	}
	
	public List<Incoming> searchIncomingByProductionAndStaffContact(Production prod, Contact contact) {
		return manager.createNamedQuery("searchIncomingByProductionAndStaffContact", Incoming.class)
				.setParameter("pProduction", prod)
				.setParameter("pContact", contact)
				.getResultList();
	}
	

/* *********
 * Outcoming
 ***********/
	
	public Outcoming saveOutcoming(Outcoming outcoming) {
		return outcomingDao.save(outcoming);
	}
	
	public void removeOutcoming(Outcoming outcoming) {
		outcomingDao.remove(outcoming);
	}

	public List<Outcoming> searchOutcomingByProduction(Production production) {
		return manager.createNamedQuery("Outcoming.searchByProduction", Outcoming.class)
				.setParameter("pProduction", production)
				.getResultList();
	}
	
	public List<Outcoming> searchOutcomingByProductionAndCategory(Production production, OutcomingCategory category) {
		return manager.createNamedQuery("Outcoming.searchByProductionAndCategory", Outcoming.class)
				.setParameter("pProduction", production)
				.setParameter("pCategory", category)
				.getResultList();
	}

	public List<Outcoming> searchOutcomingByContact(Contact c) {
		return manager.createNamedQuery("Outcoming.searchByContact", Outcoming.class)
				.setParameter("pContact", c)
				.getResultList();
	}
	
	public List<Outcoming> searchOutcomingByProductionAndContact(Production prod, Contact contact) {
		return manager.createNamedQuery("searchOutcomingByProductionAndContact", Outcoming.class)
				.setParameter("pProduction", prod)
				.setParameter("pContact", contact)
				.getResultList();
	}

	
/* ********
 * Informes
 **********/
	
//	public zzzInformeFeedback saveInformeFeedback(zzzInformeFeedback feedback) {
//		return informeFeedbackDao.save(feedback);
//	}
//
//	public zzzInforme16Points saveInforme16Points(zzzInforme16Points informe) {
//		return informe16PointsDao.save(informe);
//	}
//
//	public zzzInformeFinancial saveInformeFinancial(zzzInformeFinancial informe) {
//		return informeFinancialDao.save(informe);
//	}
//
//	public zzzInformeFeedback findInformeFeedbackByProduction(Production p) {
//		try {
//			return manager.createNamedQuery("InformeFeedback.findByProduction", zzzInformeFeedback.class)
//					.setParameter("pProduction", p)
//					.getSingleResult();
//			 
//		}catch(NoResultException e) {
//			return null;
//		}
//	}
//	
//
//	public zzzInforme16Points findInforme16PointsByProduction(Production p) {
//		try {
//			return manager.createNamedQuery("Informe16Points.findByProduction", zzzInforme16Points.class)
//					.setParameter("pProduction", p)
//					.getSingleResult();
//			
//		} catch (NoResultException e) {
//			return null;
//		}
//	}
	
	
//	public zzzInformeFinancial findInformeFinancialByProduction(Production p) {
//		try {
//			return manager.createNamedQuery("InformeFinancial.findByProduction", zzzInformeFinancial.class)
//					.setParameter("pProduction", p)
//					.getSingleResult();
//			
//		} catch (Exception e) {
//			return null;
//		}
//	}
	
	
/* *****************
 * recipient informe
 *******************/
	
//	public RecipientInforme saveRecipientInforme(RecipientInforme recipient) {
//		return recipientInformeDao.save(recipient);
//	}
//
//	public void removeRecipientInforme(RecipientInforme recipient) {
//		recipientInformeDao.remove(recipient);
//	}

	
	

/* ****************************
 * Financial Adjustment Summary
 ******************************/
	public List<Contact> searchStaffContactByProduction(Production production) {
		return manager.createNamedQuery("searchStaffContactByProduction", Contact.class)
				.setParameter("pProduction", production)
				.getResultList();
	}
	
	


	
}

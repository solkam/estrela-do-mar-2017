package com.cb.mundo.model.service;

import static com.cb.mundo.model.util.ObjectUtil.isNotNull;
import static com.cb.mundo.model.util.ObjectUtil.isValidDate;
import static com.cb.mundo.model.util.QueryUtil.isNotEmpty;
import static com.cb.mundo.model.util.QueryUtil.isNotNegative;
import static com.cb.mundo.model.util.StringUtil.isNotBlank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.dto.PaymentMethodSummary;
import com.cb.mundo.model.dto.ReportParticipantDTO;
import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Country;
import com.cb.mundo.model.entity.Feedback;
import com.cb.mundo.model.entity.Incoming;
import com.cb.mundo.model.entity.Module;
import com.cb.mundo.model.entity.Participant;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.entity.Rendicion;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.entity.enumeration.SeminarPaymentMethod;
import com.cb.mundo.model.report.SearchFilter;

/**
 * EJB para relatorios de Seminarios
 * 
 * @author Solkam
 * @since 20 nov 2011
 */
@Stateless
public class ReportSeminarService {
	
	
	@PersistenceContext EntityManager manager;
	
	
	public List<Production> searchProductionByFilter(SearchFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Production> query = builder.createQuery(Production.class);
		Root<Production> root = query.from(Production.class);
		query.select(root);
		
		Predicate conjunction = builder.conjunction();
		
		if (isNotNull( filter.getSchool() )) {
			Expression<School> expSchool = root.get("school");
			Predicate predSchool = builder.equal(expSchool, filter.getSchool());
			conjunction.getExpressions().add( predSchool );
		}
		
		if (isNotBlank( filter.getCountry().getCode() )) {
			Expression<Country> expCountry = root.get("city").get("country");
			Predicate predCountry = builder.equal( expCountry, filter.getCountry() );
			conjunction.getExpressions().add( predCountry );
		}
		if (isNotNull( filter.getCity().getId() )) {
			Expression<City> expCity = root.get("city");
			Predicate predCity = builder.equal( expCity, filter.getCity() );
			conjunction.getExpressions().add( predCity );
		}
		if (isValidDate( filter.getBeginDate() )) {
			Expression<Date> expFirstDate = root.get("firstDate");
			Predicate predFirstDate = builder.greaterThanOrEqualTo(expFirstDate, filter.getBeginDate() );
			conjunction.getExpressions().add( predFirstDate );
		}
		if (isValidDate( filter.getEndDate() )) {
			Expression<Date> expLastDate = root.get("lastDate");
			Predicate predLastDate = builder.lessThanOrEqualTo(expLastDate, filter.getEndDate() );
			conjunction.getExpressions().add( predLastDate );
		}
		
		query.where( conjunction );
		
		query.orderBy( builder.asc(root.get("firstDate")) );
		
		List<Production> resultList = manager.createQuery(query).getResultList(); 
		return resultList;
	}
	
	
	
	public List<PaymentMethodSummary> groupByPaymentMethodSummaryByCity(City city) {
		List<PaymentMethodSummary> sumaries = groupByPaymentMethodByCity(city);
		for (PaymentMethodSummary summary : sumaries) {
			List<Incoming> incomings = searchIncomingByCityAndPaymentMethod( city, summary.getPaymentMethod() );
			summary.setIncomings(incomings);
		}
		return sumaries;
	}
	
	
	
	@SuppressWarnings("unchecked")
	private List<PaymentMethodSummary> groupByPaymentMethodByCity(City city) {
		return manager.createNamedQuery("PaymentMethodSummary.searchGroupByCity")
				.setParameter("pCity", city)
				.getResultList();
	}
	
	
	
	private List<Incoming> searchIncomingByCityAndPaymentMethod(City city, SeminarPaymentMethod paymentMethod) {
		return manager.createNamedQuery("Incoming.searchByCityAndPaymentMethod", Incoming.class)
				.setParameter("pCity", city)
				.setParameter("pPaymentMethod", paymentMethod)
				.getResultList();
	}
	


	
	
	/* *********************************
	 * Relatorio Financeiro de Producoes
	 * *********************************/
	
	/**
	 * 
	 * @param schools
	 * @param cities
	 * @param year
	 * @param months
	 * @return
	 */
	public List<Production> searchProductionBySchoolsAndCitiesAndYearAndMonths(List<School> schools, List<City> cities,	int year, List<Integer> months) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Production> criteria = builder.createQuery(Production.class);
		Root<Production> root = criteria.from(Production.class);

		Predicate conjunction = builder.conjunction();
		//schools
		if (isNotEmpty(schools)) {
			conjunction = builder.and(conjunction, 
				root.<School>get("school").in(schools) 
			);
		}
		//cities
		if (isNotEmpty(cities)) {
			conjunction = builder.and(conjunction, 
				root.<City>get("city").in(cities) 
			);
		}
		//year
		if (isNotNegative(year)) {
			conjunction = builder.and(conjunction, 
				builder.equal( 
					builder.function("YEAR", Integer.class, root.<Date>get("firstDate")),
					year
			));
		}
		//cities
		if (isNotEmpty(months)) {
			conjunction = builder.and(conjunction, 
				builder.function("MONTH", Integer.class, root.<Date>get("firstDate")).
				in(months)
			);
		}

		criteria.where(conjunction);
		
		//order by
		criteria.orderBy( 
			builder.asc(root.<School>get("school")), 
			builder.asc(root.<City>get("city")) 
		);
		
		//executa query
		List<Production> productions = manager.createQuery(criteria).getResultList();
		
		//forca a carga dos dados financeiros e produtores...
		for (Production production : productions) {
			production.getCalculatedIncomingTotal();
			production.getCalculatedOutcomingTotal();
			production.getNumberOfParticipants();
			production.getNumberOfStaffs();
			production.getIntegrants().size();
		}
		return productions;
	}
	
	
	
	/* **********************
	 * Relatorio de Rendicoes
	 * **********************/
	
	/**
	 * Pesquisa por rendicoes usando criteria
	 * @param schools
	 * @param cities
	 * @param year
	 * @param months
	 * @return rendicoes com os detalhes de despesas carregados
	 */
	public List<Rendicion> searchRendicionByFilters(
			List<School> schools, 
			List<City> cities,	
			int year, 
			List<Integer> months,
			boolean flagEagerLoad) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Rendicion> criteria = builder.createQuery(Rendicion.class);
		
		Root<Rendicion> rootRendicion = criteria.from(Rendicion.class);
		Path<Production> rootProduction = rootRendicion.<Production>get("production");
		
		Predicate conjunction = builder.conjunction();
		//schools
		if (isNotEmpty(schools)) {
			conjunction = builder.and(conjunction, 
				rootProduction.<School>get("school").in(schools) 
			);
		}
		//cities
		if (isNotEmpty(cities)) {
			conjunction = builder.and(conjunction, 
				rootProduction.<City>get("city").in(cities) 
			);
		}
		//year
		if (isNotNegative(year)) {
			conjunction = builder.and(conjunction, 
				builder.equal( 
					builder.function("YEAR", Integer.class, rootProduction.<Date>get("firstDate")),
					year
			));
		}
		//cities
		if (isNotEmpty(months)) {
			conjunction = builder.and(conjunction, 
				builder.function("MONTH", Integer.class, rootProduction.<Date>get("firstDate")).
				in(months)
			);
		}

		criteria.where(conjunction);
		
		//order by
		criteria.orderBy( 
			builder.asc(rootProduction.<School>get("school")), 
			builder.asc(rootProduction.<City>get("city")) 
		);

		List<Rendicion> rendicoes = manager.createQuery(criteria).getResultList();
		
		//forc a carga das referencias
		for (Rendicion r : rendicoes) {
			Production production = r.getProduction();
			production.getIntegrants().size();
			production.getParticipants().size();
			production.getStaffs().size();
			production.getOutcomings().size();
			production.getIncomings().size();
		}
		
		if (flagEagerLoad) {
			for (Rendicion rendicionVar : rendicoes) {
				rendicionVar.getOutcomingDetails().size();
				rendicionVar.getPayments().size();
			}
		}
		
		return rendicoes;
	}
	
	
	
	/* ***********************
	 * Relatorios de Feedbacks
	 * ***********************/
	/**
	 * Pesquisa Feedback por criterios
	 * @param schools
	 * @param cities
	 * @param year
	 * @param months
	 * @return
	 */
	public List<Feedback> searchFeedbackByFilters(
			List<School> schools, 
			List<City> cities,	
			int year, 
			List<Integer> months) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Feedback> criteria = builder.createQuery(Feedback.class);
		
		Root<Feedback> rootRendicion = criteria.from(Feedback.class);
		Path<Production> rootProduction = rootRendicion.<Production>get("production");
		
		Predicate conjunction = builder.conjunction();
		//schools
		if (isNotEmpty(schools)) {
			conjunction = builder.and(conjunction, 
				rootProduction.<School>get("school").in(schools) 
			);
		}
		//cities
		if (isNotEmpty(cities)) {
			conjunction = builder.and(conjunction, 
				rootProduction.<City>get("city").in(cities) 
				);
		}
		//year
		if (isNotNegative(year)) {
		conjunction = builder.and(conjunction, 
			builder.equal( 
				builder.function("YEAR", Integer.class, rootProduction.<Date>get("firstDate")),
				year
			));
		}
		//cities
		if (isNotEmpty(months)) {
			conjunction = builder.and(conjunction, 
				builder.function("MONTH", Integer.class, rootProduction.<Date>get("firstDate")).
				in(months)
			);
		}
		
		criteria.where(conjunction);
		
		//order by
		criteria.orderBy( 
			builder.asc(rootProduction.<School>get("school")), 
			builder.asc(rootProduction.<City>get("city")) 
		);
		
		List<Feedback> feedbacks = manager.createQuery(criteria).getResultList();
		
		return feedbacks;
	}


	
	
	
	/* *****************************
	 * Relatorio Contatos por Escola 
	 *******************************/
	
	public List<Contact> searchContactBySchoolAlreadyParticipated(School school, List<Module> moduleList) {
		List<Contact> contacts = manager.createNamedQuery("searchContactBySchoolAlreadyParticipated", Contact.class)
				.setParameter("pSchool", school)
				.setParameter("pModuleList", moduleList)
				.getResultList();
		return contacts;
	}
	
	
	
	/* ******************
	 * Participantes e NF
	 ********************/
	
	public List<ReportParticipantDTO> searchReportaParticipantDTOByProduction(Production filterProduction) {
		List<ReportParticipantDTO> dtos = new ArrayList<ReportParticipantDTO>();
		
		//1.seleciona todos os participantes da producao
		filterProduction = manager.find(Production.class, filterProduction.getId() );
		List<Participant> participants = filterProduction.getParticipants();
		
		//2.para cada participante...
		for (Participant participantVar : participants) {
			//...seleciona as suas receitas
			List<Incoming> incomings = manager.createNamedQuery("searchIncomingByParticipant", Incoming.class)
				.setParameter("pParticipant", participantVar)
				.getResultList();
			
			//3.instancia o DTO
			ReportParticipantDTO dto = new ReportParticipantDTO(filterProduction, participantVar, incomings);
			dtos.add( dto );
		}
		
		return dtos;
	}
	
	
	


}

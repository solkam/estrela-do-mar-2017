package com.cb.mundo.model.service;

import static com.cb.mundo.model.util.QueryUtil.isNotEmpty;
import static com.cb.mundo.model.util.QueryUtil.isNotNegative;
import static com.cb.mundo.model.util.QueryUtil.isNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CompoundSelection;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.dto.BalanceMegaEventMethodByEventDTO;
import com.cb.mundo.model.dto.BalanceMegaEventPresenceByEventDTO;
import com.cb.mundo.model.dto.DebtDTO;
import com.cb.mundo.model.dto.RendicionByCityDTO;
import com.cb.mundo.model.dto.RendicionByModuleDTO;
import com.cb.mundo.model.dto.RendicionBySchoolDTO;
import com.cb.mundo.model.dto.ReportPeopleAndRoleDetailDTO;
import com.cb.mundo.model.entity.City;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Country;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Module;
import com.cb.mundo.model.entity.Participant;
import com.cb.mundo.model.entity.Production;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.Rendicion;
import com.cb.mundo.model.entity.RendicionPayment;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.MegaEventRole;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.entity.enumeration.util.RegisterStatusUtil;
import com.cb.mundo.model.util.CalendarUtil;
import com.cb.mundo.model.util.NumberUtil;

/**
 * Servicos de Negocio para os Relatorios Gerenciais
 * @author Solkam
 * @since 05 ABR 2015
 */
@Stateless
public class MgmtReportService {

	@PersistenceContext EntityManager manager;
	
	@EJB RegisterService registerService;
	
	@EJB EventService eventService;

	
	
	/**
	 * Pesquisar pelos DTOs que relaciona Pessoas a um Papel.
	 * A ideia aqui é montar um mapa tendo contato com chave o que vai garantir
	 * que nao haja contatos repetivos. Sempre que se detectar um contato repetido,
	 * verifica quem tem o Role mais especial.
	 * No final, monta-se a lista de DTOs com as entrada do map.
	 * @param day
	 * @param activeMegaEvents
	 * @return
	 */
	public List<ReportPeopleAndRoleDetailDTO> searchReportPeopleAndRoleDetailDTOByDayAndMegaEventList(Date day, List<MegaEvent> activeMegaEvents) {
		List<ReportPeopleAndRoleDetailDTO> dtos = new ArrayList<ReportPeopleAndRoleDetailDTO>();
		
		//1.pesquisa pelos registers de mega eventos ativos no dia
		List<RegisterStatus> statusList = RegisterStatusUtil.getRegisterStatusListInsideMegaEvent();
		List<Register> registers = registerService.searchRegisterByMegaEventListAndStatusList(activeMegaEvents, statusList );
		
		//2.monta um mapa com <Contact, DTO>
		Map<Contact, ReportPeopleAndRoleDetailDTO> mapContactAndDTO = new HashMap<Contact, ReportPeopleAndRoleDetailDTO>();
		for (Register registerVar : registers) {

			Contact contactVar = registerVar.getContact();
			
			ReportPeopleAndRoleDetailDTO dto = mapContactAndDTO.get( contactVar );
			if (dto==null) {
				MegaEventRole roleDefined = defineSuitableMegaEventRole(registerVar, day);
				dto = new ReportPeopleAndRoleDetailDTO(registerVar, roleDefined);
				mapContactAndDTO.put(contactVar, dto);
			
			} else {
				//aqui é onde acontece a treta...
				//se a role for especial, deixa o register
				
				if (!dto.getRole().getFlagCBRole()) {//se NAO for especial..
					MegaEventRole roleDefined = defineSuitableMegaEventRole(registerVar, day);
					
					if (roleDefined.isMorePriorityThan(dto.getRole())) {//e priority for maior, entao.. 
						dto = new ReportPeopleAndRoleDetailDTO(registerVar, roleDefined);
						mapContactAndDTO.put(contactVar, dto);//sobreescreve
					}
				}
			}
		}
		
		//3.a partir do map, constroi o dtos
		for (Entry<Contact, ReportPeopleAndRoleDetailDTO> entryVar : mapContactAndDTO.entrySet()) {
			dtos.add( entryVar.getValue() );
		}
		
		return dtos;
	}
	
	
	
	
	/**
	 * Define qual eh o melhor papel para o inscrito
	 * @param register
	 * @param day
	 * @return
	 */
	private MegaEventRole defineSuitableMegaEventRole(Register register, Date day) {
		//1.se tiver CBRole:
		Contact contact = register.getContact();
		if (contact.getCbRole()!=null) {
			
			switch (contact.getCbRole()) {
			case DIRECTORIO:
				return MegaEventRole.DIRECTORIO;
			
			case DIRECTOR:
				return MegaEventRole.DIRECTOR;
			
			case LAMA:
				return MegaEventRole.LAMA;
			
			case YAIKIN:
				return MegaEventRole.YAIKIN;
			
			case MENTOR:
				return MegaEventRole.MENTOR;
			
			case COLABORADOR_CB:
				return MegaEventRole.COLABORADOR_CB;

			case COLABORADOR:
				return MegaEventRole.COLABORADOR;

			case INSTRUTOR:
				return MegaEventRole.INSTRUCTOR;
				
			}
		}
		
		//2.verifica pelos eventos cadastrados:
		for (RegisterDetail registerDetail : register.getDetails()) {
			
			Event event = registerDetail.getEvent();
			EventWeek eventWeek = registerDetail.getEventWeek();
			
			//contratada não tem nem semana, nem evento
			if (EventPresence.CONTRATED.equals( registerDetail.getPresence() )) {
				return MegaEventRole.COLABORADOR_CB;
			}

			//estah dentro do evento?
			if (CalendarUtil.isBetween(day, eventWeek.getBeginDate(), eventWeek.getEndDate()) ) {
			
				switch (event.getType()) {
				case FORMATION:
					if (EventPresence.PARTICIPANT.equals( registerDetail.getPresence() )) {
						return MegaEventRole.PARTICIPANT;
					} else {
						return MegaEventRole.STAFF_SEMINAR;
					}

				case INVITED:
					if (EventPresence.PARTICIPANT.equals( registerDetail.getPresence() )) {
						return MegaEventRole.PARTICIPANT;
					} else {
						return MegaEventRole.STAFF_SEMINAR;
					}

				case COMPLEMENTAR:
					if (EventPresence.PARTICIPANT.equals( registerDetail.getPresence() )) {
						return MegaEventRole.PARTICIPANT;
					} else {
						return MegaEventRole.STAFF_SEMINAR;
					}

				case STAFF:
					return MegaEventRole.STAFF_SERVICE;

				case DEPENDENT_BABY:
					return MegaEventRole.DEPENDENT_BABY;

				case DEPENDENT_CHILD:
					return MegaEventRole.DEPENDENT_CHILD;
					
				case DEPENDENT_TEENAGER:
					return MegaEventRole.DEPENDENT_TEENAGER;
					
				case DEPENDENT_PARTNER:
					return MegaEventRole.DEPENDENT_PARTNER;

					
				//indefinidos...	
				case PLAN:
					continue;
				}
			}
		}
		
		//3.se nao definiu nada...
		return MegaEventRole.STAFF_SERVICE;
		
	}




	/**
	 * Pesquisa pelos novos participantes segundo filtros
	 * (usando criteria)
	 * @param filterCountry
	 * @return
	 */
	public List<Participant> searchReportContactNewParticipantByFilter(School filterSchool, Country filterCountry, City filterCity, Integer filterYear) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Participant> criteria = builder.createQuery(Participant.class);
		Root<Participant> root = criteria.from(Participant.class);
		
		Predicate conjunction = builder.conjunction();
		
		Join<Participant, Contact> joinContact = root.<Participant, Contact>join("contact");

		//1.seleciona tudo sem filtro
		criteria.where(conjunction);
		
		Path<Contact> pathContact = root.<Contact>get("contact");
		criteria.groupBy( pathContact );
		criteria.having( builder.equal(builder.count(pathContact), 1) );
		
		criteria.orderBy( builder.asc( joinContact.<String>get("civilName") ));
		
		List<Participant> allNewParticipants = manager.createQuery(criteria).getResultList();
		
		
		//2.usando os dois filtros, cria-se uma nova lista de participantes  
		
		List<Participant> filteredNewParticipants = new ArrayList<Participant>();
		for (Participant participantVar : allNewParticipants) {
			boolean filterSchoolsPassed = false;
			boolean filterCountryPassed = false;
			boolean filterCityPassed = false;
			boolean filterYearPassed = false;
			
			//school
			if (isNotNull(filterSchool) ) {
				if (filterSchool.equals( participantVar.getProduction().getSchool() )) {
					filterSchoolsPassed = true;
				}
			} else {
				filterSchoolsPassed = true;
			}
			
			//country
			if (isNotNull(filterCountry)) {
				if (filterCountry.equals( participantVar.getProduction().getCity().getCountry() )) {
					filterCountryPassed = true;
				}
			} else {
				filterCountryPassed = true;
			}
			
			//city
			if (isNotNull(filterCity)) {
				if (filterCity.equals( participantVar.getProduction().getCity() )) {
					filterCityPassed = true;
				}
			} else {
				filterCityPassed = true;
			}
				 
			//ano
			if (isNotNegative(filterYear)) {
				if (filterYear.equals( CalendarUtil.getYear( participantVar.getProduction().getFirstDate() ))) {
					filterYearPassed = true;
				}
			} else {
				filterYearPassed = true;
			}
			
			//se os 2 filtros estiver OK, entao o participant faz parte do filtro
			if (filterSchoolsPassed && filterCountryPassed && filterCityPassed && filterYearPassed) {
				filteredNewParticipants.add( participantVar );
			}
		}
		
		return filteredNewParticipants;
	}




	/**
	 * Pesquisa inscricoes unicos (participantes pela primeira vez) pelo mega evento 
	 * @param filterMegaEvent
	 * @return
	 */
	public List<Register> searchNewRegisterByMegaEvent(MegaEvent filterMegaEvent) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Register> criteria = builder.createQuery(Register.class);
		Root<Register> root = criteria.from(Register.class);
		
		//1.seleciona tudo agrupando pelo contato...
		Path<Contact> pathContact = root.<Contact>get("contact");
		
		criteria.groupBy( pathContact );
		criteria.having( builder.equal(builder.count(pathContact), 1) );
		criteria.orderBy( builder.asc(pathContact.<String>get("civilName")) );

		List<Register> allRegisters = manager.createQuery(criteria).getResultList();
		
		
		//2.filtra pelo mega evento
		List<Register> filteredRegisters = new ArrayList<Register>();
		for (Register registerVar : allRegisters) {
			if (filterMegaEvent.equals( registerVar.getMegaEvent() )) {
				filteredRegisters.add( registerVar );
			}
		}
		
		return filteredRegisters;
	}
	

	/**
	 * busqueda de cantidad de participantes por evento en un megaevento 
	 * @param pMegaEvent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BalanceMegaEventPresenceByEventDTO> searchBalanceMegaEventPresenceByEventDTO(MegaEvent me, List<RegisterStatus> listStatus){
		List<BalanceMegaEventPresenceByEventDTO> details = new ArrayList<BalanceMegaEventPresenceByEventDTO>();
		for (RegisterStatus status : listStatus) {
		  details.addAll(  manager.createNamedQuery("searchBalanceMegaEventPresenceByEventDTO")
				.setParameter("pMegaEvent", me)
				.setParameter("pStatus", status)
				.getResultList()  );
		}
		return details;
		
	}
	/**
	 * busqueda de pagos con su metodo y valor por evento en un megaevento 
	 * @param pMegaEvent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BalanceMegaEventMethodByEventDTO> searchBalanceMegaEventMethodByEventDTO(MegaEvent me, List<RegisterStatus> listStatus){
		List<BalanceMegaEventMethodByEventDTO> details = new ArrayList<BalanceMegaEventMethodByEventDTO>();
		for (RegisterStatus status : listStatus) {
		  details.addAll( manager.createNamedQuery("searchBalanceMegaEventMethodByEventDTO")
				.setParameter("pMegaEvent", me)
				.setParameter("pStatus", status)
				.getResultList()  );
		}
		return details;
	}
	
	
	
	
	/* *******************************
	 * Rendiciones para Administracion
	 *********************************/
	
	/**
	 * Pesquisa pelo DTO que representa a agregacao de rendicoes por escola, somando os eventos, qtd de participantes,
	 * de staff, bem como total de receitas e total de despesas.
	 * Num segundo passo, totaliza os pagamentos por escola, ano e mes.
	 * Num terceiro passo, itera sobre os dtos e preenche a lista de agregados por modulo
	 * @param year
	 * @param months
	 * @return
	 */
	public List<RendicionBySchoolDTO> searchRendicionSchoolDTOByYearAndMonths(Integer year, List<Integer> months) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RendicionBySchoolDTO> criteria = builder.createQuery(RendicionBySchoolDTO.class);
		
		Root<Rendicion> root = criteria.from(Rendicion.class);
		Join<Rendicion, Production> joinProduction = root.<Rendicion, Production>join("production");
		
		Predicate conjunction = builder.conjunction();
		
		//1.ano
		if (isNotNegative(year)) {
			conjunction = builder.and(conjunction,
					builder.equal( builder.function("YEAR", Integer.class, joinProduction.<Date>get("firstDate")), year)
				);
		}
		
		//2.meses
		if (isNotEmpty(months)) {
			conjunction = builder.and(conjunction,
					builder.function("MONTH", Integer.class, joinProduction.<Date>get("firstDate"))
						.in( months )
				);
		}
		
		CompoundSelection<RendicionBySchoolDTO> selection = builder.construct(RendicionBySchoolDTO.class
				,joinProduction.<School>get("school")
				,builder.count( root )
				,builder.sum( root.<Integer>get("participantNumber"))
				,builder.sum( root.<Integer>get("staffNumber")      )
				,builder.sum( root.<BigDecimal>get("incomingsTotal")      ).as(BigDecimal.class)
				,builder.sum( root.<BigDecimal>get("outcomingsTotal")     ).as(BigDecimal.class)
				,builder.sum( root.<BigDecimal>get("valueToMountain")     ).as(BigDecimal.class)
				,builder.sum( root.<BigDecimal>get("valueToFoundation")   ).as(BigDecimal.class)
				,builder.sum( root.<BigDecimal>get("valueToFacilitators") ).as(BigDecimal.class)
				,builder.sum( root.<BigDecimal>get("valueToProductors")   ).as(BigDecimal.class)
				,builder.sum( root.<BigDecimal>get("valueToMarketing")    ).as(BigDecimal.class)
				);
		
		criteria.select( selection )
			.where( conjunction )
			.groupBy( joinProduction.<School>get("school") );
		

		//2.totaliza os valores ja pagos
		List<RendicionBySchoolDTO> dtos = manager.createQuery(criteria).getResultList();
		
		//3.realiza totalizacoes e pesquisa por sub-agregados
		for (RendicionBySchoolDTO dtoVar : dtos) {
			BigDecimal totalPaid = totalizeRendicionPaymentBySchoolAndYearAndMounths(dtoVar.getSchool(), year, months);
			dtoVar.setTotalPaid(totalPaid);
			
			//lista de sub-agregados por modulo
			List<RendicionByModuleDTO> rendicionByModuloDtos = searchRendicionModuleDTOBySchoolAndYearAndMonths(dtoVar.getSchool(), year, months);
			dtoVar.setRendicionByModuleDtos(rendicionByModuloDtos);
			
			//lista de sub-agregados por cidade
			List<RendicionByCityDTO> rendicionByCityDtos = searchRendicionCityDTOBySchoolByYearByMonths(dtoVar.getSchool(), year, months);
			dtoVar.setRendicionByCityDtos(rendicionByCityDtos);
		}
		
		return dtos;
	}




	/**
	 * Totaliza os pagamento dado uma escola, ano e lista de meses.
	 * @param school
	 * @param year
	 * @param months
	 * @return
	 */
	private BigDecimal totalizeRendicionPaymentBySchoolAndYearAndMounths(School school, Integer year, List<Integer> months) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> criteria = builder.createQuery(BigDecimal.class);
		
		Root<RendicionPayment> rootRendicionPayment = criteria.from(RendicionPayment.class);
		Join<RendicionPayment, Rendicion> joinRendicion = rootRendicionPayment.<RendicionPayment, Rendicion>join("rendicion");
		Join<Rendicion, Production> joinProduction = joinRendicion.<Rendicion, Production>join("production");
		
		Predicate conjunction = builder.conjunction();
		//1.school
		if (isNotNull(school) ) {
			conjunction = builder.and(conjunction,
				builder.equal(joinProduction.<School>get("school"), school)
				);
		}
		//2.year
		if (isNotNegative(year) ) {
			conjunction = builder.and(conjunction,
				builder.equal( builder.function("YEAR", Integer.class, joinProduction.<Date>get("firstDate")), year )	
				);
		}
		//3.meses
		if (isNotEmpty(months)) {
			conjunction = builder.and(conjunction,
					builder.function("MONTH", Integer.class, joinProduction.<Date>get("firstDate")).in( months )	
				);
		}
		//prepara criteria...
		criteria.select( builder.sum(rootRendicionPayment.<BigDecimal>get("paymentValue")).as(BigDecimal.class) )
			.where( conjunction )
			.groupBy( joinProduction.<School>get("school") )
			;
		
		//executa e trata nulls...
		BigDecimal totalPaid = null;
		try {
			totalPaid = manager.createQuery(criteria).getSingleResult();
			
		} catch (NoResultException e) {
			//totalPaid continuara null...
		}
		
		return totalPaid!=null ? totalPaid : NumberUtil.VALUE_ZERO;
	}
	
	
	/**
	 * Pesquisa pela rendiciones agrupdadas por modulo para um ano e uma lista de meses. 
	 * @param year
	 * @param months
	 * @return
	 */
	public List<RendicionByModuleDTO> searchRendicionModuleDTOBySchoolAndYearAndMonths(School school, Integer year, List<Integer> months) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RendicionByModuleDTO> criteria = builder.createQuery(RendicionByModuleDTO.class);
		
		Root<Rendicion> rootRendicion = criteria.from(Rendicion.class);
		Join<Rendicion, Production> joinProduction = rootRendicion.<Rendicion, Production>join("production");
		
		Predicate conjunction = builder.conjunction();
		
		//1.escola
		if (isNotNull(school)) {
			conjunction = builder.and(conjunction,
					builder.equal( joinProduction.<School>get("school"), school )
				);
		}
		
		//2.ano
		if (isNotNegative(year)) {
			conjunction = builder.and(conjunction,
					builder.equal( builder.function("YEAR", Integer.class, joinProduction.<Date>get("firstDate")), year)
				);
		}
		
		//3.meses
		if (isNotEmpty(months)) {
			conjunction = builder.and(conjunction,
					builder.function("MONTH", Integer.class, joinProduction.<Date>get("firstDate"))
						.in( months )
				);
		}
		
		//prepara a criteria
		criteria
			.select( builder.construct( RendicionByModuleDTO.class
						,joinProduction.<Module>get("module")
						,builder.count( rootRendicion )
						,builder.sum( rootRendicion.<Integer>get("participantNumber"))
						,builder.sum( rootRendicion.<Integer>get("staffNumber")      )
						,builder.sum( rootRendicion.<BigDecimal>get("incomingsTotal")      ).as(BigDecimal.class)
						,builder.sum( rootRendicion.<BigDecimal>get("outcomingsTotal")     ).as(BigDecimal.class)
						,builder.sum( rootRendicion.<BigDecimal>get("valueToMountain")     ).as(BigDecimal.class)
						,builder.sum( rootRendicion.<BigDecimal>get("valueToFoundation")   ).as(BigDecimal.class)
						,builder.sum( rootRendicion.<BigDecimal>get("valueToFacilitators") ).as(BigDecimal.class)
						,builder.sum( rootRendicion.<BigDecimal>get("valueToProductors")   ).as(BigDecimal.class)
						,builder.sum( rootRendicion.<BigDecimal>get("valueToMarketing")    ).as(BigDecimal.class)
						)
					)
			.where( conjunction )
			.groupBy( joinProduction.<Module>get("module") )
			.orderBy( builder.asc(joinProduction.<School>get("school")) )
			;
		
		//2.totaliza os valores ja pagos
		List<RendicionByModuleDTO> dtos = manager.createQuery(criteria).getResultList();
		
		return dtos;
	}
	

	/**
	 * Pesquisa rendicion agrupado por module e cidade
	 * @param school
	 * @param year
	 * @param months
	 * @return
	 */
	private List<RendicionByCityDTO> searchRendicionCityDTOBySchoolByYearByMonths(School school, Integer year, List<Integer> months) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RendicionByCityDTO> criteria = builder.createQuery(RendicionByCityDTO.class);
		
		Root<Rendicion> rootRendicion = criteria.from(Rendicion.class);
		Join<Rendicion, Production> joinProduction = rootRendicion.<Rendicion, Production>join("production");
		
		Predicate conjunction = builder.conjunction();
		
		//1.escola
		if (isNotNull(school)) {
			conjunction = builder.and(conjunction,
					builder.equal( joinProduction.<School>get("school"), school )
				);
		}
		//2.ano
		if (isNotNegative(year)) {
			conjunction = builder.and(conjunction,
					builder.equal( builder.function("YEAR", Integer.class, joinProduction.<Date>get("firstDate")), year)
				);
		}
		//3.meses
		if (isNotEmpty(months)) {
			conjunction = builder.and(conjunction,
					builder.function("MONTH", Integer.class, joinProduction.<Date>get("firstDate"))
						.in( months )
				);
		}
		
		//prepara a criteria
		criteria
			.select( builder.construct( RendicionByCityDTO.class
									  , joinProduction.<School>get("school")
									  , joinProduction.<Module>get("module")
									  , joinProduction.<City>get("city")
									  , builder.count( rootRendicion )
									  , builder.sum( rootRendicion.<Integer>get("participantNumber"))
									  , builder.sum( rootRendicion.<Integer>get("staffNumber")      )
									  , builder.sum( rootRendicion.<BigDecimal>get("incomingsTotal")      ).as(BigDecimal.class)
									  , builder.sum( rootRendicion.<BigDecimal>get("outcomingsTotal")     ).as(BigDecimal.class)
									  , builder.sum( rootRendicion.<BigDecimal>get("valueToMountain")     ).as(BigDecimal.class)
									  , builder.sum( rootRendicion.<BigDecimal>get("valueToFoundation")   ).as(BigDecimal.class)
									  , builder.sum( rootRendicion.<BigDecimal>get("valueToFacilitators") ).as(BigDecimal.class)
									  , builder.sum( rootRendicion.<BigDecimal>get("valueToProductors")   ).as(BigDecimal.class)
									  , builder.sum( rootRendicion.<BigDecimal>get("valueToMarketing")    ).as(BigDecimal.class)
					)
				)
			.where( conjunction )
			.groupBy( joinProduction.<School>get("school")
					, joinProduction.<Module>get("module")
					, joinProduction.<City>get("city")
				);
		
		//2.totaliza os valores ja pagos
		List<RendicionByCityDTO> dtos = manager.createQuery(criteria).getResultList();
		
		for (RendicionByCityDTO dtoVar : dtos) {
			School s = dtoVar.getSchool();
			Module m = dtoVar.getModule();
			City c = dtoVar.getCity();
			BigDecimal totalPaid = totalizeRendicionPaymentBySchoolByModuleByCityByYearByMounths(s, m, c, year, months);
			dtoVar.setTotalPaid(totalPaid);
		}
		
		return dtos;
	}

	
	/**
	 * Totaliza os pagamento por escola, module e cidade
	 * @param school
	 * @param module
	 * @param city
	 * @param year
	 * @param months
	 * @return
	 */
	private BigDecimal totalizeRendicionPaymentBySchoolByModuleByCityByYearByMounths(School school, Module module, City city, Integer year, List<Integer> months) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> criteria = builder.createQuery(BigDecimal.class);
		
		Root<RendicionPayment> rootRendicionPayment = criteria.from(RendicionPayment.class);
		Join<RendicionPayment, Rendicion> joinRendicion = rootRendicionPayment.<RendicionPayment, Rendicion>join("rendicion");
		Join<Rendicion, Production> joinProduction = joinRendicion.<Rendicion, Production>join("production");
		
		Predicate conjunction = builder.conjunction();
		//1.school
		conjunction = builder.and(conjunction,
			builder.equal(joinProduction.<School>get("school"), school)
		);

		//2.module
		conjunction = builder.and(conjunction,
			builder.equal(joinProduction.<Module>get("module"), module)
		);
		
		//3.city
		conjunction = builder.and(conjunction,
			builder.equal(joinProduction.<City>get("city"), city)
		);
		
		//4.year
		if (isNotNegative(year) ) {
			conjunction = builder.and(conjunction,
				builder.equal( builder.function("YEAR", Integer.class, joinProduction.<Date>get("firstDate")), year )	
				);
		}
		//5.meses
		if (isNotEmpty(months)) {
			conjunction = builder.and(conjunction,
					builder.function("MONTH", Integer.class, joinProduction.<Date>get("firstDate")).in( months )	
				);
		}
		//prepara criteria...
		criteria.select( builder.sum(rootRendicionPayment.<BigDecimal>get("paymentValue")).as(BigDecimal.class) )
			.where( conjunction )
			;
		
		//executa e trata nulls...
		BigDecimal totalPaid = null;
		try {
			totalPaid = manager.createQuery(criteria).getSingleResult();
			
		} catch (NoResultException e) {
			//totalPaid continuara null...
		}
		
		return totalPaid!=null ? totalPaid : NumberUtil.VALUE_ZERO;
	}
	
	
	
	/* *******
	 * dividas
	 *********/
	/**
	 * Pesquisa pelo DTO de divida em etapas
	 * 1) Seleciona todos os registers e eventos 
	 * 2) Filtra somente os valor pendente maior que zero
	 * @param megaEvents
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@SuppressWarnings("unchecked")
	public List<DebtDTO> searchDebtDTOByMegaEvents(List<MegaEvent> megaEvents, List<RegisterStatus> registerStatusList) {
		//1.seleciona todos os registros e eventos do megaevento
		List<DebtDTO> allDebtDtos = manager.createNamedQuery("searchDebtDTOByMegaEvents")
			.setParameter("pMegaEvents", megaEvents)
			.setParameter("pRegisterStatusList", registerStatusList)
			.getResultList();
		
		//2.para cada um, totaliza os pagamentos
		for (DebtDTO dtoVar : allDebtDtos) {
			BigDecimal valuePaid = totalizeValuePaidByMegaEventAndRegisterAndEvent( dtoVar.getMegaEvent(), dtoVar.getRegister(), dtoVar.getEvent() );
			dtoVar.setValuePaid( valuePaid );
		}
		
		//3.filter somente os que tem valor pendente
		List<DebtDTO> filteredDebtDtos = new ArrayList<>();
		for (DebtDTO debtDtoVar : allDebtDtos) {
			if (NumberUtil.isDifferenceThanZero( debtDtoVar.getCalculatedPendentValue() )) {
				filteredDebtDtos.add( debtDtoVar );
			}
		}
		
		return filteredDebtDtos;
	}
	
	
	/**
	 * Totaliza os valores pagos pelo megavento, register e evento
	 * @param me
	 * @param r
	 * @param e
	 * @return
	 */
	private BigDecimal totalizeValuePaidByMegaEventAndRegisterAndEvent(MegaEvent me, Register r, Event e) {
		try {
			return manager.createNamedQuery("totalizeValuePaidByMegaEventAndRegisterAndEvent", BigDecimal.class) 
					.setParameter("pMegaEvent", me)
					.setParameter("pRegister" , r )
					.setParameter("pEvent"    , e )
					.getSingleResult();
			
		} catch (NoResultException ex) {
			return NumberUtil.VALUE_ZERO;
		}
	}
	
	
}

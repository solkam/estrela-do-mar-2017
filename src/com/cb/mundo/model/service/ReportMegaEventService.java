package com.cb.mundo.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.dao.ReportMegaEventDAO;
import com.cb.mundo.model.dto.ComissionDTO;
import com.cb.mundo.model.dto.ComissionDetailDTO;
import com.cb.mundo.model.dto.EventSummary;
import com.cb.mundo.model.dto.EventWeekRegisterSummary;
import com.cb.mundo.model.dto.EventWeekSummary;
import com.cb.mundo.model.dto.MegaEventMovimentSummary;
import com.cb.mundo.model.dto.RegisterPaymentMethodSummary;
import com.cb.mundo.model.dto.RegisterSummary;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.RegisterDetailPayment;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.util.CalendarUtil;

/**
 * EJB para o Relatorio de Inscricao
 * 
 * @author Solkam
 * @since 25 nov 2012
 */
@Stateless
public class ReportMegaEventService {
	
	@PersistenceContext
	private EntityManager manager;
	
	private ReportMegaEventDAO reportMegaEventDAO;
	
	@PostConstruct
	protected void init() {
		reportMegaEventDAO = new ReportMegaEventDAO(manager);
	}
	
	
	/** DEPRECATED: 
	 * Informe 7.1: INFORME DE SALDO AGREGADO POR SEMANA E DETALHANDO PESSOAS E EVENTO
	 * Pesquisa EventWeek sumarizando o total e interage buscando os detalhes de inscricao
	 * Tambem totaliza os valores pagos
	 * 
	 * @deprecated: agora uma lista de semana sao selecionadas...
	 * @param megaEvent
	 * @return lista de sumario de semanas
	 */
	public List<EventWeekSummary> searchEventWeekSummaryByMegaEvent(MegaEvent megaEvent) {
		List<EventWeekSummary> summaries = reportMegaEventDAO.searchEventWeekSummaryByMegaEvent(megaEvent);
		for (EventWeekSummary summary : summaries) {
			List<RegisterDetail> registerDetails = reportMegaEventDAO.searchRegisterDetailByEventWeek(summary.getEventWeek());
			summary.setRegisterDetails(registerDetails);//implicitamente, totoliza os valores
		}
		return summaries;
	}
	
	
	/**
	 * Informe 7.1: INFORME DE SALDO AGREGADO POR SEMANA E DETALHANDO PESSOAS E EVENTO
	 * A partir de uma lista de semanas selecionadas, pesquisar os valores de cada inscricao...
	 * @param eventWeeks
	 * @return lista de sumarios de semanas
	 */
	public List<EventWeekSummary> searchEventWeekSummaryByEventWeeks(List<EventWeek> eventWeeks) {
		List<EventWeekSummary> summaries = new ArrayList<EventWeekSummary>();
		for (EventWeek eventWeek : eventWeeks) {
			eventWeek = reportMegaEventDAO.findEventWeekById( eventWeek.getId() );
			List<RegisterDetail> registerDetails = reportMegaEventDAO.searchRegisterDetailByEventWeek(eventWeek);
			
			EventWeekSummary summary = new EventWeekSummary(eventWeek, registerDetails);
			summaries.add( summary );
		}
		return summaries;
	}

	/**
	 * 7.2: INFORME DE SALDO AGREGADO POR PESSOA E DETALHANDO SEMANA
	 * Pesquisa Register sumarizando por semana, totalizando 
	 * @param megaEvent
	 * @return
	 */
	public List<RegisterSummary> searchRegisterSummaryByMegaEventAndContact(MegaEvent megaEvent, Contact contact) {
		List<RegisterSummary> registerSummaries = reportMegaEventDAO.searchRegisterSummaryByMegaEvent(megaEvent, contact);
		
		for (RegisterSummary registerSummary : registerSummaries) {
			Register register = registerSummary.getRegister();
			List<EventWeekSummary> eventWeekSummaries = reportMegaEventDAO.searchEventWeekSummaryByRegister( register );
			
			for (EventWeekSummary eventWeekSummary : eventWeekSummaries) {
				EventWeek eventWeek = eventWeekSummary.getEventWeek();
				List<RegisterDetail> details = reportMegaEventDAO.searchRegisterDetailByRegisterAndEventWeek(register, eventWeek);
				eventWeekSummary.setRegisterDetails(details);
			}

			registerSummary.setEventWeekSummaries(eventWeekSummaries);
		}
		return registerSummaries;
	}
	
	
	/**
	 * Pesquisa inscritos de maneira geral
	 */
	public List<RegisterDetail> searchRegisterDetailByMegaEvent(MegaEvent megaEvent) {
		return reportMegaEventDAO.searchRegisterDetailByMegaEvent(megaEvent);
	}


	
	
	/**
	 * Pesquisa Event summarizando o total e interage buscando a lista de inscricoes
	 * @param megaEvent
	 * @return lista do sumario de eventos
	 */
	public List<EventSummary> searchEventSummaryByMegaEvent(MegaEvent megaEvent) {
		List<EventSummary> summaries = reportMegaEventDAO.searchEventSummaryByMegaEvent(megaEvent);
		for (EventSummary summary : summaries) {
			List<RegisterDetail> registerDetails = reportMegaEventDAO.searchRegisterDetailByEvent(summary.getEvent());
			summary.setRegisterDetails(registerDetails);
		}
		return summaries;
	}


	/**
	 * Informe 7.2: Forma de Pagamento por MegaEvento e datas
	 * @param megaEvent
	 * @param firstDate
	 * @param lastDate
	 * @return
	 */
	public List<RegisterPaymentMethodSummary> searchRegisterPaymentMethodSummaryByMegaEvent(MegaEvent megaEvent, Date firstDate, Date lastDate) {
		List<RegisterPaymentMethodSummary> summaries = reportMegaEventDAO.searchRegisterPaymentMethodSummaryByMegaEvent(megaEvent,	firstDate, lastDate);
	
		for (RegisterPaymentMethodSummary summary : summaries) {
			
			List<RegisterDetailPayment> payments = reportMegaEventDAO.searchRegisterDetailPaymentsByMegaEventAndMethodAndBetweenDates(
					megaEvent, 
					summary.getPaymentMethod(), 
					firstDate, 
					lastDate);
					
			summary.setPayments(payments);
		}
		return summaries;
	}
	
	
	/**
	 * Informe 7.5: Pessoas por semana
	 * A partir de todas as semanas (selecionadas como filtro), seleciona os registers.
	 * 
	 * 
	 * @param megaEvent
	 * @return lista de EventWeekContactSummary
	 */
	public List<EventWeekRegisterSummary> searchEventWeekRegisterSummaryByEventWeeksAndStatusList(List<EventWeek> eventWeeks, List<RegisterStatus> statusList) {
		List<EventWeekRegisterSummary> summaries = new ArrayList<>();
		
		//1.para cada semana...
		for (EventWeek eventWeek : eventWeeks) {
			//2.recupera o objeto completo
			Long eventWeekId = eventWeek.getId();
			eventWeek = reportMegaEventDAO.findEventWeekById( eventWeekId );
			
			//3.pesquisa todas as inscricoes (registros)
			List<Register> registers = reportMegaEventDAO.searchDistinctRegisterByEventWeekAndStatusList(eventWeek, statusList);
			
			EventWeekRegisterSummary summary = new EventWeekRegisterSummary(eventWeek, registers);
			summaries.add( summary );
		}
		return summaries;
	}
	
	
	/**
	 * Informe 7.6: Movimento de pessoas no megaevento
	 * @param megaEvent
	 * @param initDate
	 * @param finalDate
	 * @return lista de MegaEventMovimentSummary
	 */
	public List<MegaEventMovimentSummary> searchMegaEventMovimentSummary(MegaEvent megaEvent, Date initDate, Date finalDate) {
		List<MegaEventMovimentSummary> summaries = new ArrayList<MegaEventMovimentSummary>();
		
		EventWeek eventWeek   = null;
		Long insideQuantity   = 0L;
		Long arrivingQuantity = 0L;
		Long arrivingLeaving  = 0L;
		
		List<Date> dateList = CalendarUtil.buildDateList(initDate, finalDate); 
		for (Date date : dateList) {
			insideQuantity   = reportMegaEventDAO.countInsideOnesByMegaEventAndDate(megaEvent, date);
			arrivingQuantity = reportMegaEventDAO.countArrivingOnesByMegaEventAndDate(megaEvent, date);
			arrivingLeaving  = reportMegaEventDAO.countLeavingOnesByMegaEventAndDate(megaEvent, date);
			eventWeek        = reportMegaEventDAO.findEventWeekByMegaEventAndBetweenDate(megaEvent, date);
			
			MegaEventMovimentSummary summary = new MegaEventMovimentSummary(date, eventWeek, insideQuantity, arrivingQuantity, arrivingLeaving);
			summaries.add(summary);
		}
		return summaries;
	}


	/**
	 * Informe 7.7: Pessoas no MegaEvento por Data
	 * @param megaEvent
	 * @param date
	 * @return
	 */
	public List<RegisterDetail> searchRegisterDetailByMegaEventAndDate(	MegaEvent megaEvent, Date date) {
		return reportMegaEventDAO.searchRegisterDetailByMegaEventAndDate(megaEvent, date);
	}
	
	/**
	 * Informe: Pessoas para Checkin por Date
	 * (Os status permitido sao; INCOMPLETE, REGISTERED, CHECKEDIN)
	 * @param megaEvent
	 * @param date
	 * @return
	 */
	public List<Register> searchRegisterByMegaEventAndStatusListAndBeetweenCheckinDates(MegaEvent megaEvent, List<RegisterStatus> statusList, Date date1, Date date2) {
		List<Register> registers = new ArrayList<>();
		for (RegisterStatus status : statusList) {
			registers.addAll( reportMegaEventDAO.searchRegisterByMegaEventAndStatusAndBetweenPreviewCheckinDates(megaEvent, status, date1, date2) );
		}
		return registers;
	}


	
	public List<Register> searchRegisterForCheckOutByMegaEventAndDates(MegaEvent megaEvent, Date initialDate, Date endDate, RegisterStatus status) {
		return reportMegaEventDAO.searchRegisterByMegaEventAndBetweenPreviewCheckoutDateAndStatus(megaEvent, initialDate, endDate, status);
	}


	
	public List<MedicalAnswer> searchMedicalAnswerWithLimitationByMegaEventAndDates(MegaEvent megaEvent, Date initDate, Date endDate) {
		return reportMegaEventDAO.searchMedicalAnswerWithLimitationByMegaEventAndDates(megaEvent, initDate, endDate);
	}
	
	
	/**
	 * Informe de pagamentos de um evento 
	 * Seleciona todos detalhes de um evento e filtra pelas presencas selecionadas
	 * 
	 * (solicitado por Lamandu em 17Jan13)
	 * @param event
	 * @return
	 */
	public List<RegisterDetail> searchRegisterDetailByEventAndPresencesWithPayments(
			List<Event> events, 
			EventPresence[] presences, 
			List<RegisterStatus> statusList) {

		//1.seleciona todos os details...
		List<RegisterDetail> details = new ArrayList<RegisterDetail>();
		for (EventPresence presence : presences) {
			for (RegisterStatus status : statusList) {
				details.addAll( reportMegaEventDAO.searchRegisterDetailByEventAndStatusAndPresence(events, status, presence) );
			}
		}
		
		//2.forca a carga dos pagamentos
		for (RegisterDetail detail : details) {
			detail.getCalculatedPaidValue();
			detail.getCalculatedPendentValue();
			detail.getRegister().getContact().getProductorContact();
		}
		return details;
	}
	


	/**
	 * "Informe Pessoas por Evento"
	 * Pesquisar por RegisterDetails pelo evento e array de presencas
	 * @param event
	 * @param presences
	 * @return
	 */
	public List<RegisterDetail> searchRegisterDetailByEventAndInPresencesAndInStatus(List<Event> events, EventPresence[] presences, List<RegisterStatus> statusList) {
		List<RegisterDetail> details = new ArrayList<RegisterDetail>();
		for (Event event : events) {
			for (EventPresence presence : presences) {
				for (RegisterStatus status : statusList) {
					details.addAll( reportMegaEventDAO.searchRegisterDetailByEventAndInPresences(event, presence, status) );
				}
			}
		}
		return details;
	}
	

	/**
	 * "Informe de Comissao de Produtores"
	 * Monta lista de dtos de comissoes e seus detalhes
	 * @param megaEvent
	 * @return
	 */
	public List<ComissionDTO> searchComissionDTO() {
		return null;
	}
	

	@SuppressWarnings("unchecked")
	public List<ComissionDetailDTO> searchComissionDetailDTOByMegaEvent(MegaEvent megaEvent, Integer percentual, BigDecimal subractValue) {
		List<ComissionDetailDTO> detailDtos = manager.createNamedQuery("searchComissionDetailDTOByMegaEvent")
					.setParameter("pMegaEvent", megaEvent)
					.setParameter("pPresence", EventPresence.PARTICIPANT)
					.getResultList();
		
		for (ComissionDetailDTO detailDtoVar : detailDtos) {
			detailDtoVar.setPercentualInteger(percentual);
			detailDtoVar.setSubractValue(subractValue);
		}
		
		return detailDtos;
	}
	
	
}

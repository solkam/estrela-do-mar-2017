package com.cb.mundo.model.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;

import com.cb.mundo.model.dto.EventSummary;
import com.cb.mundo.model.dto.EventWeekSummary;
import com.cb.mundo.model.dto.ComissionDTO;
import com.cb.mundo.model.dto.RegisterPaymentMethodSummary;
import com.cb.mundo.model.dto.RegisterSummary;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterCredit;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.RegisterDetailPayment;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;

/**
 * DAO para relatorios de MegaEventos
 * @author Solkam
 * @since 09 set 2012
 */
public class ReportMegaEventDAO {
	
	private EntityManager manager;
	
	public ReportMegaEventDAO(EntityManager manager) {
		this.manager = manager;
	}
	
	/**
	 * Informe 7.1: Informe de Semana por pessoa e saldo
	 * @param megaEvent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EventWeekSummary> searchEventWeekSummaryByMegaEvent(MegaEvent megaEvent) {
		return manager.createNamedQuery("EventWeekSummary.searchByMegaEvent")
				.setParameter("pMegaEvent", megaEvent)
				.getResultList();
	}
	
	public List<RegisterDetail> searchRegisterDetailByEventWeek(EventWeek eventWeek) {
		return manager.createNamedQuery("RegisterDetail.searchByEventWeek", RegisterDetail.class)
				.setParameter("pEventWeek", eventWeek)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<EventSummary> searchEventSummaryByMegaEvent(MegaEvent megaEvent) {
		return manager.createNamedQuery("EventSummary.searchByMegaEvent")
				.setParameter("pMegaEvent", megaEvent)
				.getResultList();
	}
	
	public List<RegisterDetail> searchRegisterDetailByEvent(Event event) {
		return manager.createNamedQuery("RegisterDetail.searchByEvent", RegisterDetail.class)
				.setParameter("pEvent", event)
				.getResultList();
	}
	
	public List<RegisterDetail> searchRegisterDetailByEventAndStatusAndPresence(List<Event> events, RegisterStatus status, EventPresence presence) {
		return manager.createNamedQuery("searchRegisterDetailByEventAndStatusAndPresence", RegisterDetail.class)
				.setParameter("pEvents", events)
				.setParameter("pStatus", status)
				.setParameter("pPresence", presence)
				.getResultList();
	}
	
	public List<RegisterDetail> searchRegisterDetailByMegaEvent(MegaEvent megaEvent) {
		return manager.createNamedQuery("RegisterDetail.searchByMegaEvent", RegisterDetail.class)
				.setParameter("pMegaEvent", megaEvent)
				.getResultList();
	}
	
	/** 
	 * Informe 7.2: Informe por Pessoa, Saldos
	 * @param megaEvent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RegisterSummary> searchRegisterSummaryByMegaEvent(MegaEvent megaEvent, Contact contact) {
		return manager.createNamedQuery("RegisterSummary.searchByMegaEventAndContact")
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	/**
	 * Informe 7.2 (auxiliar)
	 * Um Register, agregada as semanas totalizando os valores
	 * @param register
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<EventWeekSummary> searchEventWeekSummaryByRegister(Register register) {
		return manager.createNamedQuery("EventWeekSummary.searchByRegister")
				.setParameter("pRegister", register)
				.getResultList();
	}

	/**
	 * Informe 7.2 (auxiliar 2)
	 * Pesquisar os detalhes de um register para uma semana
	 * @param megaEvent
	 * @param eventWeek
	 * @return
	 */
	public List<RegisterDetail> searchRegisterDetailByRegisterAndEventWeek(Register register, EventWeek eventWeek) {
		return manager.createNamedQuery("RegisterDetail.serchByRegisterAndEventWeek", RegisterDetail.class)
				.setParameter("pRegister", register)
				.setParameter("pEventWeek", eventWeek)
				.getResultList();
	}
	
	/**
	 * Informe 7.3: Forma de Pagamento por MegaEvent e datas
	 * @param megaEvent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RegisterPaymentMethodSummary> searchRegisterPaymentMethodSummaryByMegaEvent(MegaEvent megaEvent, Date firstDate, Date lastDate) {
		return manager.createNamedQuery("RegisterPaymentMethodSummary.searchByMegaEventAndDates")
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pFirstDate", firstDate)
				.setParameter("pLastDate", lastDate)
				.getResultList()
				;
	}

	/**
	 * Informe 7.3 (aux): Seleciona as inscricoes que tenham tido pagamentos entre as datas
	 * @param megaEvent
	 * @param firstDate
	 * @param lastDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RegisterDetailPayment> searchRegisterDetailPaymentsByMegaEventAndMethodAndBetweenDates(
			MegaEvent megaEvent,
			MegaEventPaymentMethod method,
			Date firstDate,
			Date lastDate) {
		
		return manager.createNamedQuery("RegisterDetailPayment.searchByMegaEventAndMethodAndBetweenDates")
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pPaymentMethod", method)
				.setParameter("pFirstDate", firstDate)
				.setParameter("pLastDate", lastDate)
				.getResultList();
	}

	public EventWeek findEventWeekById(Long id) {
		return manager.find(EventWeek.class, id);
	}
	
	
	public List<Register> searchDistinctRegisterByEventWeekAndStatusList(EventWeek eventWeek, List<RegisterStatus> statusList) {
		return manager.createNamedQuery("searchDistinctRegisterByEventWeekAndStatusList", Register.class)
				.setParameter("pEventWeek", eventWeek)
				.setParameter("pStatusList", statusList)
				.getResultList();
	}

	/**
	 * Informe 7.6: Movimento no acampamentos: pessoas dentro do megaevento
	 * @param megaEvent
	 * @param date
	 * @return
	 */
	public Long countInsideOnesByMegaEventAndDate(MegaEvent megaEvent, Date date) {
		return manager.createNamedQuery("countInsideOnesByMegaEventAndDate", Long.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pDate", date)
				.getSingleResult();
	}
	
	/**
	 * Informe 7.6: Movimento no acampamentos: pessoas entrando no megaevento
	 * @param megaEvent
	 * @param date
	 * @return
	 */
	public Long countArrivingOnesByMegaEventAndDate(MegaEvent megaEvent, Date date) {
		return manager.createNamedQuery("countArrivingOnesByMegaEventAndDate", Long.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pDate", date)
				.getSingleResult();
	}
	
	/**
	 * Informe 7.6: Movimento no acampamentos: pessoas saindo do megaevento
	 * @param megaEvent
	 * @param date
	 * @return
	 */
	public Long countLeavingOnesByMegaEventAndDate(MegaEvent megaEvent, Date date) {
		return manager.createNamedQuery("countLeavingOnesByMegaEventAndDate", Long.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pDate", date)
				.getSingleResult();
	}

	/**
	 * Informe 7.6: Movimento no acampamento: semana de uma data especifica
	 * @param initDate
	 * @param finalDate
	 * @return EventWeek referente a data
	 */
	public EventWeek findEventWeekByMegaEventAndBetweenDate(MegaEvent megaEvent, Date date) {
		try {
			return manager.createNamedQuery("EventWeek.findByMegaEventAndBetweenDate", EventWeek.class)
					.setParameter("pMegaEvent", megaEvent)
					.setParameter("pDate", date)
					.setParameter("pOficial", Boolean.TRUE)
					.getSingleResult();
			
		} catch (NonUniqueResultException e) {
			return null;
			
		} catch (NoResultException e) {
			//Mais um chanche lidando agora com eventos nao-oficiais
			try {
				return manager.createNamedQuery("EventWeek.findByMegaEventAndBetweenDate", EventWeek.class)
						.setParameter("pMegaEvent", megaEvent)
						.setParameter("pDate", date)
						.setParameter("pOficial", Boolean.FALSE)
						.getSingleResult();

			} catch(PersistenceException e2) {
				return null;
			}
		}
	}
	
	/**
	 * Informe 7.7: Pessoas no MegaEvento por Data
	 * Considera apenas os eventos do tipo FORMATION e STAFF
	 * @param megaEvent
	 * @param date
	 * @return lista de RegisterDetail
	 */
	public List<RegisterDetail> searchRegisterDetailByMegaEventAndDate(MegaEvent megaEvent, Date date) {
		return manager.createNamedQuery("RegisterDetail.searchByMegaEventAndDate", RegisterDetail.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pDate", date)
				.getResultList();
	}
	
	/**
	 * Informe: Pessoas para Checkin por Data (checkin previsto).
	 * (Usa previewCheckinDate para status INCOMPLETE ou REGISTERED)
	 * (Usa checkinDate para status CHECKEDIN)
	 * (lanca exception se vier outro status
	 * @param megaEvent
	 * @param date
	 * @return lista de register detail
	 */
	public List<Register> searchRegisterByMegaEventAndStatusAndBetweenPreviewCheckinDates(MegaEvent megaEvent, RegisterStatus status, Date date1, Date date2) {
		if (RegisterStatus.INCOMPLETE.equals(status) || RegisterStatus.REGISTERED.equals(status)) {
			return manager.createNamedQuery("searchRegisterByMegaEventAndStatusAndBetweenPreviewCheckinDates", Register.class)
					.setParameter("pMegaEvent", megaEvent)
					.setParameter("pDate1", date1)
					.setParameter("pDate2", date2)
					.setParameter("pStatus", status)
					.getResultList();
		}
		
		if (RegisterStatus.CHECKEDIN.equals(status)) {
			return manager.createNamedQuery("searchRegisterByMegaEventAndStatusAndBetweenCheckinDates", Register.class)
					.setParameter("pMegaEvent", megaEvent)
					.setParameter("pDate1", date1)
					.setParameter("pDate2", date2)
					.setParameter("pStatus", status)
					.getResultList();
		}
		
		throw new IllegalArgumentException("Status "+status+" nao suportado pelo metodo");
	}


	
	
	/**
	 * Informe: Pessoas para Checkout por range de datas
	 * @param megaEvent
	 * @param initialDate
	 * @param endDate
	 * @return
	 */
	public List<Register> searchRegisterByMegaEventAndBetweenPreviewCheckoutDateAndStatus(MegaEvent megaEvent, Date initialDate, Date endDate, RegisterStatus status) {
		return manager.createNamedQuery("searchRegisterByMegaEventAndBetweenPreviewCheckoutDateAndStatus", Register.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pInitialDate", initialDate)
				.setParameter("pEndDate", endDate)
				.setParameter("pStatus", status)
				.getResultList();
	}

	/**
	 * Informe de limitacoes fisicas:
	 * Respostas 'sim' na ficha medica
	 * @param megaEvent
	 * @param initDate
	 * @param endDate
	 * @return
	 */
	public List<MedicalAnswer> searchMedicalAnswerWithLimitationByMegaEventAndDates(MegaEvent megaEvent, Date initDate, Date endDate) {
		return manager.createNamedQuery("MedicalAnswer.searchWithLimitationByMegaEventAndDates", MedicalAnswer.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pCreateInitDate" , initDate)
				.setParameter("pCreateEndDate"  , endDate)
				.setParameter("pUpdateInitDate" , initDate)
				.setParameter("pUpdateEndDate"  , endDate)
				.getResultList();
	}
	
	/**
	 * Informe de Pessoas por Evento
	 * @param event
	 * @param registerStatus 
	 * @param presences
	 * @return
	 */
	public List<RegisterDetail> searchRegisterDetailByEventAndInPresences(Event event, EventPresence presence, RegisterStatus registerStatus) {
		return manager.createNamedQuery("searchRegisterDetailByEventAndInPresenceAndInRegister", RegisterDetail.class)
				.setParameter("pEvent", event)
				.setParameter("pPresence", presence)
				.setParameter("pRegisterStatus", registerStatus)
				.getResultList();
	}


	/**
	 * Informe de Comissao de Produtores
	 * Pesquisa os produtores de um mega evento.
	 * @param megaEvent
	 * @return
	 */
	public List<Contact> searchContactProductorByMegaEvent(MegaEvent megaEvent) {
		return manager.createNamedQuery("Contact.searchProductorByMegaEvent", Contact.class)
				.setParameter("pMegaEvent", megaEvent)
				.getResultList();
	}

	
	

}

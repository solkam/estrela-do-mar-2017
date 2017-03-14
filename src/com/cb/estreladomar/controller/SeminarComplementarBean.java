package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.estreladomar.controller.util.Navigation;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.exception.ContactAlreadyAddedToSeminarComplementarException;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.RegisterService;
import com.cb.mundo.model.service.SeminarComplementarService;

/**
 * Controller para UC Gerenciar Seminario Complementar
 * 
 * @author Solkam
 * @since 25 DEZ 2013
 */
@ManagedBean(name="seminarCompBean")
@SessionScoped
public class SeminarComplementarBean implements Serializable {
	
	@EJB SeminarComplementarService seminarComplementarService;
	@EJB EventService eventService;
	@EJB RegisterService registerService;

	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	//combo
	private MegaEvent currentMegaEvent;
	private EventWeek selectedWeek;
	private List<EventWeek> weeks;

	private List<Event> seminarComplementarEvents;
	
	private Event selectedSeminarComplementarEvent;
	
	private RegisterDetail newRegisterDetail;

	@PostConstruct void init() {
		populateCurrentMegaEvent();
		populateWeeks();
		populateSelectedWeek();
		populateSeminarComplementarEvents();
	}

	private void populateNewRegisterDetail() {
		newRegisterDetail = new RegisterDetail();
		newRegisterDetail.setEvent(selectedSeminarComplementarEvent);
		newRegisterDetail.setEventWeek( selectedSeminarComplementarEvent.getEventWeek() );
	}
	

	private void populateCurrentMegaEvent() {
		currentMegaEvent = sessionHolder.getCurrentMegaEvent();
	}
	
	private void populateWeeks() {
		weeks = eventService.searchEventWeekByMegaEvent(currentMegaEvent);
	}
	
	private void populateSelectedWeek() {
		selectedWeek = eventService.findCurrentEventWeekByMegaEvent(currentMegaEvent);
		if (selectedWeek==null) {
			if (weeks.get(0)!=null) {
				selectedWeek = weeks.get(0);
			} else {
				selectedWeek = new EventWeek();
			}
		}
	}
	
	private void populateSeminarComplementarEvents() {
		seminarComplementarEvents = seminarComplementarService.searchSeminarComplementarEventByEventWeek(selectedWeek);
	}
	
	
	
//auto complete
	public List<Register> queryRegister(String fragName) {
		return seminarComplementarService.searchRegisterByMegaEventAndContactName(currentMegaEvent, fragName);
	}
	
	
//actions...
	public void search() {
		populateSeminarComplementarEvents();
	}
	
	public String manageSeminarComplementar(Event selectedEvent) {
		this.selectedSeminarComplementarEvent = selectedEvent;
		populateNewRegisterDetail();
		reloadSeminarComplementarEvent();
		return Navigation.SEMINAR_COMPLEMENTAR_EDIT;
	}
	
	/**
	 * Adiciona o Detail.
	 * (durante o autocomplete, o register � setado no detail)
	 * @param flagAsParticipant
	 */
	public void prepareToAddRegisterDetailToSeminarComplementar(boolean flagAsParticipant) {
		//verificao de negocio
		verifyIfContactAlreadyAdded();

		//seta os valores
		if (flagAsParticipant) {
			newRegisterDetail.setPresence( EventPresence.PARTICIPANT );
			newRegisterDetail.setValue( selectedSeminarComplementarEvent.getValueParticipant() );
		} else {
			newRegisterDetail.setPresence( EventPresence.STAFF );
			newRegisterDetail.setValue( selectedSeminarComplementarEvent.getValueStaff() );
		}

		//salva o novo detail
		newRegisterDetail = registerService.saveRegisterDetail(newRegisterDetail);
		
		//refresca os pagamentos e creditos
		refreshRegisterDetailWithPayments();
		refreshRegisterWithCredits();
	}
	

	private void refreshRegisterDetailWithPayments() {
		newRegisterDetail = registerService.refreshRegisterDetailWithPayments(newRegisterDetail);
	}

	private void refreshRegisterWithCredits() {
		Register register = newRegisterDetail.getRegister();
		register = registerService.refreshRegisterWithPaymentsAndCredits(register);
		newRegisterDetail.setRegister(register);
		
	}

	public void confirmAddRegisterDetailToSeminarComplementar() {
		registerService.saveRegisterDetail(newRegisterDetail);
		populateNewRegisterDetail();
		reloadSeminarComplementarEvent();
		JSFUtil.addInfoMessage("msg_register_add_to_seminar_complementar_sucess");
	}
	
	
	/**
	 * Verifica se o contacto selecionado ja foi adicionado
	 * ao seminario complementar. Isso evita duplicacoes.
	 */
	private void verifyIfContactAlreadyAdded() {
		Contact selectedContact = newRegisterDetail.getRegister().getContact();
		
		for (RegisterDetail detail : selectedSeminarComplementarEvent.getDetails()) {
			if (detail.getRegister().getContact().equals( selectedContact )) {
				throw new ContactAlreadyAddedToSeminarComplementarException(selectedContact);
			}
		}
	}

	public void removeRegisterDetailFromSerminarComplementar(RegisterDetail selectedDetail) {
		registerService.removeRegisterDetail(selectedDetail);
		reloadSeminarComplementarEvent();
		JSFUtil.addInfoMessage("msg_register_remove_from_seminar_complementar_sucess");
	}
	
	
//util
	private void reloadSeminarComplementarEvent() {
		selectedSeminarComplementarEvent = seminarComplementarService.reloadSeminarComplementarEvent(selectedSeminarComplementarEvent);
	}
	
	
	
	
	
	
	
	
//acessores...	
	private static final long serialVersionUID = 1225092810000393385L;

	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

	public EventWeek getSelectedWeek() {
		return selectedWeek;
	}
	public void setSelectedWeek(EventWeek selectedWeek) {
		this.selectedWeek = selectedWeek;
	}
	public MegaEvent getCurrentMegaEvent() {
		return currentMegaEvent;
	}
	public List<EventWeek> getWeeks() {
		return weeks;
	}
	public List<Event> getSeminarComplementarEvents() {
		return seminarComplementarEvents;
	}

	public Event getSelectedSeminarComplementarEvent() {
		return selectedSeminarComplementarEvent;
	}

	public void setSelectedSeminarComplementarEvent(
			Event selectedSeminarComplementarEvent) {
		this.selectedSeminarComplementarEvent = selectedSeminarComplementarEvent;
	}

	public RegisterDetail getNewRegisterDetail() {
		return newRegisterDetail;
	}

	public void setNewRegisterDetail(RegisterDetail newRegisterDetail) {
		this.newRegisterDetail = newRegisterDetail;
	}
	
	
}

package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.security.LoginBean;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.enumeration.DocumentType;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.exception.RegisterBadConfiguratedException;
import com.cb.mundo.model.service.ContactService;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.MedicalFormService;
import com.cb.mundo.model.service.RegisterService;

/**
 * Managed Bean caso de uso onde produtores e administracao 
 * podem fazer inscricoes de terceiros.
 * 
 * @author Solkam
 * @since 07 JAN 2013
 */
@ManagedBean(name="myRegisteredOnesBean")
@ViewScoped
public class MyRegisteredOnesBean implements Serializable {

	@EJB EventService eventService;
	@EJB RegisterService registerService;
	@EJB ContactService contactService;
	@EJB MedicalFormService medicalFormService;
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;
	
	//combos...
	private List<MegaEvent> comboMegaEvents;
	private List<EventWeek> comboEventWeeks;
	private List<Event> comboEvents;
	private List<EventPresence> compatiblePresences;
	private List<DocumentType> comboDocumentTypes;
	
	//filters...
	private MegaEvent selectedMegaEvent;
	private EventWeek selectedEventWeek;
	private Event selectedEvent;
	private List<EventPresence> selectedPresences = new ArrayList<>();
	
    //result...
	private List<RegisterDetail> registerDetails;
	
	
    //nova inscricao
	private Register register;
	
	
	@PostConstruct void init() {
		populateComboMegaEvents();
		populateSelectedMegaEventFromHolder();
		
		populateComboEventWeeks();
		populateSelectedEventWeek();
		
		populateComboEvents();
		populateSelectedEvent();
		
		populateCompatiblePresences();
		populateSelectedPresences();
		
		populateComboDocumentTypes();
	}
	
	
	private void populateComboDocumentTypes() {
		comboDocumentTypes = new ArrayList<DocumentType>();
		comboDocumentTypes.add( DocumentType.CPF );
		comboDocumentTypes.add( DocumentType.PASSPORT );
	}


	//populates...	
	private void populateSelectedMegaEventFromHolder() {
		selectedMegaEvent = sessionHolder.getCurrentMegaEvent();
	}

	private void populateComboMegaEvents() {
		comboMegaEvents = eventService.searchActiveMegaEvent();
	}
	
	private void populateComboEventWeeks() {
		if (selectedMegaEvent!=null && selectedMegaEvent.getId()!=null) {
			comboEventWeeks = eventService.searchEventWeekByMegaEvent(selectedMegaEvent);
		}
	}
	
	private void populateSelectedEventWeek() {
		if (comboEventWeeks!=null && !comboEventWeeks.isEmpty()) {
			selectedEventWeek = comboEventWeeks.get(0);
		}
	}

	private void populateComboEvents() {
		if (selectedEventWeek!=null && selectedEventWeek.getId()!=null) {
			comboEvents = eventService.searchEventByEventWeek(selectedEventWeek);
		} else {
			comboEvents = new ArrayList<Event>();
		}
	}
	
	private void populateSelectedEvent() {
		if (comboEvents!=null && !comboEvents.isEmpty()) {
			selectedEvent = comboEvents.get(0);
		}
	}
	
	private void populateSelectedPresences() {
		selectedPresences.clear();
		for (EventPresence presence : compatiblePresences) {
			selectedPresences.add(presence);
		}
	}
	
	//listeners...
	public void onMegaEventChange() {
		populateComboEventWeeks();
		populateSelectedEventWeek();
		populateComboEvents();
		selectedEvent = new Event();
	}
	
	public void onEventWeekChange() {
		populateComboEvents();
		selectedEvent = new Event();
	}
	
	
	/**
	 * Verifica o evento selecionado e suas configuracoes internas
	 * para definir qual a lista de presence compativel.
	 */
	private void populateCompatiblePresences() {
		compatiblePresences = new ArrayList<EventPresence>();

		selectedEvent = eventService.findEventById(selectedEvent.getId());
		//evento habilitado para participant
		if (selectedEvent.getEnableForParticipant()) {
			compatiblePresences.add( EventPresence.PARTICIPANT );
		}

		//evento habilitado para Staff
		if (selectedEvent.getEnableForStaff()) {
			compatiblePresences.add( EventPresence.STAFF );
		}
		
		//evento mal-configurado
		if (compatiblePresences.isEmpty()) {
			JSFUtil.addErrorMessage("msg_event_not_configured_properly", null);
			return;
		}
	}
	
	
	//actions...
	
	/**
	 * Pesquisa tudo (se for super usuario) ou restringe pelo produtor
	 */
	private void privateSearch() {
		//TODO solucao temporaria para o Estrela do Mar 2014
		registerDetails = registerService.searchRegisterDetailByMegaEventAndEventWeekAndEvent(
				selectedMegaEvent, 
				selectedEventWeek, 
				selectedEvent,
				selectedPresences);
		
		//RN solicitada pela Admin CB
//		if (loginBean.getAuthenticatedUser().isSuperUser()) {
//			registerDetails = eventService.searchRegisterDetailByMegaEventAndEventWeekAndEvent(
//					selectedMegaEvent, 
//					selectedEventWeek, 
//					selectedEvent,
//					selectedPresences);
//		} else {
//			registerDetails = eventService.searchRegisterDetailByMegaEventAndEventWeekAndEventAndProductor(
//					selectedMegaEvent, 
//					selectedEventWeek, 
//					selectedEvent,
//					selectedPresences,
//					loginBean.getAuthenticatedUser().getContact());
//		}
	}
	
	public void resetRegister() {
		register = new Register();
		register.setContact( new Contact() );
		register.setMegaEvent( selectedMegaEvent );
	}
	
	public void search() {
		privateSearch();
		JSFUtil.addMessageAboutResult(registerDetails);
	}
	
	
	public void manageRegister(Register selectRegister) {
		this.register = selectRegister;
		refreshRegisterFully();
	}


	
	/* ******************
	 * Register e Contact	
	 ********************/
	public void saveRegister() {
			//contact
			Contact contact = contactService.saveContact(register.getContact(), loginBean.getAuthenticatedUser() );
			register.setContact(contact);
			
			//register
			register = findOrCreateRegister(selectedMegaEvent, contact); 
			
			//renova a pesquisa
			privateSearch();
			
			//add msg
			JSFUtil.addInfoMessage("msg_registeredone_save_sucess", null);
			
			//refresh register
			refreshRegisterFully();
	}
	
	
	private Register findOrCreateRegister(MegaEvent me, Contact c) {
		List<Register> registers = eventService.searchRegistersByMegaEventAndContact(me, c);
		//se existir, estah sendo editada
		if (registers.size()==1) {
			return registers.get(0);
		}
		 
		//se nao existir, salvar um novo
		if (registers.size()==0) {
			Register newRegister = new Register();
			newRegister.setContact( c );
			newRegister.setMegaEvent( me );
			newRegister = registerService.saveRegister( newRegister );
			return newRegister;
		}

		//se tiver mais de uma, eh pq ja existem varias duplicadas e temos um problema 
		throw new RegisterBadConfiguratedException();
	}


	public void removeRegister() {
		registerService.removeRegister(register);
		privateSearch();
		JSFUtil.addInfoMessage("msg_registeredone_remove_sucess", null);
	}
	
	
	/* ******
	 * Events	
	 ********/
	public void addEventAsParticipant() {
		RegisterDetail registerDetail = new RegisterDetail();
		registerDetail.setRegister(register);
		registerDetail.setEventWeek( selectedEventWeek );
		registerDetail.setEvent( selectedEvent );
		registerDetail.setPresence( EventPresence.PARTICIPANT );
		registerDetail.setValue( selectedEvent.getValueParticipant() );
		
		registerDetail = registerService.saveRegisterDetail(registerDetail);
		
		privateSearch();
		
		refreshRegisterFully();

		JSFUtil.addInfoMessage("msg_event_add_sucess");
	}
	
	public void addEventAsStaff() {
		RegisterDetail registerDetail = new RegisterDetail();
		registerDetail.setRegister(register);
		registerDetail.setEventWeek( selectedEventWeek );
		registerDetail.setEvent( selectedEvent );
		registerDetail.setPresence( EventPresence.STAFF );
		registerDetail.setValue( selectedEvent.getValueStaff() );
		
		registerDetail = registerService.saveRegisterDetail(registerDetail);
		
		privateSearch();

		refreshRegisterFully();
		
		JSFUtil.addInfoMessage("msg_event_add_sucess");
	}
	
	
	public void removeEvent(RegisterDetail selectedDetail) {
		registerService.removeRegisterDetail( selectedDetail );
		
		privateSearch();
		
		refreshRegisterFully();
		
		JSFUtil.addInfoMessage("msg_event_remove_sucess");
	}
	
	
	
	/* **********
	 * Produtores	
	 ************/
	public void saveProductor() {
		//salva o contact e reatribui ao register
		Contact savedContact = contactService.saveContact( register.getContact(), loginBean.getAuthenticatedUser() );
		register.setContact( savedContact );
		
		//renova resultados
		privateSearch();
		
		//msg
		JSFUtil.addInfoMessage("msg_productor_save_sucess");
	}
	

	//util	
	private void refreshRegisterFully() {
		register = registerService.refreshRegisterWithPaymentsAndCredits( register );
	}
	
	
	
	//acessores...
	private static final long serialVersionUID = 9079740876208057184L;
	public List<MegaEvent> getComboMegaEvents() {
		return comboMegaEvents;
	}
	public List<EventWeek> getComboEventWeeks() {
		return comboEventWeeks;
	}
	public List<Event> getComboEvents() {
		return comboEvents;
	}


	public MegaEvent getSelectedMegaEvent() {
		return selectedMegaEvent;
	}
	public void setSelectedMegaEvent(MegaEvent selectedMegaEvent) {
		this.selectedMegaEvent = selectedMegaEvent;
	}

	public EventWeek getSelectedEventWeek() {
		return selectedEventWeek;
	}
	public void setSelectedEventWeek(EventWeek selectedEventWeek) {
		this.selectedEventWeek = selectedEventWeek;
	}
	
	public Event getSelectedEvent() {
		return selectedEvent;
	}
	public void setSelectedEvent(Event selectedEvent) {
		this.selectedEvent = selectedEvent;
	}
	
	public Register getRegister() {
		return register;
	}
	public void setRegister(Register register) {
		this.register = register;
	}
	public List<RegisterDetail> getRegisterDetails() {
		return registerDetails;
	}
	public List<EventPresence> getCompatiblePresences() {
		return compatiblePresences;
	}
	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
	public List<EventPresence> getSelectedPresences() {
		return selectedPresences;
	}
	public void setSelectedPresences(List<EventPresence> selectedPresences) {
		this.selectedPresences = selectedPresences;
	}
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	public List<DocumentType> getComboDocumentTypes() {
		return comboDocumentTypes;
	}
	
}

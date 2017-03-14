package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.cb.estreladomar.controller.builder.RegisterConfirmationEmailBuilder;
import com.cb.estreladomar.controller.security.LoginBean;
import com.cb.estreladomar.controller.transbank.TransbankPaymentHelper;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.estreladomar.controller.util.Navigation;
import com.cb.mundo.model.builder.SimpleEmailBuilder;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.Hosting;
import com.cb.mundo.model.entity.HostingSugested;
import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.RoomType;
import com.cb.mundo.model.entity.TransbankPayment;
import com.cb.mundo.model.entity.enumeration.DocumentType;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.service.ContactService;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.HostingService;
import com.cb.mundo.model.service.MedicalFormService;
import com.cb.mundo.model.service.RegisterService;
import com.cb.mundo.model.service.RoomTypeService;
import com.cb.mundo.model.service.TransbankPaymentService;
import com.cb.mundo.model.service.email.EmailService;
import com.cb.mundo.model.util.CalendarUtil;

/**
 * Managed Bean para gerenciar os passos da inscricao
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="registerStepBean")
@SessionScoped
public class RegisterStepBean implements Serializable {

	@EJB EventService eventService;
	
	@EJB RegisterService registerService;
	
	@EJB MedicalFormService medicalFormService;
	
	@EJB RoomTypeService roomTypeService;
	
	@EJB ContactService contactService;
	
	@EJB HostingService hostingService;
	
	@EJB TransbankPaymentService transbankPaymentService;

	@EJB EmailService emailService;
	
	
	
	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;
	

	//main object	
	private Register register;
	
	
	
	//lists
	private List<MedicalAnswer> medicalAnswers;
	private List<EventWeek> availableEventWeeks;
	

	@PostConstruct void init() {
		populateComboRoomTypes();
	}
	
	


	//populates...	
	/**
	 * Seleciona somente as semanas e evento que ainda nao inscritos.
	 */
	private void populateAvailableEventWeeks() {
		this.availableEventWeeks = eventService.searchEventWeekAndAvailableEventsByRegister(register);
	}

	/**
	 * Este metodo pesquisa por todas as questoes medicais e 
	 * para cada um, cria um resposta atribuindo a inscricao.
	 */
	private void populateMedicalAnswer() {
		medicalAnswers = medicalFormService.searchOrCreateMedicalAnswerByRegister(register);
	}
	
	
	
	
	//metodos de preparo para inscricao...
	
	/**
	 * Na tela inicial de inscricoes, ao clicar num inscricao para Revisar, este
	 * metodo eh invocado. 
	 * Neste caso, o objeto register eh detached e tem o mega evento interno setado.
	 * Faz um refresh do register para totalizar valores internos...
	 * Alem disso, popula as semana disponiveis
	 * @param selectedRegister
	 * @return navegacao para o primeiro passo da inscricao
	 */
	public String prepareToUpdateRegister(Register selectedRegister) {
		this.register = selectedRegister;
		
		refreshRegister();
		
		populateAvailableEventWeeks();
		populateMedicalAnswer();
		populateOptHosting();
		
		return Navigation.REGISTER_STEP_EVENT;
	}
	
	/**
	 * Na tela inicial de inscricoes, ao clicar num mega evento disponivel,
	 * este metodo eh invocado.
	 * Entao, cria-se uma instancia de register, set o mega evento selecionado,
	 * seta o contato do usuario logado e o salva.
	 * @param megaEvent
	 * @return navegacao para o primeiro passo da inscricao
	 */
	public String prepareToNewRegister(MegaEvent megaEvent) {
		Contact contact = loginBean.getAuthenticatedUser().getContact();
		//		this.register.setContact( contact );
	
		this.register = registerService.createInitialRegister(megaEvent, contact);

		populateAvailableEventWeeks();
		populateMedicalAnswer();
		populateOptHosting();
		
		return Navigation.REGISTER_STEP_EVENT;
	}	
	
	/**
	 * Na tela inicial de inscricoes, este metodo sera invocado
	 * ao clicar em Visualizar Pagamentos.
	 * Este metodo vai refrescar o register com pagamentos e creditos.
	 * No dialogo que abrira, estara listado todos os pagamentos e 
	 * um link para realizar novo pagamento junto a transbank.
	 * @param seletectedRegister
	 */
	public void prepareToViewPayments(Register seletectedRegister) {
		this.register = registerService.refreshRegisterWithPaymentsAndCredits(seletectedRegister);
	}
	
	/**
	 * View Action para resposta sobre falha na Transbank:
	 * - grava os parametros de resposta da transbank
	 */
	public void processFailTransbankResponse() {
		TransbankPaymentHelper helper = new TransbankPaymentHelper(transbankPaymentService);
		helper.saveFailPayment( FacesContext.getCurrentInstance() );
	}

	/**
	 * View Action para resposta sobre sucesso na Transbank:
	 * - grava os parametros de resposta da transbank
	 * - grava um pagamento para a inscricao correspondente;
	 */
	public void processSucessTransbankResponse() {
		TransbankPaymentHelper helper = new TransbankPaymentHelper(transbankPaymentService);
		TransbankPayment sucessPayment = helper.saveSucessPayment( FacesContext.getCurrentInstance() );
		
		transbankPaymentService.prorateTransbankPayment(sucessPayment);
	}
	
	
	
	
	
	//actions...
	
	public void confirmAsParticipant(EventWeek week, Event event) {
		RegisterDetail registerDetailAsParticipant = new RegisterDetail();
		registerDetailAsParticipant.setPresence( EventPresence.PARTICIPANT );
		registerDetailAsParticipant.setEventWeek( event.getEventWeek() );
		registerDetailAsParticipant.setEvent( event );
		registerDetailAsParticipant.setRegister( this.register );
		registerDetailAsParticipant.setValue( event.getValueParticipant() );
		
		registerDetailAsParticipant = registerService.saveRegisterDetail( registerDetailAsParticipant );
		refreshRegister();
		
		populateAvailableEventWeeks();
	}
	
	public void confirmAsStaff(EventWeek week, Event event) {
		RegisterDetail registerDetailAsStaff = new RegisterDetail();
		registerDetailAsStaff.setPresence( EventPresence.STAFF );
		registerDetailAsStaff.setEventWeek( event.getEventWeek() );
		registerDetailAsStaff.setEvent( event );
		registerDetailAsStaff.setRegister( this.register );
		registerDetailAsStaff.setValue( event.getValueStaff() );
		
		registerDetailAsStaff = registerService.saveRegisterDetail( registerDetailAsStaff );
		refreshRegister();
		
		populateAvailableEventWeeks();
	}
	
	public void removeRegisterDetail(RegisterDetail rd) {
		try {
			registerService.removeRegisterDetail( rd );
			refreshRegister();
			
			populateAvailableEventWeeks();
			
		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}

	
	public String confirmRegister() {
		try {
			//1.save register
			register.setStatus( RegisterStatus.REGISTERED );
			register = registerService.saveRegister( register );
			
			//2.save all medical aswers
			medicalFormService.saveMedicalAnswers(medicalAnswers);
			
			//3.save hosting (opt)
			saveOptHosting();
			
			//4.refresh
			refreshRegisterWithPayments();

			//5.email de confirmacao
			sendConfirmation();
			

			//6.msg
			JSFUtil.addInfoMessage("msg_register_confirm_sucess", null );
			
			//7.redirect to sucess
			return Navigation.REGISTER_STEP_SUCESS;
			
		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
			return Navigation.REGISTER_STEP_CONFIRM;
		}
	}
	
	
	/*
	 * Dados pessois
	 ***************/
	
	private Boolean flagBrasilian = true;
	
	
	private void savePersonalData() {
		Contact contact = register.getContact();

		if (flagBrasilian) {
			contact.setIdentityDocumentType(DocumentType.CPF);
		}
		
		contact = contactService.saveContact(contact, loginBean.getAuthenticatedUser() );
		register.setContact(contact);
	}
	
	private void sendConfirmation() {
		try {
			SimpleEmailBuilder builder = new RegisterConfirmationEmailBuilder(register);
			
			emailService.sendEmail(builder);
			
			JSFUtil.addInfoMessage("msg_email_sent_for_confirmation");
			
		} catch (Exception e) {
			JSFUtil.addWarnMessage("msg_email_sending_failed");
		}
	}
	
	
	/*
	 * Gestao de Dependente
	 **********************/
	
	private List<Event> dependentEvents;
	private List<Event> seletedDependentEvents;
	
	private Register familiarRegister;
	
	public void popularFamiliarRegister() {
		familiarRegister = new Register();
		familiarRegister.setContact( new Contact() );
		
		dependentEvents = eventService.searchDependentEventByMegaEvent( register.getMegaEvent() );
		seletedDependentEvents = new ArrayList<>();
	}
	
	
	public void saveFamiliarRegister() {
		try {
			//1.salva contact familiar
			Contact familyCon = familiarRegister.getContact();
			if (familyCon.getId()==null) {//se for transiente
				familyCon.setAddress(    register.getContact().getAddress() );
				familyCon.setCity(       register.getContact().getCity() );
				familyCon.setCountry(    register.getContact().getCountry() );
				familyCon.setState(      register.getContact().getState() );
				familyCon.setTelephone1( register.getContact().getTelephone1() );
				familyCon.setZipPostal(  register.getContact().getZipPostal() );
			}
			familyCon = contactService.saveContact( familyCon, loginBean.getAuthenticatedUser() );
			
			//2.salva a inscricao do familiar ou baba
			familiarRegister.setContact( familyCon );
			familiarRegister.setMegaEvent( register.getMegaEvent() );
			familiarRegister.setResponsableRegister( register );
			
			familiarRegister = registerService.saveRegister( familiarRegister );

			//2.salva os details da inscricao (um para cada semana)
			for (Event dependentEvent : seletedDependentEvents) {
				
				RegisterDetail familiarRegisterDetail = new RegisterDetail();
				familiarRegisterDetail.setEvent( dependentEvent );
				familiarRegisterDetail.setEventWeek( dependentEvent.getEventWeek() );
				familiarRegisterDetail.setRegister( familiarRegister );
				familiarRegisterDetail.setValue( dependentEvent.getValueParticipant() );
				familiarRegisterDetail.setPresence( EventPresence.DEPENDENT );
				
				registerService.saveRegisterDetail( familiarRegisterDetail );
			}
			
			//4.msg sucesso
			JSFUtil.addInfoMessage("msg_familiar_register_detail_save_sucess", null);
			
			//5.reset
			popularFamiliarRegister();
			
			//6.refresh register
			refreshRegister();
			
		} catch (Exception e) {
			JSFUtil.addErrorMessage( e );
		}
	}
	
	
	/**
	 * Recarregar register com todos os detalhes e hosting...
	 */
	private void refreshRegister() {
		register = registerService.refreshRegisterWithDetailsAndHosting(register); 
	}
	
	/**
	 * Recarregar register com os pagamentos...
	 */
	private void refreshRegisterWithPayments() {
		register = registerService.refreshRegisterWithPaymentsAndCredits(register); 
		refreshOptHosting();
	}
	
	

	//navigation...
	
	public String onNextFromStepEvent() {
		return Navigation.REGISTER_STEP_PERSONAL;
	}
	public String onNextFromStepPersonal() {
		savePersonalData();
		return Navigation.REGISTER_STEP_PRODUCTOR;
	}
	public String onNextFromStepProductor() {
		savePersonalData();
		return Navigation.REGISTER_STEP_MEDICAL;
	}
	public String onNextFromStepMedical() {
		return Navigation.REGISTER_STEP_TRIP;
	}
	public String onNextFromStepTrip() {
		//		saveTripInfo();
		return Navigation.REGISTER_STEP_CONDITION;
	}
	public String onNextFromStepCondition() {
		return Navigation.REGISTER_STEP_CONFIRM;
	}
	public String onNextFromStepConfirm() {
		return Navigation.REGISTER_STEP_SUCESS;
	}

	
	public String onPrevFromStepPersonal() {
		return Navigation.REGISTER_STEP_EVENT;
	}
	public String onPrevFromStepProductor() {
		return Navigation.REGISTER_STEP_PERSONAL;
	}
	public String onPrevFromStepMedical() {
		return Navigation.REGISTER_STEP_PRODUCTOR;
	}
	public String onPrevFromStepTrip() {
		return Navigation.REGISTER_STEP_MEDICAL;
	}
	public String onPrevFromStepCondition() {
		return Navigation.REGISTER_STEP_TRIP;
	}
	public String onPrevFromStepConfirm() {
		return Navigation.REGISTER_STEP_CONDITION;
	}
	
	

	/* *******************
	 * Hosting e Sugeridos
	 *********************/
	//dados de hospedagem sao opcionais 
	private Hosting optHosting;
	private HostingSugested optHostingSugested;
	
	private List<RoomType> comboRoomType;
	

	private void populateComboRoomTypes() {
		comboRoomType = roomTypeService.searchActiveRoomType();
	}
	
	
	private void populateOptHosting() {
		optHosting = hostingService.findHostingBySugestedContact( register.getContact(), true );
		if (optHosting==null) {
			optHosting = new Hosting();
			optHosting.setSugesteds( new ArrayList<HostingSugested>() );
		}
		resetOptHostingSugested();
	}

	private void resetOptHostingSugested() {
		optHostingSugested = new HostingSugested();
	}
	
	
	private void refreshOptHosting() {//somente recarrega se estiver valido
		if (optHosting!=null && !optHosting.isTransient()) {
			optHosting = hostingService.refreshHosting(optHosting);
		}
	}
	
	
	public void onRoomTypeChange() {
		//se tipo do quarto tiver sido selecionado e nao tiver sugeridos...
		if (optHosting.getRoomType()!=null && optHosting.getSuggestedsActual()==0) {
			//...cria um sugerido inicial (o praprio inscrito)
			optHosting.buildHostingSugessted( register.getContact() );
		}
	}
	
	
	
	public void addOptHostingSugested() {
		//1.referia o hosting
		optHostingSugested.setHosting( optHosting );
		//2.adiciona ao hosting
		optHosting.addHostingSugested( optHostingSugested );
		//3.reseta o sugerido
		resetOptHostingSugested();
	}
	
	
	public void removeOptHostingSugested(HostingSugested selectedOne) {
		//1.remove da lista de sugeridos
		optHosting.removeHostingSugested( selectedOne );
		//2.reseta o sugerido
		resetOptHostingSugested();
	}
	
	
	/**
	 * Se usuario selecionou tipo de quarto, entao salva.
	 * Por cascata, salva as sugestoes de colegas de quarto.
	 * (invocado na hora de confirmar a inscricao)
	 */
	public void saveOptHosting() {
		if (optHosting.getRoomType()!=null) {
			optHosting.setMegaEvent( register.getMegaEvent() );
			optHosting = hostingService.saveHosting( optHosting, loginBean.getAuthenticatedUser() );
		} else {//deleta se tiver
			hostingService.removeHosting( optHosting );
			optHosting = null;
		}
	}
	
	
	/**
	 * Metodo que retorno um session id artificial 
	 * baseado no timestamp
	 * @return
	 */
	public Long getTransbankSessionId() {
		return CalendarUtil.getTimestampAsLong();
	}

	
	
	//acessores...
	private static final long serialVersionUID = -5892048229664436136L;
	
	public List<MedicalAnswer> getMedicalAnswers() {
		return medicalAnswers;
	}
	public Hosting getOptHosting() {
		return optHosting;
	}
	public void setOptHosting(Hosting optHosting) {
		this.optHosting = optHosting;
	}
	public HostingSugested getOptHostingSugested() {
		return optHostingSugested;
	}
	public void setOptHostingSugested(HostingSugested optHostingSugested) {
		this.optHostingSugested = optHostingSugested;
	}
	public Register getRegister() {
		return register;
	}
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	public List<EventWeek> getAvailableEventWeeks() {
		return availableEventWeeks;
	}
	public List<Event> getSeletedDependentEvents() {
		return seletedDependentEvents;
	}
	public void setSeletedDependentEvents(List<Event> seletedDependentEvents) {
		this.seletedDependentEvents = seletedDependentEvents;
	}
	public List<Event> getDependentEvents() {
		return dependentEvents;
	}
	public Register getFamiliarRegister() {
		return familiarRegister;
	}
	public void setFamiliarRegister(Register familiarRegister) {
		this.familiarRegister = familiarRegister;
	}
	public Boolean getFlagBrasilian() {
		return flagBrasilian;
	}
	public void setFlagBrasilian(Boolean flagBrasilian) {
		this.flagBrasilian = flagBrasilian;
	}
	public List<RoomType> getComboRoomType() {
		return comboRoomType;
	}
	
}

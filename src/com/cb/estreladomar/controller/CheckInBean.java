package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.FlowEvent;

import com.cb.estreladomar.controller.builder.CheckinConfirmationEmailBuilder;
import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.security.LoginBean;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.estreladomar.controller.util.Navigation;
import com.cb.mundo.model.builder.SimpleEmailBuilder;
import com.cb.mundo.model.entity.CheckIn;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.enumeration.DocumentType;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.service.ContactService;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.MedicalFormService;
import com.cb.mundo.model.service.RegisterService;
import com.cb.mundo.model.service.email.EmailService;

/**
 * Managed Bean para realizacao de check-in
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="checkInBean")
@SessionScoped
public class CheckInBean implements Serializable {

	@EJB EventService eventService;
	@EJB RegisterService registerService;
	@EJB ContactService contactService;
	@EJB MedicalFormService medicalFormService;
	@EJB EmailService emailService;
	
	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	/**
	 * No processo de check-in, geralmente a data de check=in eh a data corrente.
	 * No entanto eh possivel manualmente alterar esta para check-ins retroativos ou futuros.
	 */
	private Date manualCheckinDate = new Date();
	
	/**
	 * A data de check-out eh calculada conforme as semanas inscritas.
	 * No processo de check-in, estah data pode ser alterada manualmente
	 */
	private Date manualCheckoutDate;

	
	//selected objects	
	private Register selectedRegister;
	
	
	
	//searching objects	
	private String keyword;
	private List<Register> registers = new ArrayList<Register>();
	
	private List<MedicalAnswer> medicalAnswers;
	
	
	/**
	 * flag para forcar que o CPF seja 
	 * preenchido por brasileiros.
	 */
	private Boolean flagBrazilian = true;
	
	
	
	//inits...	
	@PostConstruct void init() {
		populateDependentEvents();
	}
	
	private void populateMedicalAnswers() {
		medicalAnswers = medicalFormService.searchOrCreateMedicalAnswerByRegister(selectedRegister);
	}
	
	private void populateDependentEvents() {
		MegaEvent currentMegaEvent = sessionHolder.getCurrentMegaEvent();
		dependentEvents = eventService.searchDependentEventByMegaEvent( currentMegaEvent );
	}
	
	
	
	protected void refreshRegister() {
		this.selectedRegister = registerService.refreshRegisterWithPaymentsAndCredits( this.selectedRegister );
	}
	
	private void refreshRegister(Register register) {
		this.selectedRegister = registerService.refreshRegisterWithPaymentsAndCredits( register );
	}
	
	//listener...
	public String handleFlow(FlowEvent event) {
		String oldStepId = event.getOldStep();
		
		if ("tabEvents".equals(oldStepId)) {
			defineManualCheckDates();
		}
		
		//as tabs personal data e productorETreinador tem a mesma logica (salvar contato)
		if ("tabPersonalData".equals(oldStepId) || "tabProductorAndTrainner".equals(oldStepId)) {
			try {
				//salva contato pode lancar algumas exceptions...
				savePersonalData();
				
			} catch (Exception e) {
				JSFUtil.addErrorMessage(e);//...se acontecer, cria msg e fica no mesmo step
				return oldStepId;
			}
		}
		
		if ("tabTripInfo".equals(oldStepId)) {
			if (!validateCheckDates()) {
				return oldStepId;
				
			} else {//se data estao validadas...
				preconfigureCheckin();
			}
		}
		
		return event.getNewStep();
	}
	
	private void defineManualCheckDates() {
		manualCheckinDate  = selectedRegister.getCalculatedCheckinDate();
		manualCheckoutDate = selectedRegister.getCalculatedCheckoutDate();
	}

	//actions...
	/**
	 * Busca registers pela mega evento atual, status REGISTERED e INCOMPLETE e palavras chaves
	 */
	public void searchByKeywork() {
		MegaEvent currentMegaEvent = sessionHolder.getCurrentMegaEvent();
		
		//lista de status aptos para checkin
		List<RegisterStatus> listaOfStatus = new ArrayList<RegisterStatus>();
		listaOfStatus.add( RegisterStatus.REGISTERED );
		listaOfStatus.add( RegisterStatus.INCOMPLETE );
//		listaOfStatus.add( RegisterStatus.CHECKEDIN_VIRTUAL );
		registers = registerService.searchRegistersByMegaEventAndStatusAndKeywords(currentMegaEvent, listaOfStatus, keyword);
	}
	


	
	/**
	 * Apos pesquisar por inscricao, o ato de selecionar invoca este metodo aqui
	 * @param register
	 * @return string de navegacao para revisao de check-in
	 */
	public String selectRegister(Register register) {
		//1. refresca os pagamentos da inscricao
		refreshRegister(register);

		//2.medical answers
		populateMedicalAnswers();
		
		return Navigation.CHECKIN_PARTICIPANT_REVIEW;
	}
	
	/*
	 * Gestao de Dependentes 
	 * (filhos ou babas)	
	 ***********************/
	
	private Register familiarRegister;
	private RegisterDetail familiarRegisterDetail;
	
	private List<Event> dependentEvents;
	private List<Event> seletedDependentEvents = new ArrayList<>();
	
	
	public void popularFamiliarRegister() {
		familiarRegister = new Register();
		familiarRegister.setContact( new Contact() );
		
		seletedDependentEvents = new ArrayList<>();
	}
	
	public void saveFamiliarRegister() {
		//1.salva contact familiar
		Contact familyCon = familiarRegister.getContact();
		if (familyCon.getId()==null) {//se for transiente
			familyCon.setAddress(    selectedRegister.getContact().getAddress() );
			familyCon.setCity(       selectedRegister.getContact().getCity() );
			familyCon.setCountry(    selectedRegister.getContact().getCountry() );
			familyCon.setState(      selectedRegister.getContact().getState() );
			familyCon.setTelephone1( selectedRegister.getContact().getTelephone1() );
			familyCon.setZipPostal(  selectedRegister.getContact().getZipPostal() );
		}
		familyCon = contactService.saveContact( familyCon, null );
		
		//2.salva a inscricao do familiar ou babe
		familiarRegister.setContact( familyCon );
		familiarRegister.setMegaEvent(selectedRegister.getMegaEvent() );
		familiarRegister.setResponsableRegister( selectedRegister );
		
		familiarRegister = registerService.saveRegister(familiarRegister);

		//2.salva os details da inscricao (um para cada semana)
		for (Event dependentEvent : seletedDependentEvents) {
			familiarRegisterDetail = new RegisterDetail();
			familiarRegisterDetail.setEvent( dependentEvent );
			familiarRegisterDetail.setEventWeek( dependentEvent.getEventWeek() );
			familiarRegisterDetail.setRegister(familiarRegister);
			familiarRegisterDetail.setValue( dependentEvent.getValueParticipant() );
			familiarRegisterDetail.setPresence( EventPresence.DEPENDENT );
			
			registerService.saveRegisterDetail( familiarRegisterDetail );
		}
		
		familiarRegister = registerService.refreshRegisterWithPaymentsAndCredits(familiarRegister);
		
		//3.recarrega register
		refreshRegister();
		
		//4.msg sucesso
		JSFUtil.addInfoMessage("msg_familiar_register_detail_save_sucess", null);
		
		//5.reset
		popularFamiliarRegister();
	}
	

	/*
	 * Agregar outras inscricoes
	 ***************************/
	
	private Register thirdRegister;
	
	public void populateThirdRegister() {
		thirdRegister = new Register();
		thirdRegister.setContact( new Contact() );
	}
	
	public List<Register> completeRegisterByContactName(String name) {
		MegaEvent actualMegaEvent = sessionHolder.getCurrentMegaEvent();
		List<Register> registers = registerService.searchRegistersByMegaEventAndKeyword(actualMegaEvent, name);
		return registers;
	}
	
	public void confirmThirdRegister() {
		if (validateThirdRegister()) {
			//1.completa o objeto pois so o ID esta setado
			thirdRegister = registerService.refreshRegisterWithPaymentsAndCredits(thirdRegister);
			
			//2.register passa sob a responsabilidade
			thirdRegister.setResponsableRegister( selectedRegister );
			
			//3.salva a inscricao dependente
			thirdRegister = registerService.saveRegister( thirdRegister );
			
			//4.refresca
			refreshRegister();
			
			//5.reseta a inscricao de terceiro
			populateThirdRegister();

			//6.msg sucesso
			JSFUtil.addInfoMessage("msg_third_register_agregate_sucess");
		}
		
	}
	
	private boolean validateThirdRegister() {
		if (thirdRegister==null) {
			JSFUtil.addErrorMessage("msg_no_contact_selected", null);
			return false;
		}
		return true;
	}
	
	
	
	
	/*
	 * novas inscricoes no check-in	
	 ******************************/
	
	private String emailForNewRegister;
	
	/**
	 * Action para Novas inscricoes onde se busca o contact por email.
	 * @return navegacao para revisao (novo) de check-in
	 */
	public String searchByEmailForNewRegister() {
		Register newRegister = null;
		MegaEvent currentMegaEvent = sessionHolder.getCurrentMegaEvent();

		Contact contact = contactService.findContactByEmail(emailForNewRegister);

		// 1) Se o email ja estiver no sistema... 
		if (contact!=null) {
			
			//...verifica se existe uma inscricao para o email
			newRegister = registerService.findRegisterByMegaEventAndEmailAndStatus(currentMegaEvent, emailForNewRegister);
			
			//1.1) se existir inscricao..
			if (newRegister!=null) {
				
				//1.2) Se a inscricao estiver no status CHECKEDIN ou CHECKEDOUT, add error e continua na busca por email
				if ( RegisterStatus.CHECKEDIN.equals(newRegister.getStatus()) 
				  || RegisterStatus.CHECKEDOUT.equals(newRegister.getStatus()) ) {
					
					JSFUtil.addErrorMessage("msg_new_register_email_already_checkedin", new Object[] {emailForNewRegister});
					return Navigation.CHECKIN_INDEX;
				
					//1.3) Esta pronto para a revisao de checkin...
				} else {
					JSFUtil.addInfoMessage("msg_new_register_from_existing_register", null);
					return selectRegister(newRegister);
				}
			} else {
				JSFUtil.addInfoMessage("msg_new_register_from_existing_contact", null);
			}
			
		} else {
			// 2) Se email nao estiver no sistema, cria novo contato (com email) e ja salva. 
			contact = contactService.createPredefinedContact(emailForNewRegister);
			
			JSFUtil.addInfoMessage("msg_new_register_from_new_contact", null);
		}
		
		newRegister = new Register();
		newRegister.setMegaEvent(currentMegaEvent);
		newRegister.setContact(contact);//este contato ou ja existe ou eh novo
		newRegister = registerService.saveRegister(newRegister);//eh preciso salvar um versao preliminar da inscricao

		return selectRegister(newRegister);
	}	
	
	
	
	public void savePersonalData() {
		Contact contactSaved = contactService.saveContact( this.selectedRegister.getContact(), loginBean.getAuthenticatedUser() );
		
		if (flagBrazilian) {
			contactSaved.setIdentityDocumentType(DocumentType.CPF);
		}
		
		selectedRegister.setContact(contactSaved);
	}
	

	/**
	 * Responde automaticamente todas as questions 
	 */
	public void answerAllMedicalQuestions(boolean booleanAnswer) {
		for (MedicalAnswer answer : this.medicalAnswers) {
			answer.setBooleanAnswer( booleanAnswer );
		}
	}

	
	/**
	 * Configure os aspectos do checkin antes de confirmar:
	 * 1.instancia checkin real
	 * 	1.1.seta a data real de checkin
	 * 	1.2.seta o usuario que realizou o checkin
	 * 3.seta a data prevista de checkout
	 */
	private void preconfigureCheckin() {
		//1.check-in
		CheckIn checkInReal = new CheckIn();
		checkInReal.setCheckinDate( manualCheckinDate );
		checkInReal.setCheckinUser( loginBean.getAuthenticatedUser() );

		selectedRegister.setCheckin( checkInReal );

		
		//) data prevista do checkout:
		selectedRegister.setPreviewCheckoutDate( manualCheckoutDate );

		
		//data de checkin
		//[29/06/14] por que este codigo se ja esta sendo feito acima?
		//		Date checkinDate = new Date();
		//		selectedRegister.getCheckin().setCheckinDate( checkinDate );
		//		selectedRegister.setPreviewCheckinDate( checkinDate );
		
	}

	
	/**
	 * Action que confirma o checkin
	 */
	public void confirmCheckin() {
		//1.efetivamente, confirma o checkin
		selectedRegister = registerService.confirmCheckin( selectedRegister );
		
		//2.msg de sucesso
		JSFUtil.addInfoMessage("msg_checkin_confirmed_sucess", null);
		
		//3.email de confirmacao do checkin
		sendConfirmationEmail();

		//4.renova a pesquisa (para nao aparecer o register que acabou de checked-in)
		searchByKeywork();
	}
	
	
	private void sendConfirmationEmail() {
		SimpleEmailBuilder builder = new CheckinConfirmationEmailBuilder(selectedRegister);
		
		emailService.sendEmail(builder.getContactRecipient()
						      ,builder.getReplyTo()
							  ,builder.getSubject()
							  ,builder.getContent() );
		
		JSFUtil.addInfoMessage("msg_email_sent_for_confirmation");
	}
	

	/** 
	 * Valida se a data manual de checkout eh depois de checkin
	 */
	public boolean validateCheckDates() {
		if (manualCheckinDate.after(manualCheckoutDate)) {
			JSFUtil.addErrorMessage("msg_checkin_date_after_checkout_date", null);
			return false;
		}
		if (manualCheckinDate.equals(manualCheckoutDate)) {
			JSFUtil.addErrorMessage("msg_checkin_date_equals_checkout_date", null);
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	//acessores...	
	public List<Register> getRegisters() {
		return registers;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Register getSelectedRegister() {
		return selectedRegister;
	}
	public List<MedicalAnswer> getMedicalAnswers() {
		return medicalAnswers;
	}
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	public String getEmailForNewRegister() {
		return emailForNewRegister;
	}
	public void setEmailForNewRegister(String emailForNewRegister) {
		this.emailForNewRegister = emailForNewRegister;
	}
	public Date getManualCheckinDate() {
		return manualCheckinDate;
	}
	public void setManualCheckinDate(Date manualCheckinDate) {
		this.manualCheckinDate = manualCheckinDate;
	}
	public Date getManualCheckoutDate() {
		return manualCheckoutDate;
	}
	public void setManualCheckoutDate(Date manualCheckoutDate) {
		this.manualCheckoutDate = manualCheckoutDate;
	}
	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
	public Register getFamiliarRegister() {
		return familiarRegister;
	}
	public void setFamiliarRegister(Register familiarRegister) {
		this.familiarRegister = familiarRegister;
	}
	public List<Event> getDependentEvents() {
		return dependentEvents;
	}
	public List<Event> getSeletedDependentEvents() {
		return seletedDependentEvents;
	}
	public void setSeletedDependentEvents(List<Event> seletedDependentEvents) {
		this.seletedDependentEvents = seletedDependentEvents;
	}
	public Register getThirdRegister() {
		return thirdRegister;
	}
	public void setThirdRegister(Register thirdRegister) {
		this.thirdRegister = thirdRegister;
	}
	public Boolean getFlagBrazilian() {
		return flagBrazilian;
	}
	public void setFlagBrazilian(Boolean flagBrazilian) {
		this.flagBrazilian = flagBrazilian;
	}

	private static final long serialVersionUID = -2685861316029193466L;
}
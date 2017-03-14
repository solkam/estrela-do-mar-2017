package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.builder.PaymentDocketEmailBuilder;
import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.security.LoginBean;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.builder.SimpleEmailBuilder;
import com.cb.mundo.model.entity.CheckIn;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterCredit;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.RegisterDetailPayment;
import com.cb.mundo.model.entity.RegisterDocket;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;
import com.cb.mundo.model.service.ContactService;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.MedicalFormService;
import com.cb.mundo.model.service.RegisterService;
import com.cb.mundo.model.service.email.EmailService;

/**
 * Managed Bean para gestao de pagamentos
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="paymentBean")
@ViewScoped
public class PaymentBean implements Serializable {
	private static final long serialVersionUID = -2927097952771131219L;

	@EJB EventService eventService;
	@EJB RegisterService registerService;
	@EJB ContactService contactService;
	@EJB MedicalFormService medicalFormService;
	@EJB EmailService emailService;
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;
	
	private String keyword;

	private List<Register> registers;
	private Register selectedRegister;

	/**
	 * Eh preciso manter esta variavel em separado de register, pois
	 * ha um EAGER loading explicito quando se seleciona um register
	 * para checkin.
	 */
	private Contact selectedContact;

	
	private List<EventWeek> availableEventWeeks;

	private RegisterDetail registerDetail;
	private RegisterDetailPayment registerDetailPayment;
	
	private List<MedicalAnswer> medicalAnswers;

//informacoes gerais:
	private List<Register> pastedRegisters;
	

	
	@PostConstruct void init() {
	}
	
//recibos
	private RegisterDocket registerDocket;
	
	public void generateDocketForPrint() {
		registerDocket = registerService.findOrCreateRegisterDocketByRegister(selectedRegister);
	}
	
	public void sendPaymentDocketByEmail() {
		SimpleEmailBuilder builder = new PaymentDocketEmailBuilder(selectedRegister);
		
		emailService.sendEmail(builder.getContactRecipient()
				,builder.getReplyTo()
				,builder.getSubject()
				,builder.getContent()
				);
		
		JSFUtil.addInfoMessage("msg_payment_docket_sended_by_email_sucess");
	}
	
	
//credits
	private RegisterCredit credit = new RegisterCredit();
	
	public void resetCredit() {
		credit = new RegisterCredit();
		credit.setAccount( selectedRegister.getAccount() );
		credit.setDate( new Date() );
		credit.setMethod( MegaEventPaymentMethod.BT );
	}
	
	public void saveCredit() {
		credit = registerService.saveRegisterCredit(credit);
		
		refreshRegister();
		
		JSFUtil.addInfoMessage("msg_register_credit_save_sucess", null);
	}
	
	public void prorateCredit(RegisterCredit credit, Register register) {
		registerService.prorateCredit(credit, register);
		
		refreshRegister();
		
		JSFUtil.addInfoMessage("msg_credit_prorated_sucess", null);
	}

	
	public void removeCredit() {
		registerService.removeRegisterCredit(credit);
		
		refreshRegister();
		
		JSFUtil.addInfoMessage("msg_credit_remove_sucess", null);
		
	}
	
//listener...
	public void onChangeDetailValue() {
		//1.salva todos os detalhes (para meu eventos)
		for (RegisterDetail detail : selectedRegister.getDetails()) {
			registerService.saveRegisterDetail(detail);
		}
		//2.salva todos os detalhes dos dependentes.
		for (Register dependentRegister : selectedRegister.getDependentRegisters()) {
			for (RegisterDetail depDetail : dependentRegister.getDetails() ) {
				registerService.saveRegisterDetail( depDetail );
			}
		}
		//3.refresca a inscricao
		refreshRegister();
	}
	


//actions
	
	public void search() {
		MegaEvent currentMegaEvent = sessionHolder.getCurrentMegaEvent();
		registers = registerService.searchRegistersByMegaEventAndKeyword(currentMegaEvent, keyword);
	}
	
	
	/**
	 * Invocado ao clicar um dos inscrito resultado da pesquisa
	 * @param register
	 */
	public void manageRegister(Register register) {
		this.selectedRegister = register;
		this.selectedContact = register.getContact();
		refreshRegister();
		refreshAvailableEventWeeks();
		refreshMedicalAnswers();
		refreshPastRegisters();
		refreshContact();
	}
	
	public void removeRegisterDetail() {
		registerService.removeRegisterDetail(registerDetail);
		refreshRegister();
		refreshAvailableEventWeeks();
		
		JSFUtil.addInfoMessage("msg_register_detail_and_payments_remove_sucess", null);
	}
	
	public void confirmAsParticipant(Event event) {
		registerDetail = new RegisterDetail();
		registerDetail.setRegister( this.selectedRegister );
		registerDetail.setEventWeek( event.getEventWeek() );
		registerDetail.setEvent( event );
		registerDetail.setValue( event.getValueParticipant() );
		registerDetail.setPresence( EventPresence.PARTICIPANT );
		
		registerDetail = registerService.saveRegisterDetail(registerDetail);
		refreshRegister();
		refreshAvailableEventWeeks();
		
		JSFUtil.addInfoMessage("msg_event_add_sucess", null);
	}
	
	public void confirmAsStaff(Event event) {
		registerDetail = new RegisterDetail();
		registerDetail.setRegister( this.selectedRegister );
		registerDetail.setEventWeek( event.getEventWeek() );
		registerDetail.setEvent( event );
		registerDetail.setValue( event.getValueStaff() );
		registerDetail.setPresence( EventPresence.STAFF );
		
		registerDetail = registerService.saveRegisterDetail(registerDetail);
		refreshRegister();
		refreshAvailableEventWeeks();
		
		JSFUtil.addInfoMessage("msg_event_add_sucess", null);
	}
	
	
	
/* **********
 * PAGAMENTOS 
 ************/	
	/**
	 * Pagamento parcial eh referente a apenas um evento
	 * @param detail
	 */
	public void addPartialPayment(RegisterDetail detail) {
		this.registerDetail = detail;
		resetPayment();
		//automaticamente seta o valor do pagamento como o valor pendente
		registerDetailPayment.setValue( detail.getCalculatedPendentValue() );
	}
	
	public void savePartialPayment() {
		registerService.saveRegisterDetailPayment(registerDetailPayment);
		
		refreshRegister();
		refreshRegisterDetail();
		resetPayment();
		
		JSFUtil.addInfoMessage("msg_register_payment_add_sucess", null);
	}
	
	private void resetPayment() {
		registerDetailPayment = new RegisterDetailPayment();
		registerDetailPayment.setRegisterDetail( registerDetail );
		registerDetailPayment.setDate( new Date() );
		registerDetailPayment.setValue( this.registerDetail.getCalculatedPendentValue() );
	}
	
	/**
	 * Utiliza um registro de credito como pagamento de um evento
	 * @param credit
	 */
	public void useCreditAsPayment(RegisterCredit credit, RegisterDetail detail) {
		registerService.transformCreditIntoPayment(credit, detail);
		refreshRegisterDetail();  //para atualizar pagamentos...
		refreshRegister(); //para atualizar creditos...
		
		JSFUtil.addInfoMessage("msg_credit_assigned_as_payment_sucess", null);
	}
	
	/**
	 * Utiliza um pagamento para ser transformado em credito
	 * @param payment
	 */
	public void usePaymentAsCredit(RegisterDetailPayment payment) {
		registerService.transformPaymentIntoCredit(payment);
		refreshRegisterDetail(); //atualizando pagamentos...
		refreshRegister();//atualizando creditos...
		JSFUtil.addInfoMessage("msg_payment_assigned_as_credit_sucess", null);
	}
	
	
/* 
 * pagamentos parciais	
 *********************/
	
	//metodo de remover recebe direto como parametro
//	private RegisterDetailPayment paymentToRemove;//payment atribuido para ser removido
//	
//	public void setPaymentToRemove(RegisterDetailPayment paymentToRemove) {
//		this.paymentToRemove = paymentToRemove;
//	}
//	public RegisterDetailPayment getPaymentToRemove() {
//		return paymentToRemove;
//	}
	
	
	/**
	 * Remove um pagamento (dialog de pagamentos parciais)
	 * [15Abr2014] recebe diretamente o parametro
	 * @param payment
	 */
	public void removePayment(RegisterDetailPayment selectedPayment) {
		registerService.removeRegisterDetailPayment( selectedPayment );
		JSFUtil.addInfoMessage("msg_register_payment_remove_sucess", null);
		
		refreshRegister();
		
		refreshRegisterDetail();
	}
		

	
/* Pagamentos Totais	
 *******************/
	
	private List<RegisterDetailPayment> listForTotalPayment;
	private BigDecimal valueForTotalPayment;
	private MegaEventPaymentMethod methodForTotalPayment = MegaEventPaymentMethod.CC;
	private Date dateForTotalPayment = new Date();
	private String saleOrderForTotalPayment;
	private String noteForTotalPayment;
	private String checkTitularForTotalPayment;
	private String checkBankForTotalPayment;
	private String checkAccountForTotalPayment;
	private String checkNumberForTotalPayment;

	
	/**
	 * Pagamento total eh referente a toda a inscricao,
	 * ou seja, de todo valor pendente.
	 * Pagamento total se referencia a 'selectedRegister'
	 * - Calcula tb o valor total do pagamento 
	 */
	public void addTotalPayment(Register register) {
		listForTotalPayment = registerService.searchRegisterDetailPaymentForTotalPayment(register);
		valueForTotalPayment = BigDecimal.ZERO;
		for (RegisterDetailPayment payment : listForTotalPayment) {
			valueForTotalPayment = valueForTotalPayment.add( payment.getValue() );
		}
	}
	
	public void saveTotalPayment() {
		for (RegisterDetailPayment payment : listForTotalPayment) {
			payment.setDate( dateForTotalPayment );
			payment.setMethod(methodForTotalPayment);
			payment.setSaleOrder(saleOrderForTotalPayment);
			payment.setNote(noteForTotalPayment);
			payment.setCheckAccount(checkAccountForTotalPayment);
			payment.setCheckBank(checkBankForTotalPayment);
			payment.setCheckNumber(checkNumberForTotalPayment);
			payment.setCheckTitular(checkTitularForTotalPayment);
			payment = registerService.saveRegisterDetailPayment(payment);
		}
		refreshRegister();
		
		JSFUtil.addInfoMessage("msg_total_payment_done_sucess", null);
			
	}
	
	
/* Personal Data
 ***************/

	public void manageContact() {
		refreshContact();
	}
	
	public void savePersonalData() {
		selectedContact = contactService.saveContact( selectedContact, loginBean.getAuthenticatedUser() );
		refreshContact();
		
		JSFUtil.addInfoMessage("msg_personal_data_save_sucess", null);
	}


/* Produtor e Trainner
 *********************/
	/**
	 * praticamente igual ao savePersonalData mas com outra msg de sucess
	 */
	public void saveProductorAndTrainner() {
		selectedContact = contactService.saveContact(selectedContact, loginBean.getAuthenticatedUser() );
		refreshContact();
		
		JSFUtil.addInfoMessage("msg_productor_and_trainner_save_sucess", null);
	}
	
	public void saveMedicalAnswers() {
		medicalFormService.saveMedicalAnswers(medicalAnswers);
		JSFUtil.addInfoMessage("msg_medical_form_save_sucess", null);
	}
	
	/**
	 * Simplesmente salva o register que implicitamente salvara tripInfos
	 */
	public void saveTripInfo() {
		selectedRegister = registerService.saveRegister(selectedRegister);
		refreshRegister();
		
		JSFUtil.addInfoMessage("msg_trip_data_save_sucess", null);
	}

	
//[24 ABR 2014] Criada funcionalidade propria	
//	public void saveHostingInfo() {
//		//hosting eh embeddable. Portant basta salvar register
//		selectedRegister = eventService.saveRegister( selectedRegister);
//		refreshRegister();
//		
//		JSFUtil.addInfoMessage("msg_hosting_info_save_sucess", null);
//	}
	
	
//alterar status...
	
	/**
	 * Cria um objeto checkin setando os valores iniciais
	 */
	public void createCheckin() {
		CheckIn checkin = new CheckIn();
		checkin.setCheckinUser( loginBean.getAuthenticatedUser() );
		checkin.setCheckinDate( new Date() );
		selectedRegister.setCheckin(checkin);
	}
	
	public void alterStatusToCheckedin() {
		registerService.confirmCheckin(selectedRegister);
		
		refreshRegister();
		
		JSFUtil.addInfoMessage("msg_register_status_altered_sucess", null);
	}
	
	public void removeRegister() {
		//1.remove register
		registerService.removeRegister(selectedRegister);
		
		//2.renova a pesquisa
		search();
		
		//3.reinstancia selectedRegsiter
		selectedRegister = null;
		
		//4.msg de sucesso
		JSFUtil.addInfoMessage("msg_register_remove_sucess", null);
	}
	
	
/* *********************************
 * Gerenciar transferencia de evento
 ***********************************/  

	private RegisterDetail detailToTransfer;

	public void manageEventTransfering(RegisterDetail selectedDetail) {
		detailToTransfer = selectedDetail;
		
		futureEventsWithSameFormation = eventService.searchFutureEventsWithSameFormation( selectedDetail.getEvent() );
		futureRegister = null;
	}
	
/*
 * ...para evento futuro
 ***********************/

	private List<Event> futureEventsWithSameFormation;
	
	public void transferToFutureEvent(Event futureEvent) {
		//1.transfere para 
		registerService.transferToFutureEvent(detailToTransfer, futureEvent);
		
		//2.refresca atual register
		refreshRegister();

		//3.msg sucesso
		JSFUtil.addInfoMessage("msg_event_transfered_sucess");
	}
	
	
/* ...para outra inscricao
 *************************/
	private Register futureRegister;
	
	/**
	 * Recebe o nome de contato (novo ou civil) e retorna as inscricoes
	 * para o mega evento currente
	 * Usado no auto-complete
	 * @param name
	 * @return
	 */
	public List<Register> completeRegisterByContactName(String name) {
		MegaEvent actualMegaEvent = sessionHolder.getCurrentMegaEvent();
		
		List<Register> registers = registerService.searchRegistersByMegaEventAndKeyword(actualMegaEvent, name);
		return registers;
	}
	
	public void transferToOtherRegister() {
		if (validateFutureRegister()) {
			//1.simplesmente troca o register do detail selecionado
			detailToTransfer.setRegister(futureRegister);
			detailToTransfer = registerService.saveRegisterDetail(detailToTransfer);
			
			//2.refresca tudo
			refreshRegister();
			
			//3.msg sucesso
			JSFUtil.addInfoMessage("msg_event_transfered_sucess");
		}
	}
	
	
	private boolean validateFutureRegister() {
		if (futureRegister==null) {
			JSFUtil.addErrorMessage("msg_no_contact_selected", null);
			return false;
		}
		return true;
	}

	
//utilit�rios...	
	private void refreshRegister() {
		this.selectedRegister = registerService.refreshRegisterWithPaymentsAndCredits(selectedRegister);
	}
	
	private void refreshAvailableEventWeeks() {
		this.availableEventWeeks = eventService.searchEventWeekAndAvailableEventsByRegister(selectedRegister);
	}
	
	private void refreshRegisterDetail() {
		this.registerDetail = registerService.refreshRegisterDetailWithPayments(registerDetail);
	}
	
	private void refreshMedicalAnswers() {
		medicalAnswers = medicalFormService.searchOrCreateMedicalAnswerByRegister(selectedRegister);
	}
	
	
	/**
	 * Pesquisar por todas as inscricoes de um contact.
	 * Eh necessario que creditos e pagamentos venham carregados;
	 */
	private void refreshPastRegisters() {
		pastedRegisters = registerService.searchRegistersByContactLoadingCreditsAndPayments( selectedContact );
	}
	
	
	private void refreshContact() {
		selectedContact = contactService.refreshContact( selectedContact );
	}
	
	
//acessores

	public RegisterDetail getRegisterDetail() {
		return registerDetail;
	}
	public void setRegisterDetail(RegisterDetail registerDetail) {
		this.registerDetail = registerDetail;
	}
	public List<Register> getRegisters() {
		return registers;
	}
	public Register getSelectedRegister() {
		return selectedRegister;
	}
	public void setSelectedRegister(Register selectedRegister) {
		this.selectedRegister = selectedRegister;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public List<EventWeek> getAvailableEventWeeks() {
		return availableEventWeeks;
	}
	public RegisterDetailPayment getRegisterDetailPayment() {
		return registerDetailPayment;
	}
	public void setRegisterDetailPayment(RegisterDetailPayment registerDetailPayment) {
		this.registerDetailPayment = registerDetailPayment;
	}
	public BigDecimal getValueForTotalPayment() {
		return valueForTotalPayment;
	}
	public void setValueForTotalPayment(BigDecimal valueForTotalPayment) {
		this.valueForTotalPayment = valueForTotalPayment;
	}
	public MegaEventPaymentMethod getMethodForTotalPayment() {
		return methodForTotalPayment;
	}
	public void setMethodForTotalPayment(MegaEventPaymentMethod methodForTotalPayment) {
		this.methodForTotalPayment = methodForTotalPayment;
	}
	public String getSaleOrderForTotalPayment() {
		return saleOrderForTotalPayment;
	}
	public void setSaleOrderForTotalPayment(String saleOrderForTotalPayment) {
		this.saleOrderForTotalPayment = saleOrderForTotalPayment;
	}
	public String getNoteForTotalPayment() {
		return noteForTotalPayment;
	}
	public void setNoteForTotalPayment(String noteForTotalPayment) {
		this.noteForTotalPayment = noteForTotalPayment;
	}
	public List<RegisterDetailPayment> getListForTotalPayment() {
		return listForTotalPayment;
	}
	public List<MedicalAnswer> getMedicalAnswers() {
		return medicalAnswers;
	}
	public RegisterCredit getCredit() {
		return credit;
	}
	public void setCredit(RegisterCredit credit) {
		this.credit = credit;
	}
	public RegisterDocket getRegisterDocket() {
		return registerDocket;
	}
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	public List<Register> getPastedRegisters() {
		return pastedRegisters;
	}
	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
	public Contact getSelectedContact() {
		return selectedContact;
	}
	public List<Event> getFutureEventsWithSameFormation() {
		return futureEventsWithSameFormation;
	}
	public RegisterDetail getDetailToTransfer() {
		return detailToTransfer;
	}
	public void setDetailToTransfer(RegisterDetail detailToTransfer) {
		this.detailToTransfer = detailToTransfer;
	}
	public Register getFutureRegister() {
		return futureRegister;
	}
	public void setFutureRegister(Register futureRegister) {
		this.futureRegister = futureRegister;
	}
	public String getCheckTitularForTotalPayment() {
		return checkTitularForTotalPayment;
	}
	public void setCheckTitularForTotalPayment(String checkTitularForTotalPayment) {
		this.checkTitularForTotalPayment = checkTitularForTotalPayment;
	}
	public String getCheckBankForTotalPayment() {
		return checkBankForTotalPayment;
	}
	public void setCheckBankForTotalPayment(String checkBankForTotalPayment) {
		this.checkBankForTotalPayment = checkBankForTotalPayment;
	}
	public String getCheckAccountForTotalPayment() {
		return checkAccountForTotalPayment;
	}
	public void setCheckAccountForTotalPayment(String checkAccountForTotalPayment) {
		this.checkAccountForTotalPayment = checkAccountForTotalPayment;
	}
	public String getCheckNumberForTotalPayment() {
		return checkNumberForTotalPayment;
	}
	public void setCheckNumberForTotalPayment(String checkNumberForTotalPayment) {
		this.checkNumberForTotalPayment = checkNumberForTotalPayment;
	}
	public Date getDateForTotalPayment() {
		return dateForTotalPayment;
	}
	public void setDateForTotalPayment(Date dateForTotalPayment) {
		this.dateForTotalPayment = dateForTotalPayment;
	}
	

	
}

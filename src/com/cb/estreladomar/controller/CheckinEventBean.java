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

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterCredit;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.RegisterDetailPayment;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.RegisterService;

/**
 * Managed Bean especifico para Evento e Pagamento do CheckIn
 * 
 * @author Solkam
 * @since 09 dez 2012
 */
@ManagedBean(name="checkInEventBean")
@ViewScoped
public class CheckinEventBean implements Serializable {

	@EJB EventService eventService;
	
	@EJB RegisterService registerService;
	
	@ManagedProperty("#{checkInBean}")
	private CheckInBean checkInBean;
	
	private RegisterDetail registerDetail;
	
	//Payments (para adicionar pagamentos no momento do checking)
	private RegisterDetailPayment registerDetailPayment = new RegisterDetailPayment();
	
	//lista para adicionar eventos
	private List<EventWeek> eventWeeks;
	
	@PostConstruct void init() {
		//		populateEventWeeks();
	}

	public void populateEventWeeks() {
		Register selectedRegister = checkInBean.getSelectedRegister();
		eventWeeks = eventService.searchEventWeekAndAvailableEventsByRegister(selectedRegister);
	}
	
	//actions...	
	public void confirmAsParticipant(Event event) {
		try {
			registerDetail = new RegisterDetail();
			registerDetail.setRegister( checkInBean.getSelectedRegister() );
			registerDetail.setEventWeek( event.getEventWeek() );
			registerDetail.setEvent( event );
			registerDetail.setValue( event.getValueParticipant() );
			registerDetail.setPresence( EventPresence.PARTICIPANT );
			
			registerDetail = registerService.saveRegisterDetail(registerDetail);
			checkInBean.refreshRegister();
			populateEventWeeks();
			
			JSFUtil.addInfoMessage("msg_event_add_sucess", null);
			
		}catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
	public void confirmAsStaff(Event event) {
		try {
			registerDetail = new RegisterDetail();
			registerDetail.setRegister( checkInBean.getSelectedRegister() );
			registerDetail.setEventWeek( event.getEventWeek() );
			registerDetail.setEvent( event );
			registerDetail.setValue( event.getValueStaff() );
			registerDetail.setPresence( EventPresence.STAFF );
			
			registerDetail = registerService.saveRegisterDetail(registerDetail);
			checkInBean.refreshRegister();
			populateEventWeeks();
			
			JSFUtil.addInfoMessage("msg_event_add_sucess", null);
			
		}catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
	/**
	 * Remove um evento da lista 
	 * Se houver pagamentos para este evento:
	 * - Adicionar o valor pago como credito
	 * - Remove todos os pagamentos
	 * - Refresca a inscricao
	 */
	public void removeRegisterDetail() {
		try {
			registerService.removeRegisterDetail(registerDetail);
			checkInBean.refreshRegister();
			
			JSFUtil.addInfoMessage("msg_register_detail_and_payments_remove_sucess", null);
			
		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
	private void refreshRegisterDetail() {
		this.registerDetail = registerService.refreshRegisterDetailWithPayments(registerDetail);
	}
	
	
	
	/* Pagamentos Parciais	
	 *********************/
	
	/**
	 * Pagamento parcial eh referente a apenas um evento
	 * @param detail
	 */
	public void addPartialPayment(RegisterDetail detail) {
		this.registerDetail = detail;
		resetPayment();
		//automaticamente seta o valor do pagamento como o valor pendente
		registerDetailPayment.setValue( detail.getCalculatedPendentValue() );
		registerDetailPayment.setDate( new Date() );
		registerDetailPayment.setMethod( MegaEventPaymentMethod.CC );
	}
	
	public void savePartialPayment() {
		registerService.saveRegisterDetailPayment(registerDetailPayment);
		
		checkInBean.refreshRegister();
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
		try {
			registerService.transformCreditIntoPayment(credit, detail);
			this.refreshRegisterDetail();  //para atualizar pagamentos...
			checkInBean.refreshRegister(); //para atualizar creditos...
			
			JSFUtil.addInfoMessage("msg_credit_assigned_as_payment_sucess", null);
			
		}catch(Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
	/**
	 * Utiliza um pagamento para ser transformado em credito
	 * @param payment
	 */
	public void usePaymentAsCredit(RegisterDetailPayment payment) {
		try {
			registerService.transformPaymentIntoCredit(payment);
			this.refreshRegisterDetail(); //atualizando pagamentos...
			checkInBean.refreshRegister();//atualizando creditos...
			JSFUtil.addInfoMessage("msg_payment_assigned_as_credit_sucess", null);
			
		}catch(Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
	
	
	/**
	 * Remove um pagamento
	 * @param payment
	 */
	public void removePayment() {
		try {
			registerService.removeRegisterDetailPayment(paymentToRemove);
			
			JSFUtil.addInfoMessage("msg_register_payment_remove_sucess", null);
			
			checkInBean.refreshRegister();
			
			refreshRegisterDetail();
			
		}catch(Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
		
	private RegisterDetailPayment paymentToRemove;//payment atribuido para ser removido

	public void setPaymentToRemove(RegisterDetailPayment paymentToRemove) {
		this.paymentToRemove = paymentToRemove;
	}

	
	/* Pagamentos TOTAIS	
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
			payment.setMethod(methodForTotalPayment);
			payment.setSaleOrder(saleOrderForTotalPayment);
			payment.setDate(dateForTotalPayment);
			payment.setCheckAccount(checkAccountForTotalPayment);
			payment.setCheckBank(checkBankForTotalPayment);
			payment.setCheckNumber(checkNumberForTotalPayment);
			payment.setCheckTitular(checkTitularForTotalPayment);
			payment.setNote(noteForTotalPayment);
			
			payment = registerService.saveRegisterDetailPayment(payment);
		}
		checkInBean.refreshRegister();
		this.refreshRegisterDetail();
		
		JSFUtil.addInfoMessage("msg_total_payment_done_sucess", null);
	}
	
	
	
	
	
	//acessores...
	public List<EventWeek> getEventWeeks() {
		return eventWeeks;
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
	public CheckInBean getCheckInBean() {
		return checkInBean;
	}
	public void setCheckInBean(CheckInBean checkInBean) {
		this.checkInBean = checkInBean;
	}
	public RegisterDetail getRegisterDetail() {
		return registerDetail;
	}
	public void setRegisterDetail(RegisterDetail registerDetail) {
		this.registerDetail = registerDetail;
	}
	public RegisterDetailPayment getRegisterDetailPayment() {
		return registerDetailPayment;
	}
	public void setRegisterDetailPayment(RegisterDetailPayment registerDetailPayment) {
		this.registerDetailPayment = registerDetailPayment;
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
	public Date getDateForTotalPayment() {
		return dateForTotalPayment;
	}
	public void setDateForTotalPayment(Date dateForTotalPayment) {
		this.dateForTotalPayment = dateForTotalPayment;
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
	
	private static final long serialVersionUID = -4322745300273322755L;
}

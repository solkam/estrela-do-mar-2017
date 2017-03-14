package com.cb.estreladomar.controller.util;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * Define todas as navega��es do sistema
 * 
 * @author Solkam
 * @since 25 set 2012
 */
@ManagedBean(name="navigation")
@ApplicationScoped
public class Navigation implements Serializable {
	private static final long serialVersionUID = -7625168645193327879L;

	//constants 	

	public static final String LOGIN = "/login";
	
	//Minhas Inscricoes
	public static final String REGISTER_INDEX          ="/pages/myregister/register-index";
	public static final String REGISTER_STEP_EVENT     ="/pages/myregister/register-step-event";
	public static final String REGISTER_STEP_PERSONAL  ="/pages/myregister/register-step-personal";
	public static final String REGISTER_STEP_MEDICAL   ="/pages/myregister/register-step-medical";
	public static final String REGISTER_STEP_PRODUCTOR ="/pages/myregister/register-step-productor";
	public static final String REGISTER_STEP_TRAINNER  ="/pages/myregister/register-step-trainner";
	public static final String REGISTER_STEP_TRIP      ="/pages/myregister/register-step-trip";
	public static final String REGISTER_STEP_CONDITION ="/pages/myregister/register-step-condition";
	public static final String REGISTER_STEP_CONFIRM   ="/pages/myregister/register-step-confirmation";
	public static final String REGISTER_STEP_SUCESS    ="/pages/myregister/register-sucess";
	
	//Meus Inscritos:
	public static final String MY_REGISTERED_ONES ="/pages/myregisteredones/myregisteredones";
	
	
	//informes
	public static final String REPORT_INDEX               = "/pages/report/report-index";
	public static final String REPORT_PEOPLE_BY_EVENT     = "/pages/report/report-people-by-event";
	public static final String REPORT_REGISTER_BY_WEEK    = "/pages/report/report-register-by-week";
	public static final String REPORT_PRODUCTOR_COMISSION = "/pages/report/report-productor-comission";
	public static final String REPORT_PAYMENT_BY_EVENT    = "/pages/report/report-payments-by-event";	
	public static final String REPORT_HOSTING             = "/pages/report/report-hosting";

	//check-in
	public static final String CHECKIN_INDEX              = "/pages/checkIN/checkin-index";
	public static final String CHECKIN_PARTICIPANT_REVIEW = "/pages/checkIN/checkin-participant-review";
	public static final String CHECKIN_CONTRATED_REVIEW   = "/pages/checkIN/checkin-contrated-review";
	
	//check-out
	public static final String CHECKOUT = "/pages/checkOUT/checkout";
	
	//configuracoes
	public static final String CONFIG_MEGAEVENT = "/pages/config/config-megaevent";
	public static final String CONFIG_WEEK      = "/pages/config/config-eventweek";
	public static final String CONFIG_EVENT     = "/pages/config/config-event";
	public static final String CONFIG_USER      = "/pages/config/config-user";
	public static final String CONFIG_CONTACT   = "/pages/config/config-contact";
	

	public static final String SIGNUP_FORM = "/pages/signup/signup-form";
	
	public static final String PAYMENT = "/pages/payment/payment";
	
	public static final String TRANSPORT = "/pages/transport/transport";

	public static final String SEMINAR_COMPLEMENTAR_SEARCH = "/pages/seminar_complementar/seminar-complementar-search";
	public static final String SEMINAR_COMPLEMENTAR_EDIT   = "/pages/seminar_complementar/seminar-complementar-edit";

	public static final String HOSTING   = "/pages/hosting/hosting";

	
//actions for EL
	
	public String toRegisterIndex() {
		return REGISTER_INDEX;
	}
	
	public String toReportIndex() {
		return REPORT_INDEX;
	}
}

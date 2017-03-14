package com.cb.estreladomar.controller.transbank;

import java.util.Date;
import java.util.Enumeration;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.cb.mundo.model.entity.TransbankPayment;
import com.cb.mundo.model.entity.TransbankPayment.TransbankPaymentStatus;
import com.cb.mundo.model.entity.TransbankPaymentParam;
import com.cb.mundo.model.service.TransbankPaymentService;


/**
 * Classe auxiliar para apoiar em pagamentos da transbank
 * 
 * @author Solkam
 * @since 31 AGO 2013
 */
public class TransbankPaymentHelper {
	
	private TransbankPaymentService service;

	public TransbankPaymentHelper(TransbankPaymentService service) {
		this.service = service;
	}

	
// salva pagamentos com sucesso...
	
	public TransbankPayment saveSucessPayment(HttpServletRequest request) {
		TransbankPayment sucessPayment = new TransbankPayment();
		sucessPayment.setStatus( TransbankPaymentStatus.S );
		return savePayment(request, sucessPayment);
	}
	
	public TransbankPayment saveSucessPayment(FacesContext context) {
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return saveSucessPayment(request);
	}
	
	
// salva pagamentos com falha...
	
	public void saveFailPayment(HttpServletRequest request) {
		TransbankPayment failPayment = new TransbankPayment();
		failPayment.setStatus( TransbankPaymentStatus.F );
		savePayment(request, failPayment);
	}
	
	public void saveFailPayment(FacesContext context) {
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		saveFailPayment(request);
	}
	
	
// Efetivamente salva o pagamento	
	
	/**
	 * Salva os parametros do request enviados pela Transbank.
	 * Um dos parametros retornados ser� o registerId.
	 * @param request
	 */
	private TransbankPayment savePayment(HttpServletRequest request, TransbankPayment payment) {
		payment.setDate( new Date() );
		payment = service.saveTransbankPayment(payment);
		
		Enumeration<String> parameterEnumeration = request.getParameterNames();
		while (parameterEnumeration.hasMoreElements()) {
			String paramName = parameterEnumeration.nextElement();
			String paramValue = request.getParameter(paramName);
			
			TransbankPaymentParam tpp = new TransbankPaymentParam();
			tpp.setParamName(paramName);
			tpp.setParamValue(paramValue);
			tpp.setTransbankPayment(payment);
			
			tpp = service.saveTransbankPaymentParam(tpp);
		}
		return payment;
	}
	
	

	
	

}

package com.cb.mundo.model.service;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.entity.Account;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterCredit;
import com.cb.mundo.model.entity.TransbankPayment;
import com.cb.mundo.model.entity.TransbankPaymentParam;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;

/**
 * EJB para pagamento do transbank
 * 
 * @author Solkam
 * @since 31 AGO 2013
 */
@Stateless
public class TransbankPaymentService {

	@EJB RegisterService eventService;

	@PersistenceContext
	private EntityManager manager;
	
	
	
	public TransbankPayment saveTransbankPayment(TransbankPayment payment) {
		return manager.merge( payment );
	}

	
	public TransbankPaymentParam saveTransbankPaymentParam(TransbankPaymentParam param) {
		return manager.merge( param );
	}
	
	/**
	 * A partir de um pagamento da transbank (e seus paramentros):
	 * - Recupera o Register referente
	 * - Salva um credito para o contato do register
	 * - invoca o metodo de proraterar do eventService
	 * @param transbankPayment
	 */
	
	public void prorateTransbankPayment(TransbankPayment transbankPayment) {
		//1.traz para estado gerenciado
		transbankPayment = manager.find(TransbankPayment.class, transbankPayment.getId() );
		
		//2.recuperar o register referente
		Long registerId = transbankPayment.retrieveRegisterId();
		Register register = manager.find(Register.class, registerId);

		if (register==null) {//inscricao nao identificada
			transbankPayment.setNote("RegisterId="+registerId+" nao encontrado. Impossivel definir o autor do pagamento.");
		
		} else {//ok
		
			//3.criar um credito
			RegisterCredit credit = new RegisterCredit();
			Account account = register.getAccount();
			credit.setAccount( account );
			credit.setValue( transbankPayment.retrievePaymentValue() );
			credit.setMethod( MegaEventPaymentMethod.CC );
			credit.setSaleOrder( registerId.toString() );//saleOrder=registerId (vide register-payment.xhtml)
			credit.setNote( transbankPayment.getParamsAsString() );
			credit.setDate( new Date() );
			credit.setAlreadyUsed( false );
			credit = manager.merge( credit );
			
			eventService.prorateCredit(credit, register);
		}
	}

}

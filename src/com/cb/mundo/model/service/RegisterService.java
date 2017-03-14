package com.cb.mundo.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.dao.AccountReceivableDAO;
import com.cb.mundo.model.dao.RegisterDAO;
import com.cb.mundo.model.entity.Account;
import com.cb.mundo.model.entity.ConfigParam;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.HostingConfirmed;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.PaymentCurrency;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterCredit;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.RegisterDetailPayment;
import com.cb.mundo.model.entity.RegisterDocket;
import com.cb.mundo.model.entity.Transport;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;
import com.cb.mundo.model.entity.enumeration.RefundStrategy;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.exception.ChangeEventAlreadyDoneException;
import com.cb.mundo.model.exception.CreditProratingNotAllowedForNoPendentValueException;
import com.cb.mundo.model.exception.EventDuplicatedOnRegisterException;
import com.cb.mundo.model.exception.NoPendentPaymentException;
import com.cb.mundo.model.exception.RegisterHasRegisterDetailsException;
import com.cb.mundo.model.util.NumberUtil;

import static com.cb.mundo.model.util.QueryUtil.toLikeMatchModeANY;

/**
 * Service de negocio para Register e associados
 * (fruto do refactoring de EventService)
 * 
 * @author Solkam
 * @since 31 MAR 2015
 */
@Stateless
public class RegisterService {

	@PersistenceContext EntityManager manager;
	
	@EJB MedicalFormService medicalFormService;
	
	@EJB HostingService hostingService;
	
	
	private RegisterDAO registerDAO;
    private AccountReceivableDAO accountReceivableDAO;
	
	@PostConstruct void init() {
		registerDAO = new RegisterDAO(manager);
        accountReceivableDAO = new AccountReceivableDAO(manager);
	}
	
	
	/* ********
	 * Register
	 **********/
	
	/**
	 * Confirma o checkin do Register
	 * @param register
	 * @return
	 */
	public Register confirmCheckin(Register register) {
		//1.invoca o processo de salva comum
		register = saveRegister(register);
		
		//2.lida com credito do produtor
		handleProductorComission( register );
		
		//3.se tudo ocorrer bem, seta o novo status
		register.setStatus( RegisterStatus.CHECKEDIN );
		
		//4.confirma checkin dos dependentes
		for (Register dependentRegister : register.getDependentRegisters()) {
			dependentRegister.setStatus( RegisterStatus.CHECKEDIN );
			saveRegister( dependentRegister );
		}
		
		//5.refresh
		register = refreshRegisterWithPaymentsAndCredits(register);
		
		//6.
		return register;
	}

	
	/**
	 * Confirma o checkin virtual do register
	 * @param register
	 */
	public void confirmCheckinVirtual(Register register) {
		register = manager.merge(register);
//		register.setStatus( RegisterStatus.CHECKEDIN_VIRTUAL );
	}
	
	
	
	/**
	 * Analisa se o inscrito tem produtor, calcula a porcentagem de 
	 * comissao do produtor, com as seguintes regras:
	 * - nao pode ter nome novo
	 * - ter vindo maximo 2x na montanha
	 * - somente participante seminario de formacao
	 * @param register
	 */
	private void handleProductorComission(Register register) {
		//verifica se tem produtor
		Contact productorContact = register.getContact().getProductorContact();
		if (productorContact!=null) {
			
			//1.ainda nao tem nome novo:
			if (!register.getContact().getFlagHasNewName() ) {
			
				//2.veio max 2x no acampamento:
				int timesInMegaEvents = calculateHistoricalTimesOnCampamento( register.getContact() );
				if (timesInMegaEvents <= 2) {
					
					//3.participante de evento de formacion
					//mesmo se for zero, gera o credito para consulta
					BigDecimal totalValueOfFormation = register.getCalculatedParticipantOfFormationTotalValue();

					//busca account do produtor
					Account productorAccount = registerDAO.findAccountByContact( productorContact );
					if (productorAccount==null) {
						productorAccount = new Account();
						productorAccount.setContact( productorContact );
						productorAccount = manager.merge( productorAccount );
					}
					
					//cria um credito
					String creditNote = String.format("Comission sobre %s por los eventos %s"
							, register.getContact().getFullDesc()
							, register.getFormationEventsAsParticipant() );
					
					RegisterCredit c = new RegisterCredit();
					c.setAccount( productorAccount );
					c.setMethod( MegaEventPaymentMethod.PR );
					c.setDate( new Date() );
					c.defineProductorComissionValue( totalValueOfFormation );
					c.setAlreadyUsed( false );
					c.setNote( creditNote );
					c = manager.merge( c );
				}
			}
		}
	}
	
	
	/**
	 * Calcula o numero de vezes que ja veio a Montanha.
	 * Tem que analisar o status da inscricao pois pode ser
	 * mega evento do futuro ou do passado que desistiu 
	 * de participar.
	 * @param contact
	 * @return
	 */
	private int calculateHistoricalTimesOnCampamento(Contact contact) {
		int historicalTimes = 0;
		for (Register register : searchRegistersByContact(contact)) {
			if (register.getFlagHasBeenParticipated()) {
				historicalTimes++;
			}
		}
		return historicalTimes;
	}

	
	
	public Register createInitialRegister(MegaEvent me, Contact c) {
		//1.salva os dados primarios
		Register register = new Register();
		register.setMegaEvent( me );
		register.setContact( c );
		
		return manager.merge( register );
	}
	
	
	/**
	 * Salva uma inscricao e recarregamento detalhes e pagamentos.
	 * RNs: 
	 * - Lida com a conta do contato
	 * - Define datas de checks (in e out)
	 * @param register a ser salvo
	 * @return Register recarregado 
	 */
	
	public Register saveRegister(Register register) {
		//1.Criar ou nao uma conta
		handleAccountForNewRegister(register);
		
		//2.Efetivamente salva
		register = manager.merge( register );

		//4.Analisa as datas de checks
		handleCheckDates(register);
		
		return register;
	}
	

	/**
	 * Verifica se register tem uma account associada.
	 * Se nao, busca pelo contact.
	 * Se ainda nao tiver, cria uma nova...
	 * @param register
	 */
	private void handleAccountForNewRegister(Register register) {
		Account account = register.getAccount(); 
		if (account==null) {
			account = registerDAO.findAccountByContact( register.getContact() );
			
			if (account==null) {
				account = new Account();
				account.setContact( register.getContact() );
				account = manager.merge( account );
			}
		}
		register.setAccount(account);
	}

	
	/**
	 * Lida com as datas de check-in e check-out.
	 * @param register
	 */
	private void handleCheckDates(Register register) {
		//1.check-in date:
		//se a data prevista for nula, sera a calculada
		if (register.getPreviewCheckinDate()==null) {
			register.setPreviewCheckinDate( register.getCalculatedCheckinDate() );
		}
		//2.check-out date:
		if (register.getPreviewCheckoutDate()==null) {
			register.setPreviewCheckoutDate( register.getCalculatedCheckoutDate() );
		}
	}
	

	
	/**
	 * Remove um register aplicando RNs
	 * @param register
	 */
	public void removeRegister(Register register) {
		//1.traz para Gerenciado
		register = manager.find(Register.class, register.getId() );
		
		//2.RN sobre details
		verifyIfRegisterHasDetails(register);
		
		//3.remove transportes
		removeRegisterFromTransport(register);

		//4.remove recibos
		removeRegisterDocketByRegister(register);
		
		//5.remove dependentes
		removeDependentRegisterByRegister(register);

		//6.elimina respostas medicas
		medicalFormService.removeMedicalAnswerByRegister(register);
		
		//7.elimina a confirmacao do quarto
		removeHostingConfirmedByRegister(register);

		//se chegou aqui, entao remove
		manager.remove( register );
	}
	
	
	

	/**
	 * Remove o quarto confirmidade da inscricao (se tiver)
	 * @param register
	 */
	private void removeHostingConfirmedByRegister(Register register) {
		HostingConfirmed hostingConfirmed = hostingService.findHostingConfirmedByRegister(register);
		if (hostingConfirmed!=null) {
			manager.remove( hostingConfirmed );
		}
	}


	/**
	 * Remove o register em cascata sem verificar RNs
	 * @param register
	 */
	public void removeRegisterWithoutVerify(Register register) {
		//traz para gerenciado
		register = manager.find(Register.class, register.getId() );
		
		//1. details
		removeRegisterDetailByRegister(register);

		//2.  recibos
		removeRegisterDockectByRegister(register);
		
		//3. transporte
		removeRegisterFromTransport(register);

		//4. resposta do questionario medico
		medicalFormService.removeMedicalAnswerByRegister(register);

		//remove
		manager.remove( register );
	}


	/**
	 * Antes de remover register, verifica se tem details associados
	 * @param register
	 */
	private void verifyIfRegisterHasDetails(Register register) {
		//1.detalhes
		List<RegisterDetail> details = register.getDetails();
		if (!details.isEmpty()) {
			throw new RegisterHasRegisterDetailsException();
		}
	}
	
	/**
	 * Se tiver recibo, elimina
	 * @param register
	 */
	private void removeRegisterDocketByRegister(Register register) {
		RegisterDocket docket = registerDAO.findRegisterDocketByRegister(register);
		if (docket!=null) {
			manager.remove( docket );
		}
	}

	/**
	 * Remover todos os registers de dependentes
	 * (register ja vem gerenciado)
	 * @param register
	 */
	private void removeDependentRegisterByRegister(Register register) {
		for (Register dependentRegister: register.getDependentRegisters() ) {
			removeRegisterWithoutVerify( dependentRegister ); 
			manager.flush();//resolveu NPE do Envers
		}
	}
	
	
	
	public Register findRegisterById(Long id) {
		return manager.find(Register.class, id);
	}
	
	public Register findRegisterByMegaEventAndContact(MegaEvent me, Contact c) {
		return registerDAO.findRegisterByMegaEventAndContact(me, c);
	}
	
	public Register findRegisterByMegaEventAndEmailAndStatus(MegaEvent megaEvent, String email) {
		return registerDAO.findRegisterByMegaEventAndEmail(megaEvent, email);
	}

	
	
	
	/**
	 * Busca inscricoes para checkin. Keyworkd pode ser:
	 * - Nome novo
	 * - Nome civil
	 * - ID da inscricao
	 * - email
	 * @param megaEvent
	 * @param status
	 * @param keyword
	 * @return
	 */
	public List<Register> searchRegistersByMegaEventAndStatusAndKeywords(MegaEvent megaEvent, List<RegisterStatus> listaOfStatus, String keyword) {
		Long registerId = NumberUtil.asLongOrZero(keyword);
		
		return manager.createNamedQuery("searchRegisterByMegaEventAndListOfStatusOrNameOrCivilNameOrIdOrEmail", Register.class)
				.setParameter("pMegaEvent"   , megaEvent )
				.setParameter("pName"        , toLikeMatchModeANY(keyword) )
				.setParameter("pCivilName"   , toLikeMatchModeANY(keyword) )
				.setParameter("pEmail"       , toLikeMatchModeANY(keyword) )
				.setParameter("pId"          , registerId )
				.setParameter("pListOfStatus", listaOfStatus )
				.getResultList();
	}
	
	
	
	/**
	 * Pesquisa Register pela mega evento, status e presenca
	 * (usando em colaboradores)
	 * @param me
	 * @param status
	 * @param presence
	 * @return
	 */
	public List<Register> searchRegisterByMegaEventAndStatusAndEventPresence(MegaEvent me, RegisterStatus status, EventPresence presence) {
		return manager.createNamedQuery("searchRegisterByMegaEventAndStatusAndEventPresence", Register.class)
				.setParameter("pMegaEvent", me)
				.setParameter("pStatus", status)
				.setParameter("pEventPresence", presence)
				.getResultList();
	}
	
	
	public List<Register> searchRegistersByMegaEventAndKeyword(MegaEvent megaEvent, String keyword) {
		return registerDAO.searchRegistersByMegaEventAndKeyword(megaEvent, keyword);
	}

	public List<Register> searchActiveRegistersByContact(Contact contact) {
		return registerDAO.searchActiveRegistersByContact(contact);
	}
	
	
	public List<Register> searchRegistersByMegaEvent(MegaEvent megaEvent) {
		return registerDAO.searchRegistersByMegaEvent(megaEvent);
	}
	
	
	/**
	 * Traz o register para estado managed e forca o load para os calculos
	 * referentes a pagamentos.
	 * Tambem define as datas previstas para check-in e check-out
	 */
	public Register refreshRegisterWithPaymentsAndCredits(Register register) {
		register = manager.find(Register.class, register.getId() );
		register.getCalculatedPaidValue();
		register.getCalculatedTotalValue();
		register.getCalculatedCreditValue();
		register.getCalculatedDependentPaidValue();
		register.getCalculatedDependentPendentValue();
		register.getCalculatedDependentTotalValue();
		
		handleCheckDates(register);
		return register;
	}
	
	
	/**
	 * Recarregar register somente com o necessario no momento da inscricao
	 * @param register
	 * @return
	 */
	public Register refreshRegisterWithDetailsAndHosting(Register register) {
		register = manager.find(Register.class, register.getId() );
		register.getDetails().size();
                
                for (int i = 0; i < register.getDetails().size(); i++) {
                    try{
//                        int size = register.getDetails().get(i).getAccountReceivable().getAccountReceivableDetailList().size();
//                        for (int j = 0; j < size; j++) {
//                            System.out.println(register.getDetails().get(i).getAccountReceivable().getAccountReceivableDetailList().get(j));
//                        }
                    }catch(Exception ex){
                            
                    }
                }
                
		register = this.refreshRegisterWithPaymentsAndCredits(register);
		register.getCalculatedDependentPaidValue();
		
		handleCheckDates(register);
		return register;
	}
	
	
	/**
	 * Refresh detalhe com seus pagamentos
	 * @param detail
	 * @return
	 */
	public RegisterDetail refreshRegisterDetailWithPayments(RegisterDetail detail) {
		detail = manager.find(RegisterDetail.class, detail.getId());
		detail.getCalculatedPaidValue();
		detail.getCalculatedPendentValue();
		return detail;
	}
	
	
	/**
	 * Pesquisa Register pela lista de mega eventos e pela lista de status
	 * (usado em relatorio de pessoas e papeis)
	 * @param megaEventList
	 * @param statusList
	 * @return
	 */
	public List<Register> searchRegisterByMegaEventListAndStatusList(List<MegaEvent> megaEventList, List<RegisterStatus> statusList) {
		return manager.createNamedQuery("searchRegisterByMegaEventListAndStatusList", Register.class)
				.setParameter("pMegaEventList", megaEventList)
				.setParameter("pStatusList", statusList)
				.getResultList();
	}
	

	/**
	 * Pesquisar Register pelo mega evento e lista de status
	 * (usando em Checkin Virtual)
	 * @param megaEvent
	 * @param registerStatusList
	 */
	public List<Register> searchRegisterByMegaEventAndStatusList(MegaEvent megaEvent, List<RegisterStatus> registerStatusList) {
		return manager.createNamedQuery("searchRegisterByMegaEventAndStatusList", Register.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pRegisterStatusList", registerStatusList)
				.getResultList();
	}
	
	
	/**
	 * Pesquisa Register pelo lista de eventos e lista de status
	 * @param listOfEvent
	 * @param listOfStatus
	 * @return
	 */
	public List<Register> searchRegisterByListOfEventAndListOfStatus(List<Event> listOfEvent, List<RegisterStatus> listOfStatus) {
		return manager.createNamedQuery("searchRegisterByListOfEventAndListOfStatus", Register.class)
				.setParameter("pListOfEvent", listOfEvent)
				.setParameter("pListOfStatus", listOfStatus)
				.getResultList();
	}
	
	


	
	
	
	/* ***************
	 * Register Detail
	 *****************/
	
	/**
	 * Salva o detalhe
	 * @param registerDetail
	 * @return
	 */
	public RegisterDetail saveRegisterDetail(RegisterDetail registerDetail) {
		verifyIfEventIsUniqueOnRegister(registerDetail);
		return manager.merge( registerDetail );
	}
	
	
	/**
	 * RN que verifica se o evento eh unico na Inscricao (Register)
	 * @param registerDetail
	 */
	private void verifyIfEventIsUniqueOnRegister(RegisterDetail registerDetail) {
		RegisterDetail detailFound = registerDAO.findRegisterDetailByRegisterAndEvent(
				registerDetail.getRegister(), 
				registerDetail.getEvent());
		
		if (detailFound!=null && !detailFound.equals(registerDetail)) {
			throw new EventDuplicatedOnRegisterException();
		}
	}

	
	public List<RegisterDetail> searchRegisterDetailsByRegister(Register register) {
		//traz para gerenciado...
		register = manager.find(Register.class, register.getId() );
		//forca a carga
		register.getDetails().size();
		//...retorna
		return register.getDetails();
	}
	

	/**
	 * Remove o detalhe
	 */
	public RegisterDetail removeRegisterDetail(RegisterDetail registerDetail) {
		//traz para gerenciado
		registerDetail = manager.find( RegisterDetail.class, registerDetail.getId() );
		
		//transforma pagamentos em creditos...
        handleRegisterDetailPayments( registerDetail);
        
        //sei la o que faz aqui...
        if(registerDetail.getAccountReceivable() != null){
            if(!registerDetail.getAccountReceivable().getAccountReceivableDetailList().isEmpty()){
                for (int i = 0; i < registerDetail.getAccountReceivable().getAccountReceivableDetailList().size(); i++) {
                    accountReceivableDAO.remove(registerDetail.getAccountReceivable().getAccountReceivableDetailList().get(i));
                }
            }
            accountReceivableDAO.remove(registerDetail.getAccountReceivable());
        }

        //remove 
        manager.remove( registerDetail );
        return registerDetail;
	}
	

	/**
	 * Remove todos os detalhes de um register
	 * @param register
	 */
	public void removeRegisterDetailByRegister(Register register) {
		register = manager.find(Register.class, register.getId() );
		for (RegisterDetail detail : register.getDetails()) {
			removeRegisterDetail( detail );
		}
	}
        
	/**
	 * Antes de remover um detalhe, seus pagamentos devem se tornar creditos.
	 * Para cada pagamento, criar um RegisterCredit e remove o pagamento
	 * (parametro ja vem gerenciado)
	 * @param registerDetail
	 */
	private void handleRegisterDetailPayments(RegisterDetail registerDetail) {
		if (registerDetail.getPayments()!=null) {
			for (RegisterDetailPayment payment : registerDetail.getPayments() ) {
				RegisterCredit credit = new RegisterCredit(payment);
				manager.persist(credit);
				manager.remove(payment);
			}
		}
	}






	
	/**
	 * Salva um pagamento
	 * @param payment
	 * @return
	 */
	public RegisterDetailPayment saveRegisterDetailPayment(RegisterDetailPayment payment) {
		return registerDAO.saveRegisterRegisterPayment(payment);
	}

	
	/**
	 * Pesquisa pagamentos pelos filtros
	 * @param name
	 * @param event
	 * @param paymentMethod
	 * @param saleOrder
	 * @return
	 */
	public List<RegisterDetailPayment> searchRegisterDetailPaymentByFilter(
			String name, 
			Event event, 
			MegaEventPaymentMethod paymentMethod,
			String saleOrder) {
		
		return registerDAO.searchRegisterDetailPaymentByFilter(name, event, paymentMethod, saleOrder);
	}

	/**
	 * Pesquisa detalhes pelos filtros de pesquisa.
	 * Tambem forca a carga dos pagamentos
	 * @param eventWeek
	 * @param event
	 * @param name
	 * @return
	 */
	public List<RegisterDetail> searchRegisterDetailByFilters(EventWeek eventWeek, Event event, String name) {
		List<RegisterDetail> details = registerDAO.searchRegisterDetailByFilters(eventWeek, event, name);
		for (RegisterDetail detail : details) {
			detail.getCalculatedPaidValue();
			detail.getCalculatedPendentValue();
		}
		return details;
	}
	
	/**
	 * Transforma um lancamento de credito de uma inscricao em um lancamento de pagamento para uma detalhe de inscricao
	 * Logica adicional: Qdo credito eh maior que o valor pendente do RegisterDetail...
	 * @param credit
	 * @param registerDetail
     * @param acceptNegativePendantValue
	 * @return lancamento de pagamento criado
	 */
	public RegisterDetailPayment transformCreditIntoPayment(RegisterCredit credit, RegisterDetail registerDetail) {
		credit = manager.find(RegisterCredit.class, credit.getId() );
		registerDetail = manager.find(RegisterDetail.class, registerDetail.getId() );
		
		double pendentValueDouble = registerDetail.getCalculatedPendentValue().doubleValue();
		double creditValueDouble = credit.getValue().doubleValue();
		if (pendentValueDouble <= 0.0) {
			throw new NoPendentPaymentException();
		}
		
		// se o valor do credito for menor que o valor pendente do detalhe...
		if ( creditValueDouble <= pendentValueDouble ) {
			//...usa integralmente o credito!!!
			RegisterDetailPayment payment = new RegisterDetailPayment(credit);
			payment.setRegisterDetail(registerDetail);
			
			manager.persist( payment );
			manager.remove( credit );
			return payment;
			
		//se o credito for maior que o valor pendente...	
		} else {
			//...usa somente parte do credito!!!
			RegisterDetailPayment payment = new RegisterDetailPayment(credit);
			payment.setRegisterDetail(registerDetail);
			payment.setValue( new BigDecimal(pendentValueDouble) );
			
			//novo valor do credito sera o credito inicial subtraido do valor pendente
			BigDecimal newCreditValue = credit.getValue().subtract( registerDetail.getCalculatedPendentValue() );
			credit.setValue( newCreditValue );
			manager.persist( payment );
			
			return payment;
		}
	}
	
	/**
	 * Transforma um pagamento ja realizado em credito para a inscricao
	 * @param payment
	 * @return credito criado
	 */
	public RegisterCredit transformPaymentIntoCredit(RegisterDetailPayment payment) {
		payment = manager.find(RegisterDetailPayment.class, payment.getId() );
		
		RegisterCredit credit = new RegisterCredit( payment );
		
		manager.persist( credit );
		manager.remove( payment );
		
		return credit;
	}
	
	
	/**
	 * Remove um pagamento
	 * @param payment
	 */
	public void removeRegisterDetailPayment(RegisterDetailPayment payment) {
		manager.remove( manager.merge(payment) );
	}

	
	/**
	 * Monta uma lista de pagamentos pendente para realizar o pagamento total.
	 * [13 OUT 2013] incluido os pagamentos de dependentes
	 * @param register
	 * @return lista de payments pendents
	 */
	public List<RegisterDetailPayment> searchRegisterDetailPaymentForTotalPayment(Register register) {
		List<RegisterDetailPayment> payments = new ArrayList<RegisterDetailPayment>();
		
		register = manager.find(Register.class, register.getId() );
		
		//1.pagamentos diretos
		for (RegisterDetail detail : register.getDetails()) {
			addPendentPayment(payments, detail);
		}
		
		//2.pagamentos dos dependentes
		for (Register dependentRegister : register.getDependentRegisters()) {
			
			for (RegisterDetail dependentDetail : dependentRegister.getDetails()) {
			
				addPendentPayment(payments, dependentDetail);
			}
		}
		return payments;
	}

	/**
	 * Gera um registro de pagamento referente ao valor pendente do detalhe e 
	 * adiciona a lista de pagamentos
	 * @param payments
	 * @param detail
	 */
	private void addPendentPayment(List<RegisterDetailPayment> payments, RegisterDetail detail) {
		BigDecimal pendentValue = detail.getCalculatedPendentValue(); 
		if (!pendentValue.equals( BigDecimal.ZERO )) {
			RegisterDetailPayment payment = new RegisterDetailPayment();
			payment.setRegisterDetail( detail );
			payment.setValue( pendentValue );
			payments.add( payment );
		}
	}
	
	
	
	
	
	
	/* *******
	 * Credits
	 **********/
	
	/**
	 * Salva um credito ou abono
	 * @param credit
	 * @return
	 */
	public RegisterCredit saveRegisterCredit(RegisterCredit credit) {
		return manager.merge( credit );
	}

	
	/**
	 * Remove um credito
	 * @param credit
	 */
	public void removeRegisterCredit(RegisterCredit credit) {
		manager.remove( manager.merge(credit) );
	}

	
	/**
	 * Faz a distribuicao do credito em todas os detalhes que tem pendencia de pagamento
	 * @param credit
	 * @param register
	 */
	public void prorateCredit(RegisterCredit credit, Register register) {
		//traz objetos para managed...
		register = manager.find(Register.class, register.getId() );
		verifyIfAllowedCreditProrating(register);
		
		credit   = manager.find(RegisterCredit.class, credit.getId() );
		
		BigDecimal creditValue  = credit.getValue();

		for (RegisterDetail detail : register.getDetails()) {
			
			BigDecimal pendentValue = detail.getCalculatedPendentValue();

			//somente se houver valor pendente, o prorate acontece...
			if (pendentValue.doubleValue() > new BigDecimal("0.00").doubleValue()) {
				BigDecimal paymentValue = null;
				
				//se o valor pendente for menor que o credito, o valor do pagamento sera o proprio valor pendente
				//se o valor pendente for maior que o credito, todo o credito eh usado
				if ( pendentValue.doubleValue() < creditValue.doubleValue() ) {
					paymentValue = pendentValue;
				} else {
					paymentValue = creditValue;
				}
					
				RegisterDetailPayment payment = new RegisterDetailPayment();
				payment.setRegisterDetail( detail                );
				payment.setMethod(         credit.getMethod()    );
				payment.setDate(           new Date()            );
				payment.setCreditDate(     credit.getDate()      );//[Yamarty:21OUT13]guarda o historico de quando o dinheiro entrou em CB
				payment.setNote(           credit.getNote()      );
				payment.setSaleOrder(      credit.getSaleOrder() );
				payment.setValue(          paymentValue          );
                payment.setPaymentCurrency( credit.getPaymentCurrency());
                payment.setWebCodeNumber( credit.getWebCodeNumber() );
                payment.setCompany(credit.getCompany());
                payment.setSaleReceipt(credit.getSaleReceipt());
				
				payment = manager.merge( payment );
				
				//atualiza o valor do credito...
				creditValue = creditValue.subtract( paymentValue );
				
				//se o credito estiver zerado, remove-o
				if (creditValue.equals( new BigDecimal("0.00") )) {
					manager.remove( credit );
					break;
				}
			}
		}
		//sendo BigDecimal imutavel, deve setar em credit
		credit.setValue(creditValue);
	}
	
	/**
	 * Verifica se ha valores pendentes para o pro-rateio. Senao, lanca exception
	 * @param register
	 */
	private void verifyIfAllowedCreditProrating(Register register) {
		BigDecimal registerPendentValue = register.getCalculatedPendentValue(); 
		
		if ( new BigDecimal("0.00").equals( registerPendentValue )) {
			throw new CreditProratingNotAllowedForNoPendentValueException();
		}
	}

	
	
	/* ******
	 * Docket	
	 ********/
	
	public RegisterDocket findOrCreateRegisterDocketByRegister(Register register) {
		RegisterDocket docket = registerDAO.findRegisterDocketByRegister(register);
		if (docket==null) {
			docket = new RegisterDocket();
			docket.setDate( new Date() );
			docket.setRegister( register );
			docket = manager.merge( docket );
		}
		return docket;
	}
	
	
	/**
	 * Remove recibo (caso exista) de um register
	 * @param register
	 */
	public void removeRegisterDockectByRegister(Register register) {
		RegisterDocket docket = registerDAO.findRegisterDocketByRegister(register);
		if (docket!=null) {
			manager.remove( manager.merge(docket));
		}
	}
	
	
	
	/**
	 * Pesquisa por todos os transporte que o register foi vinculado
	 * e remove a referencia.
	 * @param register
	 */
	public void removeRegisterFromTransport(Register register) {
		List<Transport> transports = registerDAO.searchTransportByRegister(register);
		for (Transport t : transports) {
			t.getRegisters().remove( register );
		}
	}
	
	
	

	/**
	 * Pesquisar detalhes para super usuario
	 * @param megaEvent
	 * @param eventWeek
	 * @param event
	 * @param presences
	 * @return
	 */
	public List<RegisterDetail> searchRegisterDetailByMegaEventAndEventWeekAndEvent(MegaEvent megaEvent, 
			EventWeek eventWeek,
			Event event, 
			List<EventPresence> presences) {
		return registerDAO.searchRegisterDetailByMegaEventAndEventWeekAndEvent(megaEvent, eventWeek, event, presences);
	}
	
	/**
	 * Pesquisar register detail somente do produtor
	 * @param megaEvent
	 * @param eventWeek
	 * @param event
	 * @param presences
	 * @return
	 */
	public List<RegisterDetail> searchRegisterDetailByMegaEventAndEventWeekAndEventAndProductor(MegaEvent megaEvent, 
			EventWeek eventWeek, 
			Event event, 
			List<EventPresence> presences, 
			Contact productorContact) {
		
		List<RegisterDetail> registerDetailOnlyProdutor = new ArrayList<RegisterDetail>();
		
		//restrige as inscricoes somente do produtor
		List<RegisterDetail> registerDetailTotal = registerDAO.searchRegisterDetailByMegaEventAndEventWeekAndEvent(megaEvent, eventWeek, event, presences);
		for (RegisterDetail rd : registerDetailTotal) {
			if (productorContact.equals( rd.getRegister().getContact().getProductorContact() )) {
				registerDetailOnlyProdutor.add( rd );
			}
		}
		return registerDetailOnlyProdutor;
	}
	
	
	public List<Register> searchRegistersByContactLoadingCreditsAndPayments(Contact contact) {
		List<Register> pastedRegisters = registerDAO.searchRegistersByContact(contact);
		for (Register pastedRegister : pastedRegisters) {
			pastedRegister.getCalculatedCreditValue();
			pastedRegister.getCalculatedPaidValue();
		}
		return pastedRegisters;
	}

	/**
	 * Pesquisa os registers de um contact
	 * (para mostrar na config de contact)
	 * @param contact
	 * @return
	 */
	
	public List<Register> searchRegistersByContact(Contact contact) {
		return registerDAO.searchRegistersByContact(contact);
	}
	
	
	/**
	 * Transfere para um evento futuro
	 * @param actualDetail
	 * @param futureEvent
	 * @return
	 */
	
	public RegisterDetail transferToFutureEvent(RegisterDetail actualDetail, Event futureEvent) {
		Register actualRegister   = actualDetail.getRegister();
		Account actualAccount     = actualRegister.getAccount();
		Contact actualContact     = actualRegister.getContact();
		MegaEvent futureMegaEvent = futureEvent.getEventWeek().getMegaEvent();
		
		//1.cria a futura inscricao
		Register futureRegister = createFutureRegister(actualContact, futureMegaEvent, actualAccount);
		
		//2.cria o futuro detalhe
		RegisterDetail futureDetail = createFutureRegisterDetail(futureRegister, futureEvent, actualDetail);
		
		//3.remove detail atual
		removeRegisterDetail( actualDetail );
		
		return futureDetail;
	}
	
	
	/**
	 * Cria uma futura inscricao
	 * @param contact
	 * @param futureMegaEvent
	 * @return
	 */
	private Register createFutureRegister(Contact contact, MegaEvent futureMegaEvent, Account actualAccount) {
		Register futureRegister = registerDAO.findRegisterByMegaEventAndContact(futureMegaEvent, contact);
		if (futureRegister==null) {
			futureRegister = new Register();
			futureRegister.setMegaEvent( futureMegaEvent );
			futureRegister.setContact( contact );
			futureRegister.setAccount( actualAccount );
			futureRegister = manager.merge( futureRegister );
		}
		return futureRegister;
	}
	
	
        public RegisterDetail changeEventAndEventWeekFromRegisterDetail(RegisterDetail actualRD, EventWeek eventWeek, Event event, EventPresence eventPresence){
            List<RegisterDetail> searchedRD = registerDAO.findRegisterDetailByEventWeekEventAndEventPresence(actualRD.getRegister(), eventWeek, event, eventPresence);
            if(searchedRD == null || searchedRD.isEmpty()){
                actualRD = manager.merge( actualRD );
                actualRD.setEvent(event);
                actualRD.setEventWeek(eventWeek);
                actualRD.setPresence(eventPresence);
                
                BigDecimal eventValue;
                if(eventPresence == EventPresence.PARTICIPANT){
                    actualRD.setValue(event.getValueParticipant());
                    eventValue = event.getValueParticipant();
                }else{
                    actualRD.setValue(event.getValueStaff());
                    eventValue = event.getValueStaff();
                }
                
                actualRD = manager.merge( actualRD );
                actualRD.setPayments( registerDAO.findPaymentsByRegisterDetail(actualRD) );
                if(eventValue.doubleValue() < actualRD.getCalculatedPaidValue().doubleValue()){
                    List<RegisterCredit> newCredits = new ArrayList<RegisterCredit>();
                    for (RegisterDetailPayment rdp : actualRD.getPayments()) {
                        newCredits.add(this.transformPaymentIntoCredit(rdp));
                    }
                    
                    actualRD.setPayments( registerDAO.findPaymentsByRegisterDetail(actualRD) );
                    
                    for (RegisterCredit registerCredit : newCredits) {
                        try{
                            this.transformCreditIntoPayment(registerCredit, actualRD);
                            actualRD.setPayments( registerDAO.findPaymentsByRegisterDetail(actualRD) );
                            if(actualRD.getCalculatedPendentValue().doubleValue() == 0.00){
                                break;
                            }
                        }catch(Exception ex){
                            break;
                        }
                    }
                }
                
                return actualRD;
            }else{
                throw new ChangeEventAlreadyDoneException();
            }
        }
        
	/**
	 * Cria um futuro detalhe para o futuro evento, cuidando para transferir eventuais pagamentos
	 * @param futureRegister
	 * @param futureEvent
	 * @param actualDetail
	 * @return
	 */
	private RegisterDetail createFutureRegisterDetail(Register futureRegister, Event futureEvent, RegisterDetail actualDetail) {
		//1.cria o detalhe futuro
		RegisterDetail futureDetail = registerDAO.findRegisterDetailByRegisterAndEvent(futureRegister, futureEvent);
		if (futureDetail==null) {
			futureDetail = new RegisterDetail();
			futureDetail.setRegister( futureRegister );
			futureDetail.setEvent( futureEvent );
			futureDetail.setEventWeek( futureEvent.getEventWeek() );
			futureDetail.setValue( actualDetail.getValue() );
			futureDetail.setPresence( actualDetail.getPresence() );
			futureDetail = manager.merge( futureDetail );
		}
		//2.transfere eventuais pagamentos para o futuro detalhe
		transferPaymentsToFutureDetail(actualDetail, futureDetail);
		
		return futureDetail;
	}
	
	/**
	 * Transfere eventuais pagamentos para o evento futuro
	 * @param actualDetail
	 * @param futureDetail
	 */
	private void transferPaymentsToFutureDetail(RegisterDetail actualDetail, RegisterDetail futureDetail) {
		actualDetail = manager.find(RegisterDetail.class, actualDetail.getId() );
		for (RegisterDetailPayment payment : actualDetail.getPayments()) {
			payment.setRegisterDetail( futureDetail );
		}
	}

        
    public List<PaymentCurrency> getPaymentCurrencyList(){
        return manager.createNamedQuery("paymentCurrencyList", PaymentCurrency.class).getResultList();
    }
    
    public PaymentCurrency findPaymentCurrencyById(Long id){
        return manager.find(PaymentCurrency.class, id);
    }


    /**
     * Pesquisa pelos futuros Mega Eventos em relacao ao atual
     * @param actualMegaEvent
     * @return
     */
	public List<MegaEvent> searchFutureMegaEventByActualRegister(Register actualRegister) {
		return manager.createNamedQuery("searchFutureMegaEventByActualRegister", MegaEvent.class)
				.setParameter("pActualMegaEventBeginDate", actualRegister.getMegaEvent().getBeginDate() )
				.setParameter("pContact", actualRegister.getContact() )
				.getResultList();
	}



	/**
	 * Transfere register para mega evento futuro:
	 * 1) transforma todos os pagamentos do regsiter em credito
	 * 2) altera o status do register para TRANSFERIDO
	 * 3) cria novo register no mega evento futuro
	 * @param register
	 * @param futureMegaEvent
	 */
	public void transferToFutureMegaEvent(Register register, MegaEvent futureMegaEvent) {
		//traz para Managed
		register = manager.find(Register.class, register.getId() );
		
		//transforma todos os pagamentos em credito
		for (RegisterDetail detail : register.getDetails()) {
			for (RegisterDetailPayment payment : detail.getPayments()) {
				RegisterCredit credit = new RegisterCredit(payment);
				manager.merge( credit );
			}
		}
		
		//altera status
		register.setStatus( RegisterStatus.TRANSFERED );
		
		//cria novo register
		Register newRegister = new Register(futureMegaEvent, register.getContact(), register.getAccount());
		
		manager.persist( newRegister );
	}


	/**
	 * Cancela uma inscricao e providencia o reembolso (0, 50% ou 100%)
	 * @param register
	 * @param configParamPreviousDaysForHalfRefund
	 * @param configParamPreviousDaysForTotalRefund
	 */
	public void cancelRegister(Register register,
			ConfigParam configParamPreviousDaysForHalfRefund,
			ConfigParam configParamPreviousDaysForTotalRefund,
			RefundStrategy refundStrategy) {

		//1.traz para gerenciado
		register = manager.find(Register.class, register.getId() );
		
		//2.recupera valor ja pago
		BigDecimal paidValue = register.getCalculatedPaidValue();

		//3.realiza o reembolso se tiver pagamento feitos
		if (NumberUtil.isDifferenceThanZero(paidValue) ) {
			
			//se houver reembolso, transforma todos os pagamentos em creditos...
			if (RefundStrategy.REFUND_TOTAL.equals(refundStrategy) || RefundStrategy.REFUND_HALF.equals(refundStrategy) ) {
				
				List<RegisterCredit> credits = new ArrayList<RegisterCredit>();
				for (RegisterDetail detailVar : register.getAllDetails()) {
					for (RegisterDetailPayment paymentVar : detailVar.getPayments()) {
						RegisterCredit transformedCredit = transformPaymentIntoCredit(paymentVar);
						transformedCredit.addNote( refundStrategy.getNoteAboutCancelRegister()  );
						
						credits.add( transformedCredit );
					}
				}
				
				//mas se o reembolso é de 50%, percorre todos os creditos dividindo seu valor por 2
				if (RefundStrategy.REFUND_HALF.equals( refundStrategy )) {
					for (RegisterCredit creditVar : credits) {
						BigDecimal halfValue = NumberUtil.divide( creditVar.getValue(), 2 );
						creditVar.setValue( halfValue );
					}
				}
			}
		}
		
		//4.define novo status
		register.setStatus( RegisterStatus.CANCELED );
	}
	
	
}

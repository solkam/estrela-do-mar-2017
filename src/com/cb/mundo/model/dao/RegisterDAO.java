package com.cb.mundo.model.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.entity.Account;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Event;
import com.cb.mundo.model.entity.EventWeek;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.RegisterDetailPayment;
import com.cb.mundo.model.entity.RegisterDocket;
import com.cb.mundo.model.entity.Transport;
import com.cb.mundo.model.entity.enumeration.EventPresence;
import com.cb.mundo.model.entity.enumeration.MegaEventPaymentMethod;
import com.cb.mundo.model.exception.BusinessException;
import com.cb.mundo.model.util.NumberUtil;

/**
 * DAO para Register, RegisterDetail, pagamentos e creditos
 * (Fruto do Refactor de EventDAO que estava muito grande)
 * 
 * @author Solkam
 * @since 31 MAR 2015
 */
public class RegisterDAO {
	
	private EntityManager manager;

	public RegisterDAO(EntityManager manager) {
		this.manager = manager;
	}
	

	/* ********
	 * register
	 **********/
	
	public void removeRegisterDetailByRegister(Register register) {
		try {
			Query query = manager.createNamedQuery("RegisterDetail.removeByRegister");
			query.setParameter("pRegister", register);
			query.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Register> searchRegistersByContact(Contact contact) {
		return manager.createNamedQuery("Register.searchByContact", Register.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	/**
	 * Pesquisar os registros de mega eventos ativos por contact
	 * @param contact
	 * @return
	 */
	public List<Register> searchActiveRegistersByContact(Contact contact) {
		return manager.createNamedQuery("searchActiveRegistersByContact", Register.class)
				.setParameter("pContact", contact)
				.getResultList();
	}
	
	public List<Register> searchRegistersByMegaEvent(MegaEvent megaEvent) {
		return manager.createNamedQuery("Register.searchByMegaEvent", Register.class)
				.setParameter("pMegaEvent", megaEvent)
				.getResultList();
	}
	
	
	/**
	 * Pesquisa as inscricoes por mega evento e palava-chave.
	 * Usado em Gestao de Inscritos.
	 * o bem parecido com o metodo acima mas sem o status
	 * 
	 * @param megaEvent
	 * @param keyword
	 * @return lista de inscricoes
	 */
	public List<Register> searchRegistersByMegaEventAndKeyword(MegaEvent megaEvent, String keyword) {
		Long registerId = NumberUtil.asLongOrZero(keyword);
		
		return manager.createNamedQuery("Register.searchByMegaEventAndNameOrCivilNameOrEmailOrId", Register.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pName"     , "%"+keyword+"%")
				.setParameter("pCivilName", "%"+keyword+"%")
				.setParameter("pEmail"    , "%"+keyword+"%")
				.setParameter("pId"       , registerId)
				.getResultList();
	}
	
	
	
	/**
	 * Busca Inscricao para um dado mega evento e um email de participante/staff.
	 * Usado no Check-in para novas inscricoes 
	 * @param megaEvent
	 * @param email
	 * @return Register se encontrado ou null
	 */
	public Register findRegisterByMegaEventAndEmail(MegaEvent megaEvent, String email) {
		try {
			return manager.createNamedQuery("Register.findByMegaEventAndEmail", Register.class)
					.setParameter("pMegaEvent", megaEvent)
					.setParameter("pEmail", email)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Busca inscricao pela mega evento e contato
	 * (usando para transferir para eventos futuros)
	 * @param megaEvent
	 * @param contact
	 * @return
	 */
	public Register findRegisterByMegaEventAndContact(MegaEvent megaEvent, Contact contact) {
		try {
			return manager.createNamedQuery("findRegisterByMegaEventAndContact", Register.class)
					.setParameter("pMegaEvent", megaEvent)
					.setParameter("pContact", contact)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
			
		} catch (NonUniqueResultException e) {
			throw new BusinessException("msg_register_duplicated", new Object[] {contact.getFullDesc()} );
		}
	}
	
	/**
	 * Busca um detalhe para uma inscricao e um evento
	 * (usado para transferir para eventos futuros)
	 * @param register
	 * @param event
	 * @return
	 */
	public RegisterDetail findRegisterDetailByRegisterAndEvent(Register register, Event event) {
		try {
			return manager.createNamedQuery("findRegisterDetailByRegisterAndEvent", RegisterDetail.class)
					.setParameter("pRegister", register)
					.setParameter("pEvent", event)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	/* ***************
	 * register detail	
	 *****************/

	public List<RegisterDetail> searchRegisterDetailByEventWeek(EventWeek eventWeek) {
		return manager.createNamedQuery("RegisterDetail.searchByEventWeek", RegisterDetail.class)
				.setParameter("pEventWeek", eventWeek)
				.getResultList();
	}

	
	public List<RegisterDetail> searchRegisterDetailByEvent(Event event) {
		return manager.createNamedQuery("RegisterDetail.searchByEvent", RegisterDetail.class)
				.setParameter("pEvent", event)
				.getResultList();
	}
	
	/**
	 * Pesquisa por detalhes de inscricao via criteria
	 * @param name
	 * @param event
	 * @param paymentMethod
	 * @param saleOrder
	 * @return
	 */
	public List<RegisterDetail> searchRegisterDetailByFilters(
			EventWeek eventWeek, 
			Event event,
			String name) { 

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RegisterDetail> criteria = builder.createQuery(RegisterDetail.class);
		Root<RegisterDetail> root = criteria.from(RegisterDetail.class);
		
		Predicate conjuntion = builder.conjunction();
		//1.event week
		if (eventWeek!=null && eventWeek.getId()!=null) {
			conjuntion = builder.and(conjuntion,
				builder.equal(root.<EventWeek>get("eventWeek"), eventWeek)
			);
		}
		//2.event
		if (event!=null && event.getId()!=null) {
			conjuntion = builder.and(conjuntion,
				builder.equal(root.<Event>get("event"), event)
			);
		}
		//3.nome (novo ou civil)
		if (name!=null && !name.trim().isEmpty()) {
			Predicate disjunction = builder.disjunction();
			//nome novo
			disjunction = builder.or(disjunction,
				builder.like(root.<Register>get("register").<Contact>get("contact").<String>get("name"), name+"%")
			);
			//nome civil
			disjunction = builder.or(disjunction,
				builder.like(root.<Register>get("register").<Contact>get("contact").<String>get("civilName"), name+"%")
			);
			conjuntion = builder.and(disjunction);
		}
		
		criteria.where(conjuntion);
		return manager.createQuery(criteria).getResultList();
	}
	
	
	/**
	 * Busca de maneira dinamica (criteria) todos register-details.
	 * @param megaEvent
	 * @param eventWeek
	 * @param event
	 * @return
	 */
	public List<RegisterDetail> searchRegisterDetailByMegaEventAndEventWeekAndEvent(MegaEvent megaEvent, EventWeek eventWeek, Event event, List<EventPresence> presences) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RegisterDetail> criteria = builder.createQuery(RegisterDetail.class);
		Root<RegisterDetail> root = criteria.from(RegisterDetail.class);
		
		Predicate conjuction = builder.conjunction();
		//mega event:
		conjuction = builder.and(conjuction,
				builder.equal(root.<Register>get("register").<MegaEvent>get("megaEvent"), megaEvent)
			);
		//event week:
		if (eventWeek!=null && eventWeek.getId()!=null) {
			conjuction = builder.and(conjuction,
					builder.equal(root.<EventWeek>get("eventWeek"), eventWeek)
			);
		}
		//event:
		if (event!=null && event.getId()!=null) {
			conjuction = builder.and(conjuction,
				builder.equal(root.<Event>get("event"), event)
			);
		}
		if (!presences.isEmpty()) {
			conjuction = builder.and(conjuction,
				root.<EventPresence>get("presence").in(presences)
			);
		}
		
		criteria.where(conjuction);
		criteria.orderBy( builder.asc(root.<Register>get("register").<Contact>get("contact").<String>get("civilName") ));
		
		return manager.createQuery(criteria).getResultList();
	}
	
	
	
	/* ***********************
	 * Register Detail Payment
	 *************************/
	public RegisterDetailPayment saveRegisterRegisterPayment(RegisterDetailPayment payment) {
		return manager.merge( payment );
	}


	/**
	 * Pesquisar por pagamento via criteria
	 * 
	 * @param name
	 * @param selectedEvent
	 * @param paymentMethod
	 * @param saleOrder
	 * @return
	 */
	public List<RegisterDetailPayment> searchRegisterDetailPaymentByFilter(
			String name, 
			Event event, 
			MegaEventPaymentMethod paymentMethod,
			String saleOrder) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<RegisterDetailPayment> criteria = builder.createQuery(RegisterDetailPayment.class);
		Root<RegisterDetailPayment> root = criteria.from(RegisterDetailPayment.class);
		
		Predicate conjuntion = builder.conjunction();
		//1.nome (novo ou civil)
		if (name!=null && !name.trim().isEmpty()) {
			Predicate disjunction = builder.disjunction();
			//nome novo
			disjunction = builder.or(disjunction,
				builder.like(root.<RegisterDetail>get("registerDetail").<Register>get("register").<Contact>get("contact").<String>get("name"), name+"%")
			);
			//nome civil
			disjunction = builder.or(disjunction,
				builder.like(root.<RegisterDetail>get("registerDetail").<Register>get("register").<Contact>get("contact").<String>get("civilName"), name+"%")
			);
			conjuntion = builder.and(disjunction);
		}
		//2.event
		if (event!=null) {
			conjuntion = builder.and(conjuntion,
				builder.equal(root.<RegisterDetail>get("registerDetail").<Event>get("event"), event)
			);
		}
		//3.meio de pagamnto
		if (paymentMethod !=null) {
			conjuntion = builder.and(conjuntion,
				builder.equal(root.<MegaEventPaymentMethod>get("method"), paymentMethod)
			);
		}
		//4.ordem de venda
		if (saleOrder!=null && !saleOrder.trim().isEmpty()) {
			conjuntion = builder.and(conjuntion,
				builder.equal(root.<String>get("saleOrder"), saleOrder)
			);
		}
		
		criteria.where(conjuntion);
		return manager.createQuery(criteria).getResultList();
	}
	

	

	/* ***************
	 * register docket	
	 *****************/
	/**
	 * Busca por um recibo de inscricao, devolvendo null se nao encontrar
	 * @param register
	 * @return
	 */
	public RegisterDocket findRegisterDocketByRegister(Register register) {
		RegisterDocket docket = null;
		try {
			docket = manager.createNamedQuery("RegisterDocket.findByRegister", RegisterDocket.class)
						.setParameter("pRegister", register)
						.getSingleResult();
		}catch (NoResultException e) {
			//nao precisa fazer nada
		}
		return docket;
	}



	/* *******
	 * Account 
	 *********/
	public Account findAccountByContact(Contact contact) {
		try {
			return manager.createNamedQuery("findAccountByContact", Account.class)
					.setParameter("pContact", contact)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}


	/* *********
	 * Transport	
	 ***********/
	public List<Transport> searchTransportByRegister(Register register) {
		return manager.createNamedQuery("searchTransportByRegister", Transport.class)
				.setParameter("pRegister", register)
				.getResultList();
	}

        public List<RegisterDetail> findRegisterDetailByEventWeekEventAndEventPresence(Register reg, EventWeek eventWeek, Event event, EventPresence eventPresence) {
                return manager.createNamedQuery("RegisterDetail.searchByEventWeekEventAndEventPresence", RegisterDetail.class)
                                .setParameter("pRegister", reg)
                                .setParameter("pEventWeek", eventWeek)
                                .setParameter("pEvent", event)
                                .setParameter("pPresence", eventPresence)
                                .getResultList();
        }
        
        public List<RegisterDetailPayment> findPaymentsByRegisterDetail(RegisterDetail regDet){
            return manager.createNamedQuery("RegisterDetailPayment.searchPaymentsByRegisterDetail", RegisterDetailPayment.class)
                    .setParameter("pRegisterDetail", regDet)
                    .getResultList();
        }
	

}

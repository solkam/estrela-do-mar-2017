package com.cb.mundo.model.service;

import static com.cb.mundo.model.util.QueryUtil.toLikeMatchModeANY;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.cb.mundo.model.dto.ReportHostingByOccupantDTO;
import com.cb.mundo.model.dto.ReportHostingByRoomDTO;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Hosting;
import com.cb.mundo.model.entity.HostingConfirmed;
import com.cb.mundo.model.entity.HostingSugested;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.exception.RegisterAlreadyConfirmedHostingException;
import com.cb.mundo.model.exception.RoomNumberAlreadyUsedException;
import com.cb.mundo.model.util.QueryUtil;

/**
 * EJB para os servicos de hosting.
 * 
 * @author Solkam
 * @since 21 ABR 2014
 */
@Stateless
public class HostingService {
	
	@PersistenceContext	EntityManager manager;
	
	@EJB RegisterService registerService;
	

	/* *******
	 * Hosting	
	 *********/
	public Hosting saveHosting(Hosting hosting, UserCB autenticateUser) {
		verifyIfRoomNumberAlreadyUsed(hosting);
		
		addLogs(hosting, autenticateUser);
		
		hosting = manager.merge( hosting );
		
		return hosting;
	}
	
	
	private void addLogs(Hosting hosting, UserCB autenticateUser) {
		String loginName = autenticateUser.getContact().getShortDesc();
		if (hosting.isTransient()) {
			hosting.setCreateDate( new Date() );
			hosting.setCreatedBy( loginName  );
		} else {
			hosting.setUpdateDate( new Date() );
			hosting.setUpdatedBy( loginName );
		}
	}

	/**
	 * Remover (se estiver transiente) o hosting
	 * @param hosting
	 */
	public void removeHosting(Hosting hosting) {
		//verifica se ele for persistente...
		if (!hosting.isTransient()) {
			//... entao traz para gerenciado
			hosting = manager.find(Hosting.class, hosting.getId() );
			//e remove
			manager.remove( hosting );
		}
	}
	
	
	/**
	 * Instancia um novo hosting a partir de um register selecionado.
	 * @param register
	 * @return
	 */
	public Hosting saveHostingFromMegaEvent(MegaEvent me) {
		Hosting newHosting = new Hosting(me);
		newHosting = manager.merge( newHosting );
		
		return newHosting;
	}

	
	
	private void verifyIfRoomNumberAlreadyUsed(Hosting hosting) {
		MegaEvent me         = hosting.getMegaEvent();
		String hostingArea   = hosting.getHostingArea();
		String hostingNumber = hosting.getHostingNumber();
		
		Hosting hostingFound = findHostingByMegaEventAndAreaAndNumber(me, hostingArea, hostingNumber);
		if (hostingFound!=null && !hostingFound.equals(hosting)) {
			throw new RoomNumberAlreadyUsedException();
		}
	
	}


	private Hosting findHostingByMegaEventAndAreaAndNumber(MegaEvent me, String area, String number) {
		try {
			return manager.createNamedQuery("findHostingByMegaEventAndAreaAndNumber", Hosting.class)
					.setParameter("pMegaEvent"    , me)
					.setParameter("pHostingArea"  , area)
					.setParameter("pHostingNumber", number)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}

	
	//trio dinamico
	
	/**
	 * Pesquisar pelos hosting sugeridos mas nao confirmados pendente e carrega os sugeridos
	 * @param me
	 * @return
	 */
	public List<Hosting> searchHostingNotConfirmedButWithSugestedsByMegaEvent(MegaEvent me) {
		return manager.createNamedQuery("searchHostingNotConfirmedButWithSugestedsByMegaEvent", Hosting.class)
				.setParameter("pMegaEvent", me)
				.getResultList();
	}
	
	/**
	 * Pesquisa os register que nao tem nem sugeridos nem confirmados
	 * @param me
	 * @return
	 */
	public List<Register> searchRegisterWithoutHostingByMegaEvent(MegaEvent me) {
		List<Register> registers = manager.createNamedQuery("searchRegisterWithoutHostingByMegaEvent", Register.class)
				.setParameter("pMegaEvent", me)
				.getResultList();
		
		//eager load dos eventos
		for (Register register : registers) {
			register.getAllDetails();
		}
		
		return registers;
	}
	
	
	/**
	 * Pesquisar pelos Hostings ja configurados (ou seja, que tem HostingConfirmed).
	 * Faz o eager loading dos eventos de cadas inscricao
	 * @param me
	 * @return
	 */
	public List<Hosting> searchHostingWithConfirmedsByMegaEvent(MegaEvent me) {
		List<Hosting> confirmedHostings = manager.createNamedQuery("searchHostingWithConfirmedsByMegaEvent", Hosting.class)
				.setParameter("pMegaEvent", me)
				.getResultList();

		//eager load dos eventos
		for (Hosting hosting : confirmedHostings) {
			for (HostingConfirmed confirmedHosting : hosting.getConfirmeds()) {
				confirmedHosting.getRegister().getAllDetails();
			}
		}
		
		return confirmedHostings;
	}
	

	
	
	/**
	 * Busca hosting pela inscricao e opcionalmente forca a carga
	 * (invocado na inscricao - nova ou revisao)
	 * @param contact
	 * @return hosting cujo contact foi sugerido
	 */
	public Hosting findHostingBySugestedContact(Contact contact, boolean flagEagerLoad) {
		try {
			Hosting hostingFound = manager.createNamedQuery("findHostingBySugestedContact", Hosting.class)
					.setParameter("pContact", contact)
					.getSingleResult();
			
			if (flagEagerLoad) {
				return refreshHosting(hostingFound);
			} else {
				return hostingFound;
			}
		} catch (NoResultException e) {
			return null;
		}
	}
	

	public Hosting refreshHosting(Hosting hosting) {
		hosting = manager.find(Hosting.class, hosting.getId() );
		//1.load segesteds
		hosting.getSugesteds().size();
		
		//2.load confirmed
		if (hosting.getConfirmeds()!=null) {
			for (HostingConfirmed confirmed : hosting.getConfirmeds()) {
				//3.events
				confirmed.getRegister().getAllDetails();
			}
		}
		return hosting;
	}
	
	
	
	
		
	/* *****************
	 * Hosting Confirmed
	 *******************/
	public HostingConfirmed saveHostingConfirmed(Register register, Hosting hosting) {
		HostingConfirmed confirmed = verifyThenSaveHostingConfirmed(register, hosting);
		if (confirmed==null) {//tem um erro 
			throw new RegisterAlreadyConfirmedHostingException();
		}
		return confirmed;
	}
	
	
	
	/**
	 * A ideia aqui eh percorrer todos os sugeridos, 
	 * buscar o register pelo contact e mega evento e 
	 * adicionar como confirmado
	 * @param hosting
	 */
	public Hosting convertHostingSugestedsToConfirmeds(Hosting hosting, MegaEvent me) {
		hosting = manager.find(Hosting.class, hosting.getId() );
		
		for (HostingSugested sugested : hosting.getSugesteds()) {
			Register registerFound = registerService.findRegisterByMegaEventAndContact(me, sugested.getContact());
			
			verifyThenSaveHostingConfirmed(registerFound, hosting);//testa internamente se o register eh null
		}
		
		//forca o refresh do hosting
		hosting = refreshHosting(hosting);
		
		return hosting;
	}

	
	
	public void removeHostingConfirmed(HostingConfirmed confirmed) {
		confirmed = manager.find(HostingConfirmed.class, confirmed.getId() );
		manager.remove( confirmed );
	}

	
	public HostingConfirmed findHostingConfirmedByRegister(Register register) {
		try {
			return manager.createNamedQuery("searchHostingConfirmedByRegister", HostingConfirmed.class)
					.setParameter("pRegister", register)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	

	//RNs..
	
	/**
	 * Para salvar um HostingConfirmed a partir de register e hosting
	 * eh necessario verificar se o register ja tem quarto confirmado
	 * @param register
	 * @param hosting
	 */
	private HostingConfirmed verifyThenSaveHostingConfirmed(Register register, Hosting hosting) {
		if (register==null || register.isTransient()) {
			return null;
		}
		//RN
		if (hasRegisterAHostingConfirmed(register)) {
			return null;
		}
		//se chegou aqui, salva
		HostingConfirmed hostingConfirmed = new HostingConfirmed(hosting, register);
		return manager.merge( hostingConfirmed );
	}
	
	
	/**
	 * RN: verifica se inscricao ja foi confirmado em outro quarto
	 * @param register
	 */
	private boolean hasRegisterAHostingConfirmed(Register register) {
		HostingConfirmed confirmed = findHostingConfirmedByRegister(register);
		if (confirmed!=null) {
			return true;
		} else {
			return false;
		}
	}

	
	
	/**
	 * Busca register pelo MegaEvento e pelos fragmentos nome novo, nome civil e cidade 
	 * (para autocomplete)
	 * @param megaEvent
	 * @param contactName
	 * @param contactCivilName
	 * @param contactCity
	 * @return
	 */
	public List<Register> searchRegisterByMegaEventContactNameOrCivilNameOrCity(MegaEvent megaEvent, String contactName, String contactCivilName, String contactCity) {
		List<Register> registers = manager.createNamedQuery("searchRegisterByMegaEventContactNameOrCivilNameOrCity", Register.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pContactName",      toLikeMatchModeANY(contactName))
				.setParameter("pContactCivilName", toLikeMatchModeANY(contactCivilName))
				.setParameter("pContactCity",      toLikeMatchModeANY(contactCity))
				.getResultList();
		
		return registers;
	}

	
	
	/* ******
	 * Report	
	 ********/
	/**
	 * Pesquisa para o relatario de quarto usando um helper.
	 * @param megaEvent
	 * @param register
	 * @return
	 */
	public List<ReportHostingByRoomDTO> searchReportHostingHelperByMegaEventAndRegister(MegaEvent megaEvent, Register register) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Hosting> criteria = builder.createQuery(Hosting.class);
		Root<Hosting> root = criteria.from(Hosting.class);
		
		Predicate conjunction = builder.conjunction();
		//1.mega event
		if (QueryUtil.isNotNull(megaEvent)) {
			conjunction = builder.and(conjunction, 
					builder.equal(root.<MegaEvent>get("megaEvent"), megaEvent)
				);
		}
		//2.register
		if (QueryUtil.isNotNull(register)) {
			conjunction = builder.and(conjunction
					,builder.equal(root.join("confirmeds", JoinType.INNER).<Register>get("register"), register)
				);
		}

		criteria.orderBy( builder.asc(root.<String>get("hostingArea")) );
		criteria.orderBy( builder.asc(root.<String>get("hostingNumber")) );
		
		criteria.where( conjunction );
		List<Hosting> hostings = manager.createQuery(criteria).getResultList();
		
		return buildReportHostingHelper(hostings);
	}
	

	/**
	 * Faz um pivot table com os confirmados em cada quarto
	 * @param hostings
	 * @return
	 */
	private List<ReportHostingByRoomDTO> buildReportHostingHelper(List<Hosting> hostings) {
		List<ReportHostingByRoomDTO> helpers = new ArrayList<>();
		
		for (Hosting hosting : hostings) {
			Contact contact0 = null;
			Contact contact1 = null;
			Contact contact2 = null;
			Contact contact3 = null;
			Contact contact4 = null;
			Contact contact5 = null;
			
			int i=0;
			for (HostingConfirmed confirmedVar : hosting.getConfirmeds()) {
				if (i==0) {
					contact0 = confirmedVar.getRegister().getContact();
				}
				if (i==1) {
					contact1 = confirmedVar.getRegister().getContact();
				}
				if (i==2) {
					contact2 = confirmedVar.getRegister().getContact();
				}
				if (i==3) {
					contact3 = confirmedVar.getRegister().getContact();
				}
				if (i==4) {
					contact4 = confirmedVar.getRegister().getContact();
				}
				if (i==5) {
					contact5 = confirmedVar.getRegister().getContact();
				}
				i++;
			}
			
			ReportHostingByRoomDTO helper = new ReportHostingByRoomDTO(hosting, contact0, contact1, contact2, contact3, contact4, contact5);
			helpers.add( helper );
		}
		return helpers;
	}
	
	
	
	/**
	 * Pesquisa para Relatorio de Quartos pelo mega evento
	 * @param megaEvent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReportHostingByOccupantDTO> searchReportHostingByOccupantDtosByMegaEvent(MegaEvent megaEvent) {
		return manager.createNamedQuery("searchReportHostingByOccupantDtosByMegaEvent")
				.setParameter("pMegaEvent", megaEvent)
				.getResultList();
	}
	
	

}

package com.cb.mundo.model.service;

import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.Transport;
import com.cb.mundo.model.entity.TransportClassification;
import com.cb.mundo.model.entity.enumeration.TransportDirection;
import com.cb.mundo.model.exception.TransportHasPassengersException;


/**
 * EJB para operacoes sobre Tranporte
 * 
 * @author Solkam
 * @since 11 OUT 2013
 */
@Stateless
public class TransportService {
	
	@PersistenceContext
	private EntityManager manager;

	
	
	public Transport saveTransport(Transport transport) {
		return manager.merge( transport );
	}
	
	
	public void removeTransport(Transport transport) {
		transport = manager.merge(transport);
		
		verifyTransportHasPassengers(transport);
		
		manager.remove( transport );
	}
	
	/**
	 * RN para verificar se transporte ja tem passeiros e, se sim, 
	 * lanca exeception.
	 * @param transport
	 */
	private void verifyTransportHasPassengers(Transport transport) {
		if (!transport.getRegisters().isEmpty()) {
			throw new TransportHasPassengersException(transport);
		}
	}
	
	/**
	 * Pesquisa pelos transporte de um megaevento forcando a carga dos passageiros
	 */
	
	public List<Transport> searchTransportByMegaEventAndDirection(MegaEvent megaEvent, TransportDirection direction) {
		List<Transport> transports = manager.createNamedQuery("searchTransportByMegaEventAndDirection", Transport.class)
				.setParameter("pMegaEvent", megaEvent)
				.setParameter("pDirection", direction)
				.getResultList();
		
		//forcando a carga dos passageiros
		for (Transport t : transports) {
			t.getRegisters().size();
		}
		
		return transports;
	}
	
	
	
	public List<Register> searchRegisterWithoutTransportByMegaEventAndDirection(MegaEvent megaEvent, TransportDirection direction) {
		return manager.createNamedQuery("searchRegisterWithoutTransportByMegaEventAndDirection", Register.class)
				.setParameter("pMegaEvent", megaEvent )
				.setParameter("pDirection", direction )
				.setParameter("pStatusArray", Arrays.asList( direction.getStatusArray() ))
				.getResultList();
	}

	
	public Transport refreshTransport(Transport transport) {
		//1.traz para Gerenciado
		transport = manager.find(Transport.class, transport.getId() );
		//2.forca carga de registers
		transport.getRegisters().size();
		
		return transport;
	}
	
	
	public List<TransportClassification> listAllTransportClass() {
		return manager.createNamedQuery("listAllTransportClassification", TransportClassification.class)
				.getResultList();
	}
	

}

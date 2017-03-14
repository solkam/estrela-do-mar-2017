package com.cb.mundo.model.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.entity.Feedback;
import com.cb.mundo.model.entity.Production;

/**
 * Servicos de Feedback
 * 
 * @author Solkam
 * @since 30 NOV 2014
 */
@Stateless
public class FeedbackService {
	
	@PersistenceContext EntityManager manager;
	
	
	/**
	 * Salva o feedback
	 * @param f
	 * @return
	 */
	public Feedback saveFeedback(Feedback f) {
		return manager.merge( f );
	}


	/**
	 * Busca o feedback da producao
	 * @param production
	 * @return null se nao existir
	 */
	public Feedback findFeedbackByProduction(Production production) {
		try {
			return manager.createNamedQuery("findFeedbackByProduction", Feedback.class)
					.setParameter("pProduction", production)
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	
	
	

}

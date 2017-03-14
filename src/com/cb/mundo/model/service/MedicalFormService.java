package com.cb.mundo.model.service;

import static com.cb.mundo.model.util.QueryUtil.toLikeMatchModeANY;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.MedicalQuestion;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.exception.MedicalQuestionHasAnswerException;

/**
 * EJB responsavel pelo Formulario Medico
 * @author Solkam
 * @since 20 ABR 2012
 */
@Stateless
public class MedicalFormService  {
	
	@PersistenceContext EntityManager manager;
	

	/* QUESTAO 
	 *********/
	
	/**
	 * Salva uma question do formulario medico
	 * @param question
	 * @return
	 */
	public MedicalQuestion saveMedicalQuestion(MedicalQuestion question) {
		return manager.merge( question );
	}
	
	
	/**
	 * Remove uma questao aplicando RN
	 * @param question
	 */
	public void removeMedicalQuestion(MedicalQuestion question) {
		verifyIfQuestionHasAnswer(question);
		manager.remove( manager.merge(question) );
	}

	/**
	 * RN para verifica se uma questao tem resposta associadas,
	 * lancando exception caso tenha
	 * @param question
	 */
	private void verifyIfQuestionHasAnswer(MedicalQuestion question) {
		List<MedicalAnswer> answers = searchMedicalAnswerByQuestion(question);
		if (!answers.isEmpty()) {
			throw new MedicalQuestionHasAnswerException(question);
		}
	}

	
	/**
	 * Pesquisa todas as questoes
	 * @return
	 */
	public List<MedicalQuestion> searchMedicalQuestion() {
		return manager.createNamedQuery("searchMedicalQuestion", MedicalQuestion.class)
				.getResultList();
	}

	
	/**
	 * Pesquisa somente as questoes ativas
	 * @return
	 */
	public List<MedicalQuestion> searchActiveMedicalQuestion() {
		return manager.createNamedQuery("searchActiveMedicalQuestion", MedicalQuestion.class)
				.getResultList();
	}
	

	
	/* RESPOSTA 
	 **********/
	
	/**
	 * Salva uma resposta do formulario medico
	 * @param answer
	 * @return
	 */
	public MedicalAnswer saveMedicalAnswer(MedicalAnswer answer) {
		return manager.merge( answer );
	}
	
	/**
	 * Salva uma lista de respostas
	 * @param asnwers
	 */
	public void saveMedicalAnswers(List<MedicalAnswer> asnwers) {
		for (MedicalAnswer ma : asnwers) {
			manager.merge( ma );
		}
	}


	/**
	 * Busca pela respostas medicas de uma inscricao.
	 * Caso nao exista respostas, monta uma lista de respostas vazias
	 */
	public List<MedicalAnswer> searchOrCreateMedicalAnswerByRegister(Register register) {
		List<MedicalAnswer> answers = searchMedicalAnswerByRegister(register);
		
		if (answers.isEmpty()) {
			List<MedicalQuestion> questions = searchActiveMedicalQuestion();
			for (MedicalQuestion question : questions) {
				MedicalAnswer answer = new MedicalAnswer(register, question);
				answers.add( answer );
			}
		}
		return answers;
	}
	
	/**
	 * Retorna a lista de respostas para uma inscricao.
	 * Mas somente se a inscricao for valida 
	 * @param register
	 * @return lista de resposta de uma inscricao ou uma lista vazia
	 */
	public List<MedicalAnswer> searchMedicalAnswerByRegister(Register register) {
		if (register.isTransient()) {
			return new ArrayList<MedicalAnswer>();
			
		} else {
			return manager.createNamedQuery("searchMedicalAnswerByRegister", MedicalAnswer.class)
					.setParameter("pRegister", register)
					.getResultList();
		}
	}
	

	/**
	 * Remove as respostas de uma inscricao
	 * @param register
	 */
	public void removeMedicalAnswerByRegister(Register register) {
		List<MedicalAnswer> answers = searchMedicalAnswerByRegister(register);
		for (MedicalAnswer a : answers) {
			manager.remove( a );
		}
	}

	/**
	 * Pesquisa as respostas de uma questao
	 * @param question
	 * @return
	 */
	public List<MedicalAnswer> searchMedicalAnswerByQuestion(MedicalQuestion question) {
		return manager.createNamedQuery("searchMedicalAnswerByQuestion", MedicalAnswer.class)
				.setParameter("pQuestion", question)
				.getResultList();
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
	
	


}

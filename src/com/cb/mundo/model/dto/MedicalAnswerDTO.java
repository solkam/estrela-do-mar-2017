package com.cb.mundo.model.dto;

import java.util.List;

import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.Register;

/**
 * DTO para o relatorio de fichas medicas
 * @author Solkam
 * @since 27 ABR 2015
 */
public class MedicalAnswerDTO {
	
	private final Register register;
	
	private final List<MedicalAnswer> answers;


	//construtor
	public MedicalAnswerDTO(Register register, List<MedicalAnswer> answers) {
		this.register = register;
		this.answers = answers;
	}

	
	//acessores...
	public Register getRegister() {
		return register;
	}
	public List<MedicalAnswer> getAnswers() {
		return answers;
	}
	
	
	//runtime
	
	/**
	 * Verifica se pelo menos uma resposta tem limitacao fisica
	 * @return
	 */
	public Boolean getFlagPhysicalLimitation() {
		for (MedicalAnswer answerVar : answers) {
			if (answerVar.getBooleanAnswer()) {
				return true;
			}
		}
		return false;
	}
	
}

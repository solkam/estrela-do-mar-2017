package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.MedicalQuestion;

/**
 * Exception que nao permite uma questao seja removida se existir
 * respostas para ela.
 * 
 * @author Solkam
 * @since 19 OUT 2013
 */
public class MedicalQuestionHasAnswerException extends BusinessException {
	private static final long serialVersionUID = 7910378532079067446L;

	public MedicalQuestionHasAnswerException(MedicalQuestion question) {
		super("msg_medical_question_has_answers", null);
	}
	
}

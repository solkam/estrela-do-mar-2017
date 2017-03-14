package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.MedicalQuestion;
import com.cb.mundo.model.service.MedicalFormService;

@ManagedBean(name="configMedicalFormBean")
@ViewScoped
public class ConfigMedicalFormBean implements Serializable {

	@EJB MedicalFormService medicalFormService;
	
	private List<MedicalQuestion> questions;
	
	private MedicalQuestion question;
	
	@PostConstruct void init() {
		populateQuestions();
		populateQuestion();
	}
	
	private void populateQuestions() {
		questions = medicalFormService.searchMedicalQuestion();
	}

	private void populateQuestion() {
		question = new MedicalQuestion();
	}
	
	
//actions...
	
	public void saveQuestion() {
		try {
			medicalFormService.saveMedicalQuestion(question);
			populateQuestions();
			populateQuestion();
			JSFUtil.addInfoMessage("msg_medical_question_save_sucess");
			
		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
	public void saveQuestions() {
		try {
			for (MedicalQuestion q : questions) {
				q = medicalFormService.saveMedicalQuestion(q);
			}
			populateQuestions();
			JSFUtil.addInfoMessage("msg_medical_questions_save_sucess");

		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
	public void removeQuestion(MedicalQuestion question) {
		try {
			medicalFormService.removeMedicalQuestion(question);
			populateQuestions();
			JSFUtil.addInfoMessage("msg_medical_question_remove_sucess");

		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}

	
//acessores...
	private static final long serialVersionUID = 7703042105236553495L;

	public List<MedicalQuestion> getQuestions() {
		return questions;
	}
	public void setQuestions(List<MedicalQuestion> questions) {
		this.questions = questions;
	}
	public MedicalQuestion getQuestion() {
		return question;
	}
	public void setQuestion(MedicalQuestion question) {
		this.question = question;
	}

}

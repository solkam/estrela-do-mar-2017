package com.cb.mundo.model.dto;

import java.util.List;

import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.enumeration.Gender;

/**
 * Resumo sobre pessoas para Informe 
 * de Pessoas por Evento
 * 
 * @author Solkam
 * @since 25 nov 2012
 */
public class ReportPeopleResume {
	
	//gender
	private Integer totalMale = 0;
	private Integer totalFemale = 0;
	
	//presence
	private Integer totalStaff = 0;
	private Integer totalParticipant = 0;
	private Integer totalContrated = 0;
	private Integer totalDependent = 0;
	
	//maturity
	private Integer totalChild = 0;
	private Integer totalTeenager = 0;
	private Integer totalYoung = 0;
	private Integer totalAdult = 0;
	private Integer totalAncient = 0;
	private Integer totalMaturityUnknow = 0;
	
	//status
	private Integer totalIncomplete = 0;
	private Integer totalRegistered = 0;
	private Integer totalCheckedinVirtual = 0;
	private Integer totalCheckedin = 0;
	private Integer totalCheckedout = 0;
	private Integer totalMegaeventOut = 0;
	private Integer totalCanceled = 0;
	private Integer totalExpired = 0;
	private Integer totalTransfered = 0;



	
	//t-shirts size
	private Integer totalMaleGG=0;
	private Integer totalMaleG=0;
	private Integer totalMaleM=0;
	private Integer totalMaleP=0;
	private Integer totalFemaleGG=0;
	private Integer totalFemaleG=0;
	private Integer totalFemaleM=0;
	private Integer totalFemaleP=0;
	
	
	
	public ReportPeopleResume(List<RegisterDetail> registerDetails) {
		initByRegisterDetails(registerDetails);
	}
	
	public ReportPeopleResume(List<Register> registers, int dummy) {
		initByRegisters(registers);
	}
	
	
	private void initByRegisters(List<Register> registers) {
		for (Register register : registers) {
			//1.gender
			handleGender(register.getContact());
			//2.maturity
			handleMaturity(register.getContact());
			//3.status
			handleRegisterStatus(register);
		}
		
	}

	private void initByRegisterDetails(List<RegisterDetail> registerDetails) {
		for (RegisterDetail detail : registerDetails) {
			//1.gender
			handleGender(detail.getRegister().getContact());
			//2.presence
			handlePresence(detail);
			//3.maturity
			handleMaturity(detail.getRegister().getContact());
			//4.status
			handleRegisterStatus(detail.getRegister());
			//5.t-shirt
			handleTShirtSize(detail.getRegister().getContact());
		}
	}




	private void handleRegisterStatus(Register register) {
		switch (register.getStatus()) {
		case INCOMPLETE:
			totalIncomplete++;
			break;
		case REGISTERED:
			totalRegistered++;
			break;
		case CHECKEDIN:
			totalCheckedin++;
			break;
		case CHECKEDOUT:
			totalCheckedout++;
			break;
		case MEGAEVENTOUT:
			totalMegaeventOut++;
			break;
		case CANCELED:
			totalCanceled++;
			break;
//		case CHECKEDIN_VIRTUAL:
//			totalCheckedinVirtual++;
//			break;
		case EXPIRED:
			totalExpired++;
			break;
		case TRANSFERED:
			totalTransfered++;
			break;
		}
	}



	private void handleMaturity(Contact contact) {
		switch (contact.getMaturity()) {
		case CHILD:
			totalChild++;
			break;
		case TEENAGER:
			totalTeenager++;
			break;
		case YOUNG:
			totalYoung++;
			break;
		case ADULT:
			totalAdult++;
			break;
		case ANCIENT:
			totalAncient++;
			break;
		default:
			totalMaturityUnknow++;
		}
	}



	private void handlePresence(RegisterDetail detail) {
		switch (detail.getPresence()) {
		case PARTICIPANT:
			totalParticipant++;
			break;
		case STAFF:
			totalStaff++;
			break;
		case CONTRATED:
			totalContrated++;
			break;
		case DEPENDENT:
			totalDependent++;
			break;
		}
	}



	private void handleGender(Contact contact) {
		if (Gender.M.equals( contact.getGender() )) {
			totalMale++;
		} else {
			totalFemale++;
		}
	}

	
	private void handleTShirtSize(Contact contact) {
		if (Gender.M.equals( contact.getGender() )) {//Male
			if (contact.getTshirtSize()!=null) {
				switch(contact.getTshirtSize()) {
				case GG:
					totalMaleGG++;
					break;
				case G:
					totalMaleG++;
					break;
				case M:
					totalMaleM++;
					break;
				case P:
					totalMaleP++;
					break;
				}
			}
		
		} else {//Female
			if (contact.getTshirtSize()!=null) {
				switch(contact.getTshirtSize()) {
				case GG:
					totalFemaleGG++;
					break;
				case G:
					totalFemaleG++;
					break;
				case M:
					totalFemaleM++;
					break;
				case P:
					totalFemaleP++;
					break;
				}
			}
		}
	}


	
	
//acessores...	
	public Integer getTotalMale() {
		return totalMale;
	}
	public void setTotalMale(Integer totalMale) {
		this.totalMale = totalMale;
	}
	public Integer getTotalFemale() {
		return totalFemale;
	}
	public void setTotalFemale(Integer totalFemale) {
		this.totalFemale = totalFemale;
	}
	public Integer getTotalStaff() {
		return totalStaff;
	}
	public void setTotalStaff(Integer totalStaff) {
		this.totalStaff = totalStaff;
	}
	public Integer getTotalParticipant() {
		return totalParticipant;
	}
	public void setTotalParticipant(Integer totalParticipant) {
		this.totalParticipant = totalParticipant;
	}

	public Integer getTotalMaturityUnknow() {
		return totalMaturityUnknow;
	}
	public void setTotalMaturityUnknow(Integer totalMaturityUnknow) {
		this.totalMaturityUnknow = totalMaturityUnknow;
	}
	public Integer getTotalContrated() {
		return totalContrated;
	}
	public void setTotalContrated(Integer totalContrated) {
		this.totalContrated = totalContrated;
	}
	public Integer getTotalDependent() {
		return totalDependent;
	}
	public void setTotalDependent(Integer totalDependent) {
		this.totalDependent = totalDependent;
	}
	public Integer getTotalChild() {
		return totalChild;
	}
	public void setTotalChild(Integer totalChild) {
		this.totalChild = totalChild;
	}
	public Integer getTotalTeenager() {
		return totalTeenager;
	}
	public void setTotalTeenager(Integer totalTeenager) {
		this.totalTeenager = totalTeenager;
	}
	public Integer getTotalYoung() {
		return totalYoung;
	}
	public void setTotalYoung(Integer totalYoung) {
		this.totalYoung = totalYoung;
	}
	public Integer getTotalAdult() {
		return totalAdult;
	}
	public void setTotalAdult(Integer totalAdult) {
		this.totalAdult = totalAdult;
	}
	public Integer getTotalAncient() {
		return totalAncient;
	}
	public void setTotalAncient(Integer totalAncient) {
		this.totalAncient = totalAncient;
	}
	public Integer getTotalIncomplete() {
		return totalIncomplete;
	}
	public void setTotalIncomplete(Integer totalIncomplete) {
		this.totalIncomplete = totalIncomplete;
	}
	public Integer getTotalRegistered() {
		return totalRegistered;
	}
	public void setTotalRegistered(Integer totalRegistered) {
		this.totalRegistered = totalRegistered;
	}
	public Integer getTotalCheckedin() {
		return totalCheckedin;
	}
	public void setTotalCheckedin(Integer totalCheckedin) {
		this.totalCheckedin = totalCheckedin;
	}
	public Integer getTotalCheckedout() {
		return totalCheckedout;
	}
	public void setTotalCheckedout(Integer totalCheckedout) {
		this.totalCheckedout = totalCheckedout;
	}
	public Integer getTotalMegaeventOut() {
		return totalMegaeventOut;
	}
	public void setTotalMegaeventOut(Integer totalMegaeventOut) {
		this.totalMegaeventOut = totalMegaeventOut;
	}
	public Integer getTotalCheckedinVirtual() {
		return totalCheckedinVirtual;
	}
	public Integer getTotalCanceled() {
		return totalCanceled;
	}
	public Integer getTotalExpired() {
		return totalExpired;
	}
	public Integer getTotalTransfered() {
		return totalTransfered;
	}
	public Integer getTotalMaleGG() {
		return totalMaleGG;
	}
	public Integer getTotalMaleG() {
		return totalMaleG;
	}
	public Integer getTotalMaleM() {
		return totalMaleM;
	}
	public Integer getTotalMaleP() {
		return totalMaleP;
	}
	public Integer getTotalFemaleGG() {
		return totalFemaleGG;
	}
	public Integer getTotalFemaleG() {
		return totalFemaleG;
	}
	public Integer getTotalFemaleM() {
		return totalFemaleM;
	}
	public Integer getTotalFemaleP() {
		return totalFemaleP;
	}

}

package com.cb.mundo.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Representa os pontos de feedback de uma
 * producao.
 * 
 * @author Solkam
 * @since 30 NOV 2014
 */
@Entity
public class Feedback implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@NotNull
	@JoinColumn(unique=true)
	private Production production;

	private String instructorLeader;
	
	private String instructorFollowers;
	
	private String productorsElite;
	
	private int participantAdultNumber;
	
	private int participantYoungNumber;
	
	private int staffNumber;
	
	private String feedbackDuration;
	
	private Boolean flagPresent;
	
	private Boolean flagGrupalPhotoSent;
	
	private Boolean flagGuionFollowed;
	
	@Temporal(TemporalType.TIME)
	private Date departureTimeOnThursday;
	
	@Temporal(TemporalType.TIME)
	private Date departureTimeOnFriday;
	
	@Temporal(TemporalType.TIME)
	private Date departureTimeOnSaturday;
	
	@Temporal(TemporalType.TIME)
	private Date departureTimeOnSunday;

	
	@Temporal(TemporalType.TIME)
	private Date departureTimeStaff;

	
	private Boolean flagInscriptionToNextEvent;
	
	private String totalInscriptionForEachModule;
	
	private Boolean flagMegaEventInscription;
	
	private Boolean flagPlayVideoSchool;
	
	private Boolean flagPlayVideoMegaEvent;
	
	private Boolean flagPlayVideoYaikin;
	
	@Size(max=500)
	private String observacion;
	
	
	@Size(max=1000)
	private String autofeedbackInstructor1Name;
	@Size(max=1000)
	private String autofeedbackInstructor1Positive;
	@Size(max=1000)
	private String autofeedbackInstructor1Negative;
	
	@Size(max=1000)
	private String autofeedbackInstructor2Name;
	@Size(max=1000)
	private String autofeedbackInstructor2Positive;
	@Size(max=1000)
	private String autofeedbackInstructor2Negative;
	
	@Size(max=1000)
	private String autofeedbackInstructor3Name;
	@Size(max=1000)
	private String autofeedbackInstructor3Positive;
	@Size(max=1000)
	private String autofeedbackInstructor3Negative;
	
	@Size(max=1000)
	private String autofeedbackInstructor4Name;
	@Size(max=1000)
	private String autofeedbackInstructor4Positive;
	@Size(max=1000)
	private String autofeedbackInstructor4Negative;
	
	@Size(max=1000)
	private String feedbackComunicationToInstructor1;
	@Size(max=1000)
	private String feedbackComunicationToInstructor2;
	@Size(max=1000)
	private String feedbackComunicationToInstructor3;
	@Size(max=1000)
	private String feedbackComunicationToInstructor4;
	
	@Size(max=1000)
	private String feedbackImageToInstructor1;
	@Size(max=1000)
	private String feedbackImageToInstructor2;
	@Size(max=1000)
	private String feedbackImageToInstructor3;
	@Size(max=1000)
	private String feedbackImageToInstructor4;
	
	@Size(max=1000)
	private String feedbackTimeHandleToInstructor1;
	@Size(max=1000)
	private String feedbackTimeHandleToInstructor2;
	@Size(max=1000)
	private String feedbackTimeHandleToInstructor3;
	@Size(max=1000)
	private String feedbackTimeHandleToInstructor4;
	
	@Size(max=1000)
	private String feedbackStaffHandleToInstructor1;
	@Size(max=1000)
	private String feedbackStaffHandleToInstructor2;
	@Size(max=1000)
	private String feedbackStaffHandleToInstructor3;
	@Size(max=1000)
	private String feedbackStaffHandleToInstructor4;
	
	@Size(max=1000)
	private String autofeedbackProductionPositive;
	@Size(max=1000)
	private String autofeedbackProductionNegative;
	
	@Size(max=1000)
	private String feedbackToProductionPositive;
	@Size(max=1000)
	private String feedbackToProductionNegative;
	
	@Size(max=2000)
	private String sugestion;
	
	
	//sobre o email
	
	@Size(max=1000)
	private String emailRecipients;
	
	private String emailSubject;
	
	
	private Boolean flagSent = false;
	
	
	//logs
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@PrePersist void onPersist() {
		this.createDate = new Date();
	}
	
	@PreUpdate void onUpdate() {
		this.updateDate = new Date();
	}

	
	
	//construtores
	public Feedback() {}
	
	public Feedback(Production p) {
		this.production = p;
		aboutInstructores();
		aboutProductors();
		aboutStaff();
	}
	
	//abouts...
	private void aboutInstructores() {
		instructorFollowers = production.getDescFacilitators();
	}
	
	private void aboutProductors() {
		productorsElite = production.getDescProductors();
	}
	
	private void aboutStaff() {
		staffNumber = production.getStaffs().size();
	}
	
	
	//acessores...
	private static final long serialVersionUID = 6011260678612388474L;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Production getProduction() {
		return production;
	}
	public void setProduction(Production production) {
		this.production = production;
	}
	public String getInstructorLeader() {
		return instructorLeader;
	}
	public void setInstructorLeader(String instructorLeader) {
		this.instructorLeader = instructorLeader;
	}
	public String getInstructorFollowers() {
		return instructorFollowers;
	}
	public void setInstructorFollowers(String instructorFollowers) {
		this.instructorFollowers = instructorFollowers;
	}
	public String getProductorsElite() {
		return productorsElite;
	}
	public void setProductorsElite(String productorsElite) {
		this.productorsElite = productorsElite;
	}
	public int getParticipantAdultNumber() {
		return participantAdultNumber;
	}
	public void setParticipantAdultNumber(int participantAdultNumber) {
		this.participantAdultNumber = participantAdultNumber;
	}
	public int getParticipantYoungNumber() {
		return participantYoungNumber;
	}
	public void setParticipantYoungNumber(int participantYoungNumber) {
		this.participantYoungNumber = participantYoungNumber;
	}
	public int getStaffNumber() {
		return staffNumber;
	}
	public void setStaffNumber(int staffNumber) {
		this.staffNumber = staffNumber;
	}
	public String getFeedbackDuration() {
		return feedbackDuration;
	}
	public void setFeedbackDuration(String feedbackDuration) {
		this.feedbackDuration = feedbackDuration;
	}
	public Boolean getFlagPresent() {
		return flagPresent;
	}
	public void setFlagPresent(Boolean flagPresent) {
		this.flagPresent = flagPresent;
	}
	public Boolean getFlagGrupalPhotoSent() {
		return flagGrupalPhotoSent;
	}
	public void setFlagGrupalPhotoSent(Boolean flagGrupalPhotoSent) {
		this.flagGrupalPhotoSent = flagGrupalPhotoSent;
	}
	public Boolean getFlagGuionFollowed() {
		return flagGuionFollowed;
	}
	public void setFlagGuionFollowed(Boolean flagGuionFollowed) {
		this.flagGuionFollowed = flagGuionFollowed;
	}
	public Date getDepartureTimeOnThursday() {
		return departureTimeOnThursday;
	}
	public void setDepartureTimeOnThursday(Date departureTimeOnThursday) {
		this.departureTimeOnThursday = departureTimeOnThursday;
	}
	public Date getDepartureTimeOnFriday() {
		return departureTimeOnFriday;
	}
	public void setDepartureTimeOnFriday(Date departureTimeOnFriday) {
		this.departureTimeOnFriday = departureTimeOnFriday;
	}
	public Date getDepartureTimeOnSaturday() {
		return departureTimeOnSaturday;
	}
	public void setDepartureTimeOnSaturday(Date departureTimeOnSaturday) {
		this.departureTimeOnSaturday = departureTimeOnSaturday;
	}
	public Date getDepartureTimeOnSunday() {
		return departureTimeOnSunday;
	}
	public void setDepartureTimeOnSunday(Date departureTimeOnSunday) {
		this.departureTimeOnSunday = departureTimeOnSunday;
	}
	public Date getDepartureTimeStaff() {
		return departureTimeStaff;
	}
	public void setDepartureTimeStaff(Date departureTimeStaff) {
		this.departureTimeStaff = departureTimeStaff;
	}
	public Boolean getFlagInscriptionToNextEvent() {
		return flagInscriptionToNextEvent;
	}
	public void setFlagInscriptionToNextEvent(Boolean flagInscriptionToNextEvent) {
		this.flagInscriptionToNextEvent = flagInscriptionToNextEvent;
	}
	public String getTotalInscriptionForEachModule() {
		return totalInscriptionForEachModule;
	}
	public void setTotalInscriptionForEachModule(
			String totalInscriptionForEachModule) {
		this.totalInscriptionForEachModule = totalInscriptionForEachModule;
	}
	public Boolean getFlagMegaEventInscription() {
		return flagMegaEventInscription;
	}
	public void setFlagMegaEventInscription(Boolean flagMegaEventInscription) {
		this.flagMegaEventInscription = flagMegaEventInscription;
	}
	public Boolean getFlagPlayVideoSchool() {
		return flagPlayVideoSchool;
	}
	public void setFlagPlayVideoSchool(Boolean flagPlayVideoSchool) {
		this.flagPlayVideoSchool = flagPlayVideoSchool;
	}
	public Boolean getFlagPlayVideoMegaEvent() {
		return flagPlayVideoMegaEvent;
	}
	public void setFlagPlayVideoMegaEvent(Boolean flagPlayVideoMegaEvent) {
		this.flagPlayVideoMegaEvent = flagPlayVideoMegaEvent;
	}
	public Boolean getFlagPlayVideoYaikin() {
		return flagPlayVideoYaikin;
	}
	public void setFlagPlayVideoYaikin(Boolean flagPlayVideoYaikin) {
		this.flagPlayVideoYaikin = flagPlayVideoYaikin;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getAutofeedbackInstructor1Name() {
		return autofeedbackInstructor1Name;
	}
	public void setAutofeedbackInstructor1Name(String autofeedbackInstructor1Name) {
		this.autofeedbackInstructor1Name = autofeedbackInstructor1Name;
	}
	public String getAutofeedbackInstructor1Positive() {
		return autofeedbackInstructor1Positive;
	}
	public void setAutofeedbackInstructor1Positive(
			String autofeedbackInstructor1Positive) {
		this.autofeedbackInstructor1Positive = autofeedbackInstructor1Positive;
	}
	public String getAutofeedbackInstructor1Negative() {
		return autofeedbackInstructor1Negative;
	}
	public void setAutofeedbackInstructor1Negative(
			String autofeedbackInstructor1Negative) {
		this.autofeedbackInstructor1Negative = autofeedbackInstructor1Negative;
	}
	public String getAutofeedbackInstructor2Name() {
		return autofeedbackInstructor2Name;
	}
	public void setAutofeedbackInstructor2Name(String autofeedbackInstructor2Name) {
		this.autofeedbackInstructor2Name = autofeedbackInstructor2Name;
	}
	public String getAutofeedbackInstructor2Positive() {
		return autofeedbackInstructor2Positive;
	}
	public void setAutofeedbackInstructor2Positive(
			String autofeedbackInstructor2Positive) {
		this.autofeedbackInstructor2Positive = autofeedbackInstructor2Positive;
	}
	public String getAutofeedbackInstructor2Negative() {
		return autofeedbackInstructor2Negative;
	}
	public void setAutofeedbackInstructor2Negative(
			String autofeedbackInstructor2Negative) {
		this.autofeedbackInstructor2Negative = autofeedbackInstructor2Negative;
	}
	public String getAutofeedbackInstructor3Name() {
		return autofeedbackInstructor3Name;
	}
	public void setAutofeedbackInstructor3Name(String autofeedbackInstructor3Name) {
		this.autofeedbackInstructor3Name = autofeedbackInstructor3Name;
	}
	public String getAutofeedbackInstructor3Positive() {
		return autofeedbackInstructor3Positive;
	}
	public void setAutofeedbackInstructor3Positive(
			String autofeedbackInstructor3Positive) {
		this.autofeedbackInstructor3Positive = autofeedbackInstructor3Positive;
	}
	public String getAutofeedbackInstructor3Negative() {
		return autofeedbackInstructor3Negative;
	}
	public void setAutofeedbackInstructor3Negative(
			String autofeedbackInstructor3Negative) {
		this.autofeedbackInstructor3Negative = autofeedbackInstructor3Negative;
	}
	public String getAutofeedbackInstructor4Name() {
		return autofeedbackInstructor4Name;
	}
	public void setAutofeedbackInstructor4Name(String autofeedbackInstructor4Name) {
		this.autofeedbackInstructor4Name = autofeedbackInstructor4Name;
	}
	public String getAutofeedbackInstructor4Positive() {
		return autofeedbackInstructor4Positive;
	}
	public void setAutofeedbackInstructor4Positive(
			String autofeedbackInstructor4Positive) {
		this.autofeedbackInstructor4Positive = autofeedbackInstructor4Positive;
	}
	public String getAutofeedbackInstructor4Negative() {
		return autofeedbackInstructor4Negative;
	}
	public void setAutofeedbackInstructor4Negative(
			String autofeedbackInstructor4Negative) {
		this.autofeedbackInstructor4Negative = autofeedbackInstructor4Negative;
	}
	public String getFeedbackComunicationToInstructor1() {
		return feedbackComunicationToInstructor1;
	}
	public void setFeedbackComunicationToInstructor1(
			String feedbackComunicationToInstructor1) {
		this.feedbackComunicationToInstructor1 = feedbackComunicationToInstructor1;
	}
	public String getFeedbackComunicationToInstructor2() {
		return feedbackComunicationToInstructor2;
	}
	public void setFeedbackComunicationToInstructor2(
			String feedbackComunicationToInstructor2) {
		this.feedbackComunicationToInstructor2 = feedbackComunicationToInstructor2;
	}
	public String getFeedbackComunicationToInstructor3() {
		return feedbackComunicationToInstructor3;
	}
	public void setFeedbackComunicationToInstructor3(
			String feedbackComunicationToInstructor3) {
		this.feedbackComunicationToInstructor3 = feedbackComunicationToInstructor3;
	}
	public String getFeedbackComunicationToInstructor4() {
		return feedbackComunicationToInstructor4;
	}
	public void setFeedbackComunicationToInstructor4(
			String feedbackComunicationToInstructor4) {
		this.feedbackComunicationToInstructor4 = feedbackComunicationToInstructor4;
	}
	public String getFeedbackImageToInstructor1() {
		return feedbackImageToInstructor1;
	}
	public void setFeedbackImageToInstructor1(String feedbackImageToInstructor1) {
		this.feedbackImageToInstructor1 = feedbackImageToInstructor1;
	}
	public String getFeedbackImageToInstructor2() {
		return feedbackImageToInstructor2;
	}
	public void setFeedbackImageToInstructor2(String feedbackImageToInstructor2) {
		this.feedbackImageToInstructor2 = feedbackImageToInstructor2;
	}
	public String getFeedbackImageToInstructor3() {
		return feedbackImageToInstructor3;
	}
	public void setFeedbackImageToInstructor3(String feedbackImageToInstructor3) {
		this.feedbackImageToInstructor3 = feedbackImageToInstructor3;
	}
	public String getFeedbackImageToInstructor4() {
		return feedbackImageToInstructor4;
	}
	public void setFeedbackImageToInstructor4(String feedbackImageToInstructor4) {
		this.feedbackImageToInstructor4 = feedbackImageToInstructor4;
	}
	public String getFeedbackTimeHandleToInstructor1() {
		return feedbackTimeHandleToInstructor1;
	}
	public void setFeedbackTimeHandleToInstructor1(
			String feedbackTimeHandleToInstructor1) {
		this.feedbackTimeHandleToInstructor1 = feedbackTimeHandleToInstructor1;
	}
	public String getFeedbackTimeHandleToInstructor2() {
		return feedbackTimeHandleToInstructor2;
	}
	public void setFeedbackTimeHandleToInstructor2(
			String feedbackTimeHandleToInstructor2) {
		this.feedbackTimeHandleToInstructor2 = feedbackTimeHandleToInstructor2;
	}
	public String getFeedbackTimeHandleToInstructor3() {
		return feedbackTimeHandleToInstructor3;
	}
	public void setFeedbackTimeHandleToInstructor3(
			String feedbackTimeHandleToInstructor3) {
		this.feedbackTimeHandleToInstructor3 = feedbackTimeHandleToInstructor3;
	}
	public String getFeedbackTimeHandleToInstructor4() {
		return feedbackTimeHandleToInstructor4;
	}
	public void setFeedbackTimeHandleToInstructor4(
			String feedbackTimeHandleToInstructor4) {
		this.feedbackTimeHandleToInstructor4 = feedbackTimeHandleToInstructor4;
	}
	public String getFeedbackStaffHandleToInstructor1() {
		return feedbackStaffHandleToInstructor1;
	}
	public void setFeedbackStaffHandleToInstructor1(
			String feedbackStaffHandleToInstructor1) {
		this.feedbackStaffHandleToInstructor1 = feedbackStaffHandleToInstructor1;
	}
	public String getFeedbackStaffHandleToInstructor2() {
		return feedbackStaffHandleToInstructor2;
	}
	public void setFeedbackStaffHandleToInstructor2(
			String feedbackStaffHandleToInstructor2) {
		this.feedbackStaffHandleToInstructor2 = feedbackStaffHandleToInstructor2;
	}
	public String getFeedbackStaffHandleToInstructor3() {
		return feedbackStaffHandleToInstructor3;
	}
	public void setFeedbackStaffHandleToInstructor3(
			String feedbackStaffHandleToInstructor3) {
		this.feedbackStaffHandleToInstructor3 = feedbackStaffHandleToInstructor3;
	}
	public String getFeedbackStaffHandleToInstructor4() {
		return feedbackStaffHandleToInstructor4;
	}
	public void setFeedbackStaffHandleToInstructor4(
			String feedbackStaffHandleToInstructor4) {
		this.feedbackStaffHandleToInstructor4 = feedbackStaffHandleToInstructor4;
	}
	public String getAutofeedbackProductionPositive() {
		return autofeedbackProductionPositive;
	}
	public void setAutofeedbackProductionPositive(
			String autofeedbackProductionPositive) {
		this.autofeedbackProductionPositive = autofeedbackProductionPositive;
	}
	public String getAutofeedbackProductionNegative() {
		return autofeedbackProductionNegative;
	}
	public void setAutofeedbackProductionNegative(
			String autofeedbackProductionNegative) {
		this.autofeedbackProductionNegative = autofeedbackProductionNegative;
	}
	public String getFeedbackToProductionPositive() {
		return feedbackToProductionPositive;
	}
	public void setFeedbackToProductionPositive(String feedbackToProductionPositive) {
		this.feedbackToProductionPositive = feedbackToProductionPositive;
	}
	public String getFeedbackToProductionNegative() {
		return feedbackToProductionNegative;
	}
	public void setFeedbackToProductionNegative(String feedbackToProductionNegative) {
		this.feedbackToProductionNegative = feedbackToProductionNegative;
	}
	public String getSugestion() {
		return sugestion;
	}
	public void setSugestion(String sugestion) {
		this.sugestion = sugestion;
	}
	public String getEmailRecipients() {
		return emailRecipients;
	}
	public void setEmailRecipients(String emailRecipients) {
		this.emailRecipients = emailRecipients;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public Boolean getFlagSent() {
		return flagSent;
	}
	public void setFlagSent(Boolean flagSent) {
		this.flagSent = flagSent;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Feedback other = (Feedback) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public boolean isTransient() {
		return getId()==null;
	}
	
	@Override
	public String toString() {
		return "Feedback [id=" + id + ", flagSent=" + flagSent + "]";
	}

	
	//runtime
	public String getDescribedDates() {
		return production.getDescribedDates();
	}
	
	public String getDescEvent() {
		return production.getDescEvent();
	}
	
	public String getDescCity() {
		return production.getCity().getFullDesc();
	}
	
   
	
}

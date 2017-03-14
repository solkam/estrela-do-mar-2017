package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.security.LoginBean;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.Hosting;
import com.cb.mundo.model.entity.HostingConfirmed;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RoomType;
import com.cb.mundo.model.service.HostingService;
import com.cb.mundo.model.service.RoomTypeService;


/**
 * Controller para Gestao de Hospedagem.
 * @author Solkam
 * @since 27 ABR 2014
 */
@ManagedBean(name="hostingBean")
@ViewScoped
public class HostingBean implements Serializable {
	
	@EJB HostingService hostingService;
	
	@EJB RoomTypeService roomTypeService;
	
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;

	
	private MegaEvent currentMegaEvent;


	//combo
	private List<RoomType> comboRoomTypes;

	
	//listas...	
	private List<Hosting> pendingHostings;
	private List<Register> pendingRegisters;
	
	private List<Hosting> okHostings;

	//principal
	private Hosting hosting;
	

	@PostConstruct void init() {
		initCurrentMegaEvent();
		populateComboRoomType();
		populateAllLists();
	}
	

	private void populateComboRoomType() {
		comboRoomTypes = roomTypeService.searchActiveRoomType();
	}


	private void initCurrentMegaEvent() {
		currentMegaEvent = sessionHolder.getCurrentMegaEvent();
	}

	private void populateAllLists() {
		pendingHostings = hostingService.searchHostingNotConfirmedButWithSugestedsByMegaEvent( currentMegaEvent );
		pendingRegisters = hostingService.searchRegisterWithoutHostingByMegaEvent(currentMegaEvent);
		okHostings = hostingService.searchHostingWithConfirmedsByMegaEvent(currentMegaEvent);
	}
	
	
	
	//actions para pendentes...	
	public void managePendingHosting(Hosting selectedHosting) {//lista de Inscrioes com sugeridos
		this.hosting = hostingService.convertHostingSugestedsToConfirmeds(selectedHosting, currentMegaEvent);
	}
	
	public void managerPendingRegister(Register seletectedRegister) {//lista de Inscricoes sem sugeridos
		this.hosting = createTransientHostingByRegister(seletectedRegister);
	}
	
	public void manageOkHosting(Hosting selectedHosting) {
		this.hosting = selectedHosting;
		refreshHosting();
	}
	
	

	//actions para confirmados...
	private Register selectedRegister;
	
	public void addSelectRegister() {//ajax em auto complete
		hostingService.saveHostingConfirmed(selectedRegister, hosting);
		refreshAll();
		selectedRegister = null;//pronto para a proxima inscricao
	}
	
	
	public void removeConfirmedHosting(HostingConfirmed confirmed) {//bot�o em confirmados
		hostingService.removeHostingConfirmed(confirmed);
		refreshAll();
	}
	
	
	
	
	

	//actions para hosting
	public void saveHosting() {
		hosting = hostingService.saveHosting(hosting, loginBean.getAuthenticatedUser() );
		refreshAll();
		JSFUtil.addInfoMessage("msg_hosting_info_save_sucess");
	}
	
	public void removeHosting() {
		hostingService.removeHosting(hosting);
		populateAllLists();
		JSFUtil.addInfoMessage("msg_hosting_remove_sucess");
	}


	//auto complete	
	/**
	 * Pesquisa Register para o auto complete (outras inscricoes)
	 * @param fragment
	 * @return
	 */
	public List<Register> completeRegister(String fragment) {
		return hostingService.searchRegisterByMegaEventContactNameOrCivilNameOrCity(currentMegaEvent, fragment, fragment, fragment);
	}
	
	
	
	
	
	//util
	private void refreshHosting() {
		this.hosting = hostingService.refreshHosting(hosting);
	}
	
	
	private void refreshAll() {
		refreshHosting();
		populateAllLists();
	}

	
	

	private Hosting createTransientHostingByRegister(Register seletectedRegister) {
		Hosting newHosting = new Hosting(currentMegaEvent);
		newHosting.buildHostingConfirmed(seletectedRegister);
		return newHosting;
	}
	
	
	
	
	
	//acessores...	
	private static final long serialVersionUID = -3014275596406437706L;

	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
	
	public List<Hosting> getPendingHostings() {
		return pendingHostings;
	}
	public Hosting getHosting() {
		return hosting;
	}
	public void setHosting(Hosting hosting) {
		this.hosting = hosting;
	}
	public List<Hosting> getOkHostings() {
		return okHostings;
	}
	public void setConfiguredHostings(List<Hosting> configuredHostings) {
		this.okHostings = configuredHostings;
	}
	public List<Register> getPendingRegisters() {
		return pendingRegisters;
	}
	public Register getSelectedRegister() {
		return selectedRegister;
	}
	public void setSelectedRegister(Register selectedRegister) {
		this.selectedRegister = selectedRegister;
	}
	public List<RoomType> getComboRoomTypes() {
		return comboRoomTypes;
	}
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	
	
}

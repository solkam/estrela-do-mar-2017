package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.security.LoginBean;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.entity.CheckOut;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.entity.enumeration.RegisterStatus;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.RegisterService;

/**
 * Managed Bean para realizacao de check-out
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="checkOutBean")
@ViewScoped
public class CheckOutBean implements Serializable {
	private static final long serialVersionUID = 3055898147672730835L;

	@EJB EventService eventService;
	
	@EJB RegisterService registerService;
	
	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	private List<Register> registers = new ArrayList<Register>();
	
	private Register selectedRegister;
	
	private String keyword;
	
	@PostConstruct void init() {
	}
	
	private void populateCheckout() {
		CheckOut checkout = new CheckOut();
		checkout.setCheckoutUser( loginBean.getAuthenticatedUser() );
		
		selectedRegister.setCheckout( checkout );
	}
	
//action...
	public void searchByKeywork() {
		RegisterStatus status = RegisterStatus.CHECKEDIN;//para fazer check-out, pesquisa quem ja fez check-in
		
		MegaEvent currentMegaEvent = sessionHolder.getCurrentMegaEvent();

		//lista de status aptos para checkout
		List<RegisterStatus> listOfStatus = new ArrayList<RegisterStatus>();
		listOfStatus.add( RegisterStatus.CHECKEDIN );
		
		registers = registerService.searchRegistersByMegaEventAndStatusAndKeywords(currentMegaEvent, listOfStatus, keyword);
	}
	
	public void selectRegister(Register r) {
		selectedRegister = registerService.refreshRegisterWithPaymentsAndCredits(r);
		
		populateCheckout();
	}
	
	/**
	 * Configura os aspectos do checkout antes de confirmar:
	 * 1.novo status
	 * 2.usuario responsavel pelo checkout
	 * 3.data 
	 */
	private void configueCheckout() {
		selectedRegister.setCheckout( new CheckOut() );
		
		//status
		selectedRegister.setStatus( RegisterStatus.CHECKEDOUT );
		
		//usuario responsavel
		UserCB responsableUser = loginBean.getAuthenticatedUser();
		selectedRegister.getCheckout().setCheckoutUser(responsableUser);
		
		//data de checkout
		Date checkoutDate = new Date();
		selectedRegister.getCheckout().setCheckoutDate( checkoutDate );
	}
	
	public void confirmCheckout() {
		try {
			configueCheckout();
			selectedRegister = registerService.saveRegister(selectedRegister);
			
			//renova a pesquisa para nao aparecer o registro que acabou de ser checkouted
			searchByKeywork();
			
			JSFUtil.addInfoMessage("msg_checkout_confirmed_sucess", null);
			
			selectedRegister = registerService.refreshRegisterWithPaymentsAndCredits( selectedRegister );
		
		}catch(Exception e) {
			JSFUtil.addErrorMessage(e);
		}
	}
	
//acessores...	
	
	public Register getSelectedRegister() {
		return selectedRegister;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<Register> getRegisters() {
		return registers;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public void setSelectedRegister(Register selectedRegister) {
		this.selectedRegister = selectedRegister;
	}

	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
	
	

// O proprio objeto register ja faz isso...	
//	public BigDecimal getPendentValue() {
//		BigDecimal pendent = null;
//		if (selectedRegister==null) {
//			pendent = new BigDecimal("0");
//		} else {
//			BigDecimal total = selectedRegister.getCalculatedTotalValue();
//			BigDecimal paid  = selectedRegister.getCalculatedPaidValue();
//			BigDecimal discount = selectedRegister.getDiscountValue();
//			
//			pendent = total.subtract(paid).subtract(discount);
//		}
//		return pendent;
//	}

}

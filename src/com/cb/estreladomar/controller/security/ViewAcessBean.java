package com.cb.estreladomar.controller.security;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.entity.enumeration.Profile;

/**
 * Contem os metodos que definem se items de acesso serao visualizados
 * conforme o profile do usuario logado
 * 
 * @author Solkam
 * @since 21 dez 2012
 */
@ManagedBean(name="viewAcessBean")
@RequestScoped
public class ViewAcessBean implements Serializable {
	private static final long serialVersionUID = 5906691144381386990L;

	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;
	
	private UserCB authenticatedUser;
	
	@PostConstruct void init() {
		authenticatedUser = loginBean.getAuthenticatedUser();
	}
	

//acessores de seguranca
	
	public boolean getAcessibleForMyRegister() {
		return true;//todos tem acesso a propria inscricao... 
	}

	public boolean getAcessibleForChangeMegaEvent() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
		    || Profile.ADM.equals(profile)
		    || Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
			;
	}
	
	public boolean getAcessibleForMyRegisteredOnes() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.MAR.equals(profile)
		    || Profile.PRO.equals(profile)
		    || Profile.PLI.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForReport() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.MAR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}

	public boolean getAcessibleForCheckin() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}

	public boolean getAcessibleForCheckout() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile)
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForPayment() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
		    || Profile.ADM.equals(profile)
		    || Profile.FUN.equals(profile)
			;
	}
	
	public boolean getAcessibleForContact() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.MAR.equals(profile)
		    || Profile.LMU.equals(profile)
		    || Profile.PRO.equals(profile)
		    || Profile.PLI.equals(profile)
			;
	}

	public boolean getAcessibleForConfig() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
			;
	}

	public boolean getAcessibleForUser() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.LMU.equals(profile)
		    || Profile.PRO.equals(profile)
		    || Profile.PLI.equals(profile)
			;
	}
	
	
	public boolean getAcessibleForTransport() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
//		    || Profile.ADM.equals(profile)
//			|| Profile.DIR.equals(profile)
//			|| Profile.STT.equals(profile)
			;
	}

	public boolean getAcessibleForSeminarComplementary() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile)
			;
	}

	public boolean getAcessibleForRoom() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile)
		    || Profile.ADM.equals(profile)
		    || Profile.LMU.equals(profile)
//		    || Profile.PRO.equals(profile)
			;
	}
	
	
	
/* ******************
 * Informe PAGAMENTOS
 * ******************/

	public boolean getAcessibelForPaymentGroup() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
		    || Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForReportBalanceByWeek() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
		    || Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForReportBalanceByRegister() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
		    || Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForReportPaymentByEvent() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
		    || Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForReportPaymentMethodByDate() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
		    || Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForReportProductorsComissions() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
		    || Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	
	

/* ********************
 * Informe about people	
 * ********************/

	public boolean getAcessibleForReportRegisterByWeek() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForReportMovimentByDate() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
		    || Profile.DIR.equals(profile)
			|| Profile.MAR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForReportPeopleInMegaEventByDate() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
			|| Profile.DIR.equals(profile)
			|| Profile.MAR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}

	public boolean getAcessibleForReportCheckOUTByDate() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
			|| Profile.DIR.equals(profile)
			|| Profile.MAR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForReportCheckINByDate() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
			|| Profile.DIR.equals(profile)
			|| Profile.MAR.equals(profile)
		    || Profile.LMU.equals(profile)
			;
	}
	
	public boolean getAcessibleForReportPeopleWithPhysicalLimitation() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
			|| Profile.DIR.equals(profile)
			|| Profile.MAR.equals(profile)
		    || Profile.LMU.equals(profile)
				;
	}

	public boolean getAcessibleForReportPeopleByEvent() {
		Profile profile = authenticatedUser.getProfile();
		return Profile.TEC.equals(profile) 
			|| Profile.ADM.equals(profile)
			|| Profile.FUN.equals(profile)
			|| Profile.DIR.equals(profile)
			|| Profile.MAR.equals(profile)
		    || Profile.LMU.equals(profile)
				;
	}

	
//acessores
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
}

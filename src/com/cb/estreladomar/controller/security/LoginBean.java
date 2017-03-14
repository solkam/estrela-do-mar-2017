package com.cb.estreladomar.controller.security;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.cb.estreladomar.controller.SignupBean;
import com.cb.estreladomar.controller.builder.ResetPasswordEmailBuilder;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.estreladomar.controller.util.Navigation;
import com.cb.mundo.model.builder.SimpleEmailBuilder;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.entity.enumeration.Profile;
import com.cb.mundo.model.exception.BusinessException;
import com.cb.mundo.model.exception.ContactCivilNameAlreadyExistException;
import com.cb.mundo.model.exception.ContactEmailAlreadyExistException;
import com.cb.mundo.model.exception.ContactNewNameAlreadExistException;
import com.cb.mundo.model.service.AddressService;
import com.cb.mundo.model.service.ContactService;
import com.cb.mundo.model.service.UserService;
import com.cb.mundo.model.service.email.EmailService;

/**
 * Controller para login e logout
 * 
 * @author Solkam
 * @since 20 abr 2012
 */
@ManagedBean(name="loginBean")
@SessionScoped
public class LoginBean implements Serializable {
	private static final long serialVersionUID = -2019118333500012247L;

	@EJB ContactService contactService;
	
	@EJB UserService userService;
	
	@EJB EmailService emailService;

	@EJB AddressService addressService;
	
	@ManagedProperty("#{signupBean}")
	private SignupBean signupBean;
	
	private String username;
	private String password;
	
	private UserCB user;
	
	private String emailToReset;
	
	
	public String doLogin() {
		try {
			user = userService.autenticateUser(username, password, getHttpSession() );
			
			Profile userProfile = user.getProfile();
			
			switch (userProfile) {
			case TEC:
				return Navigation.REPORT_INDEX;

			case FUN:
				return Navigation.REPORT_INDEX;
				
			case ADM:
				return Navigation.PAYMENT;
				
			case DIR:
				return Navigation.REPORT_INDEX;
				
			case MAR:
				return Navigation.REPORT_INDEX;
				
			case PRO:
				return Navigation.MY_REGISTERED_ONES;
				
			case STT:
				return Navigation.TRANSPORT;
				
			case STG:
				return Navigation.REPORT_PEOPLE_BY_EVENT;

			case CLI:
				return Navigation.REGISTER_INDEX;

			default:
				return Navigation.REGISTER_INDEX;
			}
						
		} catch (Exception e) {
			JSFUtil.addErrorMessage(e);
			return Navigation.LOGIN;
		}
	}
	
	private HttpSession getHttpSession() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession httpSession = (HttpSession) fc.getExternalContext().getSession(false);
		return httpSession;
	}
	
	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.invalidate();
		user = null;
		return Navigation.LOGIN;
	}
	
	public void resetPassword() {
		//1.reseta a senha
		UserCB requestingUser = userService.resetUserPassword(emailToReset);
		
		
		//2.envia email
		SimpleEmailBuilder builder = new ResetPasswordEmailBuilder(requestingUser);
		emailService.sendEmail(builder.getContactRecipient()
						      ,builder.getReplyTo()
							  ,builder.getSubject()
							  ,builder.getContent() );

		JSFUtil.addInfoMessage("msg_user_password_reset_sucess_and_send_email");
		
		
		//3.mensagem de sucesso
		//[alternativo] para checkins com muitas pessoas
//		Object[] params = new Object[] {requestingUser.getPassword()};
//		JSFUtil.addInfoMessage("msg_user_password_reset_sucess_and_show", params);
		
	}
	
	
	/**
	 * Navega para tela de criar novo usuario
	 */
	public String gotoSignupForm() {
		return Navigation.SIGNUP_FORM;
	}
	
	
	
	/**
	 * Salva o novo usuario e ja o autentica
	 * (invocado de sign-up.xhtml)
	 * @return
	 */
	public String saveClientUserAndDoLogin() {
		try {
			//1.salva novo usuario
			signupBean.saveClientUser();
			
			//2.autentica e faz a navegaao
			username = signupBean.getUser().getContact().getEmail();
			password = signupBean.getUser().getPassword();
			return doLogin();
			
		}catch(ContactNewNameAlreadExistException e) {
			handleBusinessException(e);
			
		}catch(ContactCivilNameAlreadyExistException e) {
			handleBusinessException(e);
			
		}catch(ContactEmailAlreadyExistException e) {
			handleBusinessException(e);
			
		}catch(Exception e) {
			handleException(e);
		}
		//se chegou aqui, algum problema aconteceu
		return Navigation.SIGNUP_FORM;
	}
	
	private void handleException(Exception e) {
		JSFUtil.addErrorMessage(e);
	}
	
	private void handleBusinessException(BusinessException e) {
		JSFUtil.addErrorMessage(e);
	}
	

	public boolean isLogged() {
		return this.user!=null && this.user.getContact()!=null;
	}
	
	public UserCB getAuthenticatedUser() {
		return user;
	}


//acessors...	
	public String getEmailToReset() {
		return emailToReset;
	}

	public void setEmailToReset(String emailToReset) {
		this.emailToReset = emailToReset;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSignupBean(SignupBean signupBean) {
		this.signupBean = signupBean;
	}
	

}

package com.cb.estreladomar.controller.builder;

import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.builder.SimpleEmailBuilder;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.UserCB;
import com.cb.mundo.model.util.Constants;

/**
 * Construtor de email para resetar senha.
 * 
 * @author Solkam
 * @since 29 ABR 2014
 */
public class ResetPasswordEmailBuilder implements SimpleEmailBuilder {
	
	private final UserCB user;
	
	public ResetPasswordEmailBuilder(UserCB user) {
		this.user = user;
	}
	

	@Override
	public Contact getContactRecipient() {
		return user.getContact();
	}

	@Override
	public String getSubject() {
		String subject = JSFUtil.getFormatedMessage("email_reset_password_subject", null);
		return subject;
	}

	@Override
	public String getContent() {
		String content = JSFUtil.getFormatedMessage("email_reset_password_content", new Object[]{ user.getPassword() });
		return content;
	}

	@Override
	public String getReplyTo() {
		return Constants.TECNOLOGY_CONTACT.getEmail();
	}
	
	
}

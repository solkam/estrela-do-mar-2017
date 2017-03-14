package com.cb.estreladomar.controller.builder;

import com.cb.estreladomar.controller.util.EmailTemplateUtil;
import com.cb.mundo.model.builder.SimpleEmailBuilder;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.util.StringUtil;

public class CheckinConfirmationEmailBuilder implements SimpleEmailBuilder {
	
	private static final String EMAIL_CONFIRMATION_CONTENT_KEY = "email_checkin_confirmation_content";
	private static final String EMAIL_CONFIRMATION_SUBJECT_KEY = "email_checkin_confirmation_subject";
	
	private Register register;
	
	private String contentTemplate;
	private String subjectTemplate;
	
	
	public CheckinConfirmationEmailBuilder(Register register) {
		this.register = register;
		
		this.contentTemplate = EmailTemplateUtil.getEmailTemplate(EMAIL_CONFIRMATION_CONTENT_KEY);
		this.subjectTemplate = EmailTemplateUtil.getEmailTemplate(EMAIL_CONFIRMATION_SUBJECT_KEY);
	}
	
	@Override
	public Contact getContactRecipient() {
		return register.getContact();
	}
	
	@Override
	public String getSubject() {
		subjectTemplate = formatMegaEventName(subjectTemplate);
		return subjectTemplate;
	}

	@Override
	public String getContent() {
		contentTemplate = formatMegaEventName( contentTemplate );
		return contentTemplate;
	}
	
	@Override
	public String getReplyTo() {
		return register.getMegaEvent().getAdminEmail();
	}

	
	
//utilitario (serve tanto para o conteudo quanto para o subject)	
	private String formatMegaEventName(String text) {
		return StringUtil.replace(text, "@megaEventName", register.getMegaEvent().getName() );
	}


	
}

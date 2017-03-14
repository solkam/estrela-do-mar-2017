package com.cb.estreladomar.controller.builder;

import com.cb.estreladomar.controller.util.EmailTemplateUtil;
import com.cb.mundo.model.builder.SimpleEmailBuilder;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.util.StringUtil;


/**
 * Adaptar a inscricao ao email template
 * @author Solkam
 */
public class RegisterConfirmationEmailBuilder implements SimpleEmailBuilder {
	
	private static final String EMAIL_CONFIRMATION_REGISTER_SUBJECT_KEY = "email_register_confirmation_subject";
	private static final String EMAIL_CONFIRMATION_REGISTER_CONTENT_KEY = "email_register_confirmation_content";
	
	private static final String TABLE_DATA_OF_EVENTS = 
	"<tr>"+
	"<td id='cellData'>%s</td>"+
	"<td id='cellData'>%s</td>"+
	"<td id='cellData'>%s</td>"+
	"</tr>";
	
	
	
	private final Register register;
	
	private String subjectTemplate;
	private String contentTemplate;

	
	public RegisterConfirmationEmailBuilder(Register register) {
		this.register = register;
		contentTemplate = EmailTemplateUtil.getEmailTemplate( EMAIL_CONFIRMATION_REGISTER_CONTENT_KEY );
		subjectTemplate = EmailTemplateUtil.getEmailTemplate( EMAIL_CONFIRMATION_REGISTER_SUBJECT_KEY );
	}

	
	@Override
	public Contact getContactRecipient() {
		return register.getContact();
	}
	
	@Override
	public String getReplyTo() {
		return register.getMegaEvent().getAdminEmail();
	}

	@Override
	public String getSubject() {
		subjectTemplate = EmailTemplateUtil.formatMegaEventName( subjectTemplate, register );
		return subjectTemplate;
	}

	@Override
	public String getContent() {
		contentTemplate = EmailTemplateUtil.formatMegaEventName(contentTemplate, register);
		contentTemplate = EmailTemplateUtil.formatRegisterId(contentTemplate, register);
		contentTemplate = formatTableOfEvents(contentTemplate);
		return contentTemplate;
	}
	


	private String formatTableOfEvents(String emailContent) {
		return StringUtil.replace(emailContent, "@RowsOfEvents", buildRowsOfEvent());
	}
	
	private String buildRowsOfEvent() {
		StringBuilder builder =  new StringBuilder();
		
		for (RegisterDetail detail : register.getDetails()) {
			builder.append( String.format(TABLE_DATA_OF_EVENTS, 
					detail.getEventWeek().getName(), 
					detail.getEvent().getDisplayNameOrSchool() + " - "+ detail.getPresence(),
					detail.getEventWeek().getDescDates()
					));
		}
		
		return builder.toString();
	}


}

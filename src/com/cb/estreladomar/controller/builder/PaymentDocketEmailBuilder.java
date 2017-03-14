package com.cb.estreladomar.controller.builder;

import com.cb.estreladomar.controller.util.EmailTemplateUtil;
import com.cb.mundo.model.builder.SimpleEmailBuilder;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.entity.RegisterDetail;

/**
 * Adapter para envio de email com o recibo de pagamento
 * 
 * @author Solkam
 * @since 20 JAN 2014
 */
public class PaymentDocketEmailBuilder implements SimpleEmailBuilder {
	
	private static final String EMAIL_PAYMENT_DOCKET_SUBJECT_KEY = "email_payment_docket_subject";
	private static final String EMAIL_PAYMENT_DOCKET_CONTENT_KEY = "email_payment_docket_content";
	
	private static final String TABLE_DATA_EVENTS = 
	"<tr>"+
	"  <td class='cellData'>%s</td>"+
	"  <td class='cellData'>%s</td>"+
	"  <td class='cellData currency'>%s</td>"+
	"  <td class='cellData currency paidValue'>%s</td>"+
	"  <td class='cellData currency pendentValue'>%s</td>"+
	"</tr>";
	
	private static final String TABLE_DATA_FOOTER = 
	"<tr>"+
	"  <td />"+
	"  <td />"+
	"  <td class='cellData currency destak'>%s</td>"+
	"  <td class='cellData currency paidValue destak'>%s</td>"+
	"  <td class='cellData currency pendentValue destak'>%s</td>"+
	"</tr>";
	
	
	private Register register;
	
	private String subjectTemplate;
	private String contentTemplate;

	public PaymentDocketEmailBuilder(Register register) {
		this.register = register;
		this.subjectTemplate = EmailTemplateUtil.getEmailTemplate( EMAIL_PAYMENT_DOCKET_SUBJECT_KEY );
		this.contentTemplate = EmailTemplateUtil.getEmailTemplate( EMAIL_PAYMENT_DOCKET_CONTENT_KEY );
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
		return EmailTemplateUtil.formatMegaEventName(subjectTemplate, register);
	}
	
	@Override
	public String getContent() {
		contentTemplate = EmailTemplateUtil.formatMegaEventName(contentTemplate, register);
		contentTemplate = EmailTemplateUtil.formatRegisterId(contentTemplate, register);
		contentTemplate = EmailTemplateUtil.formatContactName(contentTemplate, register);
		contentTemplate = EmailTemplateUtil.formatEventsRows(contentTemplate, buildEventRows() );
		contentTemplate = EmailTemplateUtil.formatFooterRow(contentTemplate, buildFooterRow() );
		return contentTemplate;
	}

	
	
//utils...
	private String buildEventRows() {
		StringBuilder builder =  new StringBuilder();
		for (RegisterDetail detail : register.getDetails()) {
			builder.append( String.format(TABLE_DATA_EVENTS, 
					detail.getEventWeek().getName(), 
					detail.getEvent().getDisplayNameOrSchool() + " - "+ detail.getPresence(),
					detail.getValue(),
					detail.getCalculatedPaidValue(),
					detail.getCalculatedPendentValue()
					));
		}
		return builder.toString();
	}

	
	private String buildFooterRow() {
		StringBuilder builder =  new StringBuilder();
		builder.append( String.format(TABLE_DATA_FOOTER, 
				register.getCalculatedTotalValue(), 
				register.getCalculatedPaidValue(),
				register.getCalculatedPendentValue()
				));
		return builder.toString();
	}

}

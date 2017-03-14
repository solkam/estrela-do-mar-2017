package com.cb.estreladomar.controller.reportexporter;

import java.awt.Color;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.List;

import com.cb.estreladomar.controller.util.I18nUtil;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.RegisterDetailPayment;
import com.cb.mundo.model.exporter.pdf.PdfExporter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;

/**
 * Exportador para PDF do Informe de Pagamentos por Eventos
 * 
 * @author Solkam
 * @since 21 jun 2013
 */
public class PaymentByEventReportPdfExporter extends PdfExporter {

	protected static final Color COLOR_PENDENT_VALUE = new Color(1.0F, 0.8F, 0.8F);	
	protected static final Color COLOR_PAID_VALUE    = new Color(0.8F, 1.0F, 0.8F);	
	
	
	private final List<RegisterDetail> registerDetails;
	private BigDecimal totalValue;
	private BigDecimal totalPaidValue;
	private BigDecimal totalPendentValue;

	
	public PaymentByEventReportPdfExporter(List<RegisterDetail> registerDetails2
										 , BigDecimal totalValue
										 , BigDecimal totalPaidValue
										 , BigDecimal totalPendentValue) {
		this.registerDetails = registerDetails2;
		this.totalValue = totalValue;
		this.totalPaidValue = totalPaidValue;
		this.totalPendentValue = totalPendentValue;
	}
	

	@Override
	protected int getTableContentNumberOfColumns() {
		return 17;
	}

	@Override
	protected void addContentToFirstPage(Paragraph paragraph) {
		//nao ha conteudo para primeira pagina...
	}

	@Override
	protected void addContentToTable(PdfPTable contentTable)
			throws DocumentException, MalformedURLException, IOException {
		
		//ajuste as larguras de columnas
		contentTable.setWidths( new float[] {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 20} );

		//1.cabecalho
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_name"));
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_sex"));
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_email"));
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_country"));
		
		addHeaderCell(contentTable, COLOR_WHTIE,         "Identidade");
		addHeaderCell(contentTable, COLOR_WHTIE,         "Nascimento");
		
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_productor"));
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_root_school"));
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_register_date"));
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_checkin_date"));
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_checkout_date"));
		addHeaderCell(contentTable, COLOR_WHTIE, 		 I18nUtil.getSimpleMessage("label_event"));
		addHeaderCell(contentTable, COLOR_WHTIE, 		 I18nUtil.getSimpleMessage("label_presence"));
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_value"));
		addHeaderCell(contentTable, COLOR_PAID_VALUE,    I18nUtil.getSimpleMessage("label_paid_value"));
		addHeaderCell(contentTable, COLOR_PENDENT_VALUE, I18nUtil.getSimpleMessage("label_pendent_value"));
		addHeaderCell(contentTable, COLOR_WHTIE,         I18nUtil.getSimpleMessage("label_payments"));

		//2.conteudo
		PaymentByEventHelper helper;
		
		int counter = 0;
		Color backgroundColor;
		for (RegisterDetail detail : registerDetails) {
			
			if (++counter%2 == 0) {
				backgroundColor = COLOR_ZEBRA_BRIGHTER;
			} else {
				backgroundColor = COLOR_ZEBRA_DARKER;
			}
			
			helper = new PaymentByEventHelper( detail );
			
			addContentCell(contentTable, backgroundColor,     helper.extractContactFullDesc() );
			addContentCell(contentTable, backgroundColor,     helper.extractContactGender() );
			addContentCell(contentTable, backgroundColor,     helper.extractContactEmail() );
			addContentCell(contentTable, backgroundColor,     helper.extractContactCountry() );
			
			addContentCell(contentTable, backgroundColor,     helper.extractContactIdentityDoc() );
			addContentCell(contentTable, backgroundColor,     helper.extractContactBirthDay() );
			
			addContentCell(contentTable, backgroundColor,     helper.extractContactProductorName() );
			addContentCell(contentTable, backgroundColor,     helper.extractSchoolDescription() );
			addContentCell(contentTable, backgroundColor,     helper.extractRegisterDate() );
			addContentCell(contentTable, backgroundColor,     helper.extractCheckinDate() );
			addContentCell(contentTable, backgroundColor,     helper.extractCheckoutDate() );
			addContentCell(contentTable, backgroundColor,     helper.extractEventName() );
			addContentCell(contentTable, backgroundColor,     helper.extractEventPresenceName() );
			
			addContentCell(contentTable, backgroundColor,     helper.extractRegisterDetailValue() );
			addContentCell(contentTable, COLOR_PAID_VALUE,    helper.extractCalculatedPaidValue() );
			addContentCell(contentTable, COLOR_PENDENT_VALUE, helper.extractCalculatedPendentValue());
			addPaymentsCell(contentTable,                     detail.getPayments()                    );
		}
		
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		addHeaderCell(contentTable, COLOR_WHTIE,         totalValue.toString() );
		addHeaderCell(contentTable, COLOR_PAID_VALUE,    totalPaidValue.toString() );
		addHeaderCell(contentTable, COLOR_PENDENT_VALUE, totalPendentValue.toString() );
		addHeaderCell(contentTable, COLOR_WHTIE,         "" );
		
	}

	
	
	
	
	/**
	 * Adiciona uma tabela interna a celula com os pagamento realizados
	 * @param table
	 * @param background
	 * @param payments
	 */
	public void addPaymentsCell(PdfPTable table, List<RegisterDetailPayment> payments) {
		PdfPTable paymentTable = new PdfPTable( 5 );
		
		//1.cabecalho
		addContentCell(paymentTable, COLOR_ZEBRA_DARKER, I18nUtil.getSimpleMessage("label_payment_method") );
		addContentCell(paymentTable, COLOR_ZEBRA_DARKER, I18nUtil.getSimpleMessage("label_sale_order") );
		addContentCell(paymentTable, COLOR_ZEBRA_DARKER, I18nUtil.getSimpleMessage("label_date") );
		addContentCell(paymentTable, COLOR_ZEBRA_DARKER, I18nUtil.getSimpleMessage("label_note") );
		addContentCell(paymentTable, COLOR_ZEBRA_DARKER, I18nUtil.getSimpleMessage("label_value") );
		
		//2.conteudo
		for (RegisterDetailPayment payment : payments) {
			addContentCell(paymentTable, COLOR_ZEBRA_BRIGHTER, I18nUtil.getSimpleMessage(payment.getMethod().getKey() ));
			addContentCell(paymentTable, COLOR_ZEBRA_BRIGHTER, payment.getSaleOrder() );
			addContentCell(paymentTable, COLOR_ZEBRA_BRIGHTER, payment.getDate() );
			addCommentCell(paymentTable, COLOR_ZEBRA_BRIGHTER, payment.getNote() );
			addContentCell(paymentTable, COLOR_ZEBRA_BRIGHTER, payment.getValue() );
		}
		table.addCell( paymentTable );
	}


}

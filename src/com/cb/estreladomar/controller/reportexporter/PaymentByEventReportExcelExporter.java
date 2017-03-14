package com.cb.estreladomar.controller.reportexporter;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.cb.estreladomar.controller.util.I18nUtil;
import com.cb.mundo.model.entity.RegisterDetail;
import com.cb.mundo.model.entity.RegisterDetailPayment;
import com.cb.mundo.model.exporter.excel.SimpleExcelExporter;

/**
 * Exportador para Excel do informe de pagamento por evento
 * 
 * @author Solkam
 * @since 25 jun 2013
 */
public class PaymentByEventReportExcelExporter extends SimpleExcelExporter {

	private final List<RegisterDetail> registerDetails;
	private BigDecimal totalValue;
	private BigDecimal totalPaidValue;
	private BigDecimal totalPendentValue;

	public PaymentByEventReportExcelExporter(List<RegisterDetail> registerDetails
			, BigDecimal totalValue
			, BigDecimal totalPaidValue
			, BigDecimal totalPendentValue) {
		this.registerDetails = registerDetails;
		this.totalValue = totalValue;
		this.totalPaidValue = totalPaidValue;
		this.totalPendentValue = totalPendentValue;
	}

	@Override
	public void addContentToSheet(HSSFSheet sheet, String[] reportTitles) {
		int rowIndex = 0;
		
		//1.add report title
		rowIndex = addReportTitles(sheet, rowIndex, reportTitles);
		
		//2.add content
		rowIndex = addContentToSheet(sheet, rowIndex);
	}

	private int addContentToSheet(HSSFSheet sheet, int rowIndex) {
		//linha em branco
		rowIndex = addWhiteLine(sheet, rowIndex);
		int colIndex = 0;

		//cabecalho da tabela
		HSSFRow headerRow = sheet.createRow( rowIndex++ );
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_name"));          //1
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_sex"));           //2
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_email"));         //3
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_country"));       //4
		
		addHeaderValue( headerRow.createCell( colIndex++ ), "Identidade");     //5
		addHeaderValue( headerRow.createCell( colIndex++ ), "Nascimento");     //5

		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_productor"));     //5
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_root_school"));   //6
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_register_date")); //7
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_checkin_date"));  //8
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_checkout_date")); //9
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_event"));         //10
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_presence"));      //10

		addHeaderValue( headerRow.createCell( colIndex++ ), "Cidade" );
		addHeaderValue( headerRow.createCell( colIndex++ ), "Bairro" );
		addHeaderValue( headerRow.createCell( colIndex++ ), "Endereço" );
		addHeaderValue( headerRow.createCell( colIndex++ ), "CEP" );
		addHeaderValue( headerRow.createCell( colIndex++ ), "Telefone" );
		
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_value"));         //11
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_paid_value"));    //12
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_pendent_value")); //13

		
		addHeaderValue( headerRow.createCell( colIndex++ ), I18nUtil.getSimpleMessage("label_payments"));      //14

		
		//conteudo da tabela
		PaymentByEventHelper helper;
		
		for (RegisterDetail detail : registerDetails) {
			colIndex = 0;
			
			helper = new PaymentByEventHelper(detail);
			
			HSSFRow contentRow = sheet.createRow( rowIndex++ );
			
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractContactFullDesc() );       //1
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractContactGender() );         //2
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractContactEmail() );          //3
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractContactCountry() );        //4
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractContactIdentityDoc() );        //4
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractContactBirthDay() );        //4
			
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractContactProductorName() );  //5
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractSchoolDescription() );     //6
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractRegisterDate() );          //7
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractCheckinDate() );           //8
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractCheckoutDate() );          //9
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractEventName() );     //10
			addContentValue(     contentRow.createCell( colIndex++  ), helper.extractEventPresenceName() );     //10
			
			addContentValue( contentRow.createCell( colIndex++ ), helper.extractCity() );
			addContentValue( contentRow.createCell( colIndex++ ), helper.extractNeighborhood() );
			addContentValue( contentRow.createCell( colIndex++ ), helper.extractAddress() );
			addContentValue( contentRow.createCell( colIndex++ ), helper.extractZip() );
			addContentValue( contentRow.createCell( colIndex++ ), helper.extractTelefone() );

			addContentValue(     contentRow.createCell( colIndex++ ), helper.extractRegisterDetailValue() );   //11
			addBlueContentValue( contentRow.createCell( colIndex++ ), helper.extractCalculatedPaidValue() );   //12
			addRedContentValue(  contentRow.createCell( colIndex++ ), helper.extractCalculatedPendentValue() );//13
			
			
			
			//tabela de payments
			//(cabecalho)
			int subColIndex = colIndex;
			addHeaderValue( contentRow.createCell( subColIndex++ ), I18nUtil.getSimpleMessage("label_payment_method"));
			addHeaderValue( contentRow.createCell( subColIndex++ ), I18nUtil.getSimpleMessage("label_sale_order"));
			addHeaderValue( contentRow.createCell( subColIndex++ ), I18nUtil.getSimpleMessage("label_date"));
			addHeaderValue( contentRow.createCell( subColIndex++ ), I18nUtil.getSimpleMessage("label_note"));
			addHeaderValue( contentRow.createCell( subColIndex++ ), I18nUtil.getSimpleMessage("label_value"));
			
			//(conteudo)
			for (RegisterDetailPayment payment : detail.getPayments()) {
				subColIndex = colIndex;
				
				HSSFRow paymentRow = sheet.createRow( rowIndex++ );
				addContentValue(paymentRow.createCell( subColIndex++ ), I18nUtil.getSimpleMessage(payment.getMethod().getKey()) );
				addContentValue(paymentRow.createCell( subColIndex++ ), payment.getSaleOrder() );
				addContentValue(paymentRow.createCell( subColIndex++ ), payment.getDate() );
				addContentValue(paymentRow.createCell( subColIndex++ ), payment.getNote() );
				addContentValue(paymentRow.createCell( subColIndex++ ), payment.getValue() );
			}
			rowIndex++;

		
		
		}
		
		//rodape com os totais
		HSSFRow contentRow = sheet.createRow( rowIndex++ );
		colIndex = 18;
		
		addContentValue(     contentRow.createCell( colIndex++ ), totalValue );   
		addBlueContentValue( contentRow.createCell( colIndex++ ), totalPaidValue );   
		addRedContentValue(  contentRow.createCell( colIndex++ ), totalPendentValue );
		
		
		return rowIndex;
	}


	
	


//		HSSFCreationHelper factory = cell.getSheet().getWorkbook().getCreationHelper();
//		HSSFPatriarch patriarch = cell.getSheet().createDrawingPatriarch();
//		
//		ClientAnchor anchor = factory.createClientAnchor();
//		Comment comment = patriarch.createCellComment( anchor );
//		RichTextString richTxt = factory.createRichTextString( "payments aqui..." );
//		comment.setString(richTxt);
//		cell.setCellComment(comment);
}

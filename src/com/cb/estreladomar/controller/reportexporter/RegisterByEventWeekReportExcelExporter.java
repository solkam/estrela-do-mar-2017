package com.cb.estreladomar.controller.reportexporter;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.cb.estreladomar.controller.util.I18nUtil;
import com.cb.mundo.model.dto.EventWeekRegisterSummary;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.exporter.excel.SimpleExcelExporter;

/**
 * Exportador para Excel do Informe de Inscricao por Semana
 * 
 * @author Solkam
 * @since 22 jun 2013
 */
public class RegisterByEventWeekReportExcelExporter extends SimpleExcelExporter {
	
	private List<EventWeekRegisterSummary> summaries;

	public RegisterByEventWeekReportExcelExporter(List<EventWeekRegisterSummary> summaries) {
		this.summaries = summaries;
	}

	@Override
	public void addContentToSheet(HSSFSheet sheet, String[] reportTitles) {
		int currentRowIndex = 0;
		
		//1. adiciona titulos
		currentRowIndex = addReportTitles(sheet, currentRowIndex, reportTitles);
		
		//2 .adiciona o conteudo do informe
		currentRowIndex = addSummariesToSheet(sheet, currentRowIndex);
		
	}

	private int addSummariesToSheet(HSSFSheet sheet, int rowIndex) {
		for (EventWeekRegisterSummary summary : summaries) {
			//linha em branco	
			rowIndex = addWhiteLine(sheet, rowIndex);
			
			//Semana
			rowIndex = addWeekTitle(sheet, rowIndex, summary);
			
			//registers
			if (summary.getRegisters().isEmpty()) {
				String messageNoResultFound = I18nUtil.getSimpleMessage("msg_no_result_found");
				addMessageCell(sheet, rowIndex, messageNoResultFound);
				
			} else {
				//cabecalho da tabela
				HSSFRow headerRow = sheet.createRow( rowIndex++ );
				addHeaderValue( headerRow.createCell( 0  ), "#");
				addHeaderValue( headerRow.createCell( 1  ), I18nUtil.getSimpleMessage("label_civil_name"));
				addHeaderValue( headerRow.createCell( 2  ), I18nUtil.getSimpleMessage("label_new_name"));
				addHeaderValue( headerRow.createCell( 3  ), I18nUtil.getSimpleMessage("label_sex"));
				addHeaderValue( headerRow.createCell( 4  ), I18nUtil.getSimpleMessage("label_age"));
				addHeaderValue( headerRow.createCell( 5  ), I18nUtil.getSimpleMessage("label_country"));
				addHeaderValue( headerRow.createCell( 6  ), I18nUtil.getSimpleMessage("label_city"));
				addHeaderValue( headerRow.createCell( 7  ), I18nUtil.getSimpleMessage("label_productor"));
				addHeaderValue( headerRow.createCell( 8  ), I18nUtil.getSimpleMessage("label_status"));
				addHeaderValue( headerRow.createCell( 9  ), I18nUtil.getSimpleMessage("label_preview_checkin_date"));
				addHeaderValue( headerRow.createCell( 10 ), I18nUtil.getSimpleMessage("label_checkin_date"));
				addHeaderValue( headerRow.createCell( 11 ), I18nUtil.getSimpleMessage("label_preview_checkout_date"));
				addHeaderValue( headerRow.createCell( 12 ), I18nUtil.getSimpleMessage("label_checkout_date"));
				
				//conteudo da tabela
				for (Register register : summary.getRegisters()) {
					HSSFRow contentRow = sheet.createRow( rowIndex++ );
					addContentValue( contentRow.createCell( 0 ), register.getId() );
					addContentValue( contentRow.createCell( 1 ), register.getContact().getCivilName() );
					addContentValue( contentRow.createCell( 2 ), register.getContact().getName() );
					addContentValue( contentRow.createCell( 3 ), register.getContact().getGender() );
					addContentValue( contentRow.createCell( 4 ), I18nUtil.getSimpleMessage(register.getContact().getMaturity().getKey()) );
					addContentValue( contentRow.createCell( 5 ), register.getContact().getCountry() );
					addContentValue( contentRow.createCell( 6 ), register.getContact().getCity() );
					addContentValue( contentRow.createCell( 7 ), register.getContact().getProductorContact() );
					addContentValue( contentRow.createCell( 8 ), I18nUtil.getSimpleMessage( register.getStatus().getKey() ));
					addContentValue( contentRow.createCell( 9 ), register.getPreviewCheckinDate() );
					addContentValue( contentRow.createCell( 10 ), register.getCheckin().getCheckinDate() );
					addContentValue( contentRow.createCell( 11 ), register.getPreviewCheckoutDate() );
					addContentValue( contentRow.createCell( 12 ), register.getCheckout().getCheckoutDate() );
				}
				
				//rodape
				HSSFRow footerRow1 = sheet.createRow( rowIndex++ );
				addCentralizedValue( footerRow1.createCell( 0 ), I18nUtil.getSimpleMessage("label_age_child_number")   );
				addCentralizedValue( footerRow1.createCell( 1 ), I18nUtil.getSimpleMessage("label_age_young_number")   );
				addCentralizedValue( footerRow1.createCell( 2 ), I18nUtil.getSimpleMessage("label_age_adult_number")   );
				addCentralizedValue( footerRow1.createCell( 3 ), I18nUtil.getSimpleMessage("label_age_ancient_number") );
				addCentralizedValue( footerRow1.createCell( 4 ), I18nUtil.getSimpleMessage("label_age_unknow_number")  );
				addCentralizedValue( footerRow1.createCell( 5 ), I18nUtil.getSimpleMessage("label_age_total_number")   );
				
				HSSFRow footerRow2 = sheet.createRow( rowIndex++ );
				addCentralizedValue( footerRow2.createCell( 0 ), summary.getNumberOfChild()   );
				addCentralizedValue( footerRow2.createCell( 1 ), summary.getNumberOfYoung()   );
				addCentralizedValue( footerRow2.createCell( 2 ), summary.getNumberOfAdult()   );
				addCentralizedValue( footerRow2.createCell( 3 ), summary.getNumberOfAncient() );
				addCentralizedValue( footerRow2.createCell( 4 ), summary.getNumberOfUnknown() );
				addCentralizedValue( footerRow2.createCell( 5 ), summary.getNumberOfTotal()   );
				
			}
		}
		return rowIndex;
	}

	private void addCentralizedValue(HSSFCell cell, int number) {
		cell.setCellValue( number );
		addCentralizedStyle( cell );
	}

	protected void addCentralizedValue(HSSFCell cell, String txt) {
		cell.setCellValue( txt );
		addCentralizedStyle( cell );
	}

	
	

	private int addWeekTitle(HSSFSheet sheet, int rowIndex,	EventWeekRegisterSummary summary) {
		HSSFCell weekCell = sheet.createRow(rowIndex++).createCell( 0 );
		addDestak1Value( weekCell, summary.getEventWeek().getName() );
		return rowIndex;
	}
	
	private int addMessageCell(HSSFSheet sheet, int rowIndex, String message) {
		HSSFCell messageCell = sheet.createRow(rowIndex++).createCell( 0 );
		addDestak2Value( messageCell, message );
		return rowIndex;
	}

	

}

package com.cb.estreladomar.controller.reportexporter;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.cb.estreladomar.controller.util.I18nUtil;
import com.cb.mundo.model.dto.EventWeekRegisterSummary;
import com.cb.mundo.model.entity.Register;
import com.cb.mundo.model.exporter.pdf.PdfExporter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * Exportador de PDF para Informe de Pessoa por Semana
 *  
 * @author Solkam
 * @since 25 JUL 2013
 */
public class RegisterByEventWeekReportPdfExporter extends PdfExporter {

	private static final float[] REGISTER_TABLE_WIDTHS = new float[]{5,10,20,5,10,10,10,10,10,10};
	
	
	private List<EventWeekRegisterSummary> summaries;
	
	public RegisterByEventWeekReportPdfExporter(List<EventWeekRegisterSummary> summaries) {
		this.summaries = summaries;
	}

	
	@Override
	protected void addContentToFirstPage(Paragraph firstPageParagraph) {
		//1.semanas selecionadas como filtro:
		StringBuilder semanas = new StringBuilder();
		boolean firstLoop = true;
		for (EventWeekRegisterSummary summary : summaries) {
			if (!firstLoop)
				semanas.append(", ");
			else
				firstLoop = false;

			semanas.append( summary.getEventWeek().getName() );
		}
		addTextOnParagraph(firstPageParagraph, "Semanas: "+semanas);
	}
	
	
	/**
	 * A tabela de conteudo ter� apenas uma coluna.
	 * A tabela interna (com registros) ter� 10 colunas
	 */
	@Override
	protected int getTableContentNumberOfColumns() {
		return 1;
	}
	
	/**
	 * Adiciona a tabela de registers de cada semana do informe
	 */
	@Override
	protected void addContentToTable(PdfPTable contentTable) 
			throws DocumentException, MalformedURLException, IOException {

		for (EventWeekRegisterSummary summary: summaries) {

			//tabela com as inscri��es:
			PdfPTable registerTable = createEmptyTable( REGISTER_TABLE_WIDTHS );

			//celula titulo (semana)
			String weekTitle = summary.getEventWeek().getName();
			addTitleCell(registerTable, weekTitle, REGISTER_TABLE_WIDTHS.length );
			
			if (summary.getRegisters().isEmpty()) {//nao tem inscri��es
				String messageNoResultFound = I18nUtil.getSimpleMessage("msg_no_result_found");
				addTitleCell(registerTable, messageNoResultFound, REGISTER_TABLE_WIDTHS.length );
				
			} else {//tem inscri��es
				
				//1.cabecalho da tabela
				addHeaderCell(registerTable, COLOR_WHTIE, "#");
				addHeaderCell(registerTable, COLOR_WHTIE, I18nUtil.getSimpleMessage("label_new_name"));
				addHeaderCell(registerTable, COLOR_WHTIE, I18nUtil.getSimpleMessage("label_civil_name"));
				addHeaderCell(registerTable, COLOR_WHTIE, I18nUtil.getSimpleMessage("label_sex"));
				addHeaderCell(registerTable, COLOR_WHTIE, I18nUtil.getSimpleMessage("label_country"));
				addHeaderCell(registerTable, COLOR_WHTIE, I18nUtil.getSimpleMessage("label_city"));
				addHeaderCell(registerTable, COLOR_WHTIE, I18nUtil.getSimpleMessage("label_productor"));
				addHeaderCell(registerTable, COLOR_WHTIE, I18nUtil.getSimpleMessage("label_checkin_date"));
				addHeaderCell(registerTable, COLOR_WHTIE, I18nUtil.getSimpleMessage("label_checkout_date"));
				addHeaderCell(registerTable, COLOR_WHTIE, I18nUtil.getSimpleMessage("label_age"));

				//2.conteudo da tabela
				
				Color backgroundColor = null;
				int contadorLinha = 0;
				
				for (Register register : summary.getRegisters() ) {
				
					//zebra control
					if (++contadorLinha % 2 == 0) {
						backgroundColor = COLOR_ZEBRA_BRIGHTER;
					} else {
						backgroundColor = COLOR_ZEBRA_DARKER;
					}
					
					addCellRegisterId(registerTable, backgroundColor, register);
					addCellNewName(registerTable, backgroundColor, register);
					addCellCivilName(registerTable, backgroundColor, register);
					addCellGender(registerTable, backgroundColor, register);
					addCellCountry(registerTable, backgroundColor, register);
					addCellCity(registerTable, backgroundColor, register);
					addCellProductor(registerTable, backgroundColor, register);
					addCellCheckinDate(registerTable, backgroundColor, register);
					addCellCheckoutDate(registerTable, backgroundColor, register);
					addCellMaturity(registerTable, backgroundColor, register);
				}
				
				//3. rodape da tabela
				addFooterCell(registerTable, summary);
			}

			//...finalmente adiciona a tabela de conteudo
			contentTable.addCell(registerTable);
		}
	}

	private void addFooterCell(PdfPTable registerTable, EventWeekRegisterSummary summary) {
		PdfPTable maturitySummaryTable = new PdfPTable( 6 );
		
		addCentralizedCell( maturitySummaryTable, I18nUtil.getSimpleMessage("label_age_child_number")   );
		addCentralizedCell( maturitySummaryTable, I18nUtil.getSimpleMessage("label_age_young_number")   );
		addCentralizedCell( maturitySummaryTable, I18nUtil.getSimpleMessage("label_age_adult_number")   );
		addCentralizedCell( maturitySummaryTable, I18nUtil.getSimpleMessage("label_age_ancient_number") );
		addCentralizedCell( maturitySummaryTable, I18nUtil.getSimpleMessage("label_age_unknow_number")  );
		addCentralizedCell( maturitySummaryTable, I18nUtil.getSimpleMessage("label_age_total_number")   );
		
		addCentralizedCell( maturitySummaryTable, summary.getNumberOfChild()   );
		addCentralizedCell( maturitySummaryTable, summary.getNumberOfYoung()   );
		addCentralizedCell( maturitySummaryTable, summary.getNumberOfAdult()   );
		addCentralizedCell( maturitySummaryTable, summary.getNumberOfAncient() );
		addCentralizedCell( maturitySummaryTable, summary.getNumberOfUnknown() );
		addCentralizedCell( maturitySummaryTable, summary.getNumberOfTotal()   );

		PdfPCell footerCell = new PdfPCell(maturitySummaryTable);
		footerCell.setColspan( 10 );
		registerTable.addCell( footerCell );
	}



	private void addCellMaturity(PdfPTable table, Color backgroundColor, Register register) {
		String maturityDescription = I18nUtil.getSimpleMessage( register.getContact().getMaturity().getKey() );
		PdfPCell cellMaturity     = createCommonCell( maturityDescription ); 
		cellMaturity.setBackgroundColor( backgroundColor );
		table.addCell( cellMaturity );
	}

	private void addCellCheckoutDate(PdfPTable table, Color backgroundColor, Register register) {
		PdfPCell cellCheckout     = createCommonCell( register.getCheckout().getCheckoutDate() ); 
		cellCheckout.setBackgroundColor( backgroundColor );
		table.addCell( cellCheckout );
	}

	private void addCellCheckinDate(PdfPTable table, Color backgroundColor, Register register) {
		PdfPCell cellCheckin     = createCommonCell( register.getCheckin().getCheckinDate() );
		cellCheckin.setBackgroundColor( backgroundColor );
		table.addCell( cellCheckin );
	}

	private void addCellProductor(PdfPTable table, Color backgroundColor, Register register) {
		String productName = EMPTY_STRING;
		if (register.getContact().getProductorContact()!=null ) {
			productName = register.getContact().getProductorContact().getShortDesc();
		}
		PdfPCell cellProductor     = createCommonCell( productName );
		cellProductor.setBackgroundColor( backgroundColor );
		table.addCell( cellProductor );
	}

	private void addCellCity(PdfPTable table, Color backgroundColor, Register register) {
		PdfPCell cellContactCity     = createCommonCell( register.getContact().getCity() );
		cellContactCity.setBackgroundColor( backgroundColor );
		table.addCell( cellContactCity );
	}

	private void addCellCountry(PdfPTable table, Color backgroundColor, Register register) {
		PdfPCell cellContactCountry  = createCommonCell( register.getContact().getCountry() );
		cellContactCountry.setBackgroundColor( backgroundColor );
		table.addCell( cellContactCountry );
	}

	private void addCellGender(PdfPTable table, Color backgroundColor, Register register) {
		String genderDesc = EMPTY_STRING;
		if (register.getContact().getGender()!=null) {
			genderDesc = register.getContact().getGender().name();
		}
		PdfPCell cellGender     = createCommonCell( genderDesc );
		cellGender.setBackgroundColor( backgroundColor );
		table.addCell( cellGender );
	}

	private void addCellCivilName(PdfPTable table, Color backgroundColor, Register register) {
		PdfPCell cellContactCivilName     = createCommonCell( register.getContact().getCivilName() );
		cellContactCivilName.setBackgroundColor( backgroundColor );
		table.addCell( cellContactCivilName );
	}

	private void addCellNewName(PdfPTable table, Color backgroundColor, Register register) {
		PdfPCell cellContactName     = createCommonCell( register.getContact().getName() );
		cellContactName.setBackgroundColor( backgroundColor );
		table.addCell( cellContactName );
	}

	private void addCellRegisterId(PdfPTable table,	Color backgroundColor, Register register) {
		PdfPCell cellId = createCommonCell( register.getId() );
		cellId.setBackgroundColor( backgroundColor );
		table.addCell( cellId );
	}
	

}

package com.cb.estreladomar.controller.reportexporter;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.cb.mundo.model.dto.ComissionDTO;
import com.cb.mundo.model.dto.ComissionDetailDTO;
import com.cb.mundo.model.exporter.excel.SimpleExcelExporter;

/**
 * Exportador para Relatorio de comissoes
 * @author Solkam
 * @since 17 AGO 2015
 */
public class ReportComisionExcelExporter extends SimpleExcelExporter  {

	private List<ComissionDTO> dtos;
	
	public ReportComisionExcelExporter(List<ComissionDTO> comissionDtos) {
		super();
		this.dtos = comissionDtos;
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

		HSSFRow headerRow = sheet.createRow( rowIndex++ );
		addHeaderValue( headerRow.createCell( colIndex++ ), "Nome Civil do Produtor "); 
		addHeaderValue( headerRow.createCell( colIndex++ ), "Nome Novo do Produtor ");   
		addHeaderValue( headerRow.createCell( colIndex++ ), "Escola Raiz ");   
		addHeaderValue( headerRow.createCell( colIndex++ ), "Total comissão");
		addHeaderValue( headerRow.createCell( colIndex++ ), "Participante"); 
		addHeaderValue( headerRow.createCell( colIndex++ ), "Evento");    
		addHeaderValue( headerRow.createCell( colIndex++ ), "Valor do Evento");     
		addHeaderValue( headerRow.createCell( colIndex++ ), "Comissão gerada");
		addHeaderValue( headerRow.createCell( colIndex++ ), "Compartilhada?");

		for (ComissionDTO dtoVar : dtos) {
			
			for (ComissionDetailDTO detailDtoVar : dtoVar.getDetailDtos()) {
				colIndex = 0;
				HSSFRow contentRow = sheet.createRow( rowIndex++ );
				addContentValue(     contentRow .createCell( colIndex++ ), extractProdutorCivilName(dtoVar) );
				addContentValue(     contentRow .createCell( colIndex++ ), extractProdutorNewName(dtoVar) );
				addContentValue(     contentRow .createCell( colIndex++ ), extractProdutorRootSchool(dtoVar) );
				addBlueContentValue( contentRow .createCell( colIndex++ ), extractTotalComission(dtoVar) );
			
				addContentValue(     contentRow .createCell( colIndex++ ), extractParticipanteCivilName(detailDtoVar) );
				addContentValue(     contentRow .createCell( colIndex++ ), extractEvent(detailDtoVar) );
				addContentValue(     contentRow .createCell( colIndex++ ), extractEventValue(detailDtoVar) );
				addBlueContentValue( contentRow .createCell( colIndex++ ), extractComissionValue(detailDtoVar) );
				addContentValue(     contentRow .createCell( colIndex++ ), extractFlagShared(detailDtoVar) );
			}
		}
		return rowIndex;
	}



	

	private String extractProdutorRootSchool(ComissionDTO dtoVar) {
		return dtoVar.getProductorContact().getRootSchool().getDescription();
	}




	private String extractProdutorCivilName(ComissionDTO dtoVar) {
		return dtoVar.getProductorContact().getCivilName();
	}

	private String extractProdutorNewName(ComissionDTO dtoVar) {
		return dtoVar.getProductorContact().getName();
	}

	private BigDecimal extractTotalComission(ComissionDTO dtoVar) {
		return dtoVar.getCalculatedTotalComission();
	}

	private String extractParticipanteCivilName(ComissionDetailDTO detailDtoVar) {
		return detailDtoVar.getParticipantContact().getShortDesc();
	}

	private String extractEvent(ComissionDetailDTO detailDtoVar) {
		return detailDtoVar.getEvent().getDisplayNameOrSchool();
	}

	private BigDecimal extractEventValue(ComissionDetailDTO detailDtoVar) {
		return detailDtoVar.getEvent().getValueParticipant();
	}

	private BigDecimal extractComissionValue(ComissionDetailDTO detailDtoVar) {
		return detailDtoVar.getCalculatedComissionValue();
	}

	private String extractFlagShared(ComissionDetailDTO detailDtoVar) {
		if (detailDtoVar.getFlagShared()) {
			return "Sim";
		} else {
			return "Não";
		}
	}

}

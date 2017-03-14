package com.cb.estreladomar.controller.reportexporter;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.cb.estreladomar.controller.util.I18nUtil;
import com.cb.mundo.model.dto.MedicalAnswerDTO;
import com.cb.mundo.model.entity.MedicalAnswer;
import com.cb.mundo.model.entity.enumeration.ContactMaturity;
import com.cb.mundo.model.entity.enumeration.Idiom;
import com.cb.mundo.model.entity.enumeration.School;
import com.cb.mundo.model.exporter.excel.SimpleExcelExporter;

/**
 * Exportador para Excel para o relatorio de fichas medicas
 * @author Solkam
 * @since 29 ABR 2015
 */
public class ReportMedicalFormExcelExporter extends SimpleExcelExporter {
	
	private static final String EMPTY_STRING = "";


	private final List<MedicalAnswerDTO> dtos;
	private final Idiom idiom;
	
	public ReportMedicalFormExcelExporter(List<MedicalAnswerDTO> dtos, Idiom idiom) {
		super();
		this.dtos = dtos;
		this.idiom = idiom;
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

		for (MedicalAnswerDTO dtoVar : dtos) {
	
			//1.tabela de cabecalho (com dados pessoais)
			HSSFRow headerRow = sheet.createRow( rowIndex++ );
			addHeaderValue( headerRow.createCell( 0  ), I18nUtil.getSimpleMessage("label_civil_name")); 
			addHeaderValue( headerRow.createCell( 1  ), I18nUtil.getSimpleMessage("label_new_name"));   
			addHeaderValue( headerRow.createCell( 2  ), I18nUtil.getSimpleMessage("label_root_school"));
			addHeaderValue( headerRow.createCell( 3  ), I18nUtil.getSimpleMessage("label_country")); 
			addHeaderValue( headerRow.createCell( 4  ), I18nUtil.getSimpleMessage("label_city"));    
			addHeaderValue( headerRow.createCell( 5  ), I18nUtil.getSimpleMessage("label_age"));     
			addHeaderValue( headerRow.createCell( 6  ), I18nUtil.getSimpleMessage("label_maturity"));
			addHeaderValue( headerRow.createCell( 7  ), I18nUtil.getSimpleMessage("label_status"));
			addHeaderValue( headerRow.createCell( 8  ), I18nUtil.getSimpleMessage("label_physical_limitation"));
			
			headerRow = sheet.createRow( rowIndex++ );
			addBlueContentValue( headerRow.createCell( 0  ), extractCivilName(dtoVar) ); 
			addBlueContentValue( headerRow.createCell( 1  ), extractNewName(dtoVar) );   
			addBlueContentValue( headerRow.createCell( 2  ), extractRootSchool(dtoVar) );      
			addBlueContentValue( headerRow.createCell( 3  ), extractCountry(dtoVar) );
			addBlueContentValue( headerRow.createCell( 4  ), extractCity(dtoVar) ); 
			addBlueContentValue( headerRow.createCell( 5  ), extractAge(dtoVar) );    
			addBlueContentValue( headerRow.createCell( 6  ), extractMaturity(dtoVar) );     
			addBlueContentValue( headerRow.createCell( 7  ), extractRegisterStatus(dtoVar) );
			addBlueContentValue( headerRow.createCell( 8  ), extractFlagPhysicalLimitation(dtoVar) );

			//2.questionario 
			HSSFRow contentRow = null;
			if (dtoVar.getAnswers()==null || dtoVar.getAnswers().isEmpty() ) {
				contentRow = sheet.createRow( rowIndex++ );
				addRedContentValue(contentRow.createCell( 0  ), I18nUtil.getSimpleMessage("msg_no_medical_form") );
				
			} else {
				contentRow = sheet.createRow( rowIndex++ );
				addHeaderValue( contentRow.createCell( 0  ), I18nUtil.getSimpleMessage("label_question") ); 
				addHeaderValue( contentRow.createCell( 1  ), I18nUtil.getSimpleMessage("label_answer") );   
				addHeaderValue( contentRow.createCell( 2  ), I18nUtil.getSimpleMessage("label_treatment") );
				
				for (MedicalAnswer answerVar : dtoVar.getAnswers()) {
					contentRow = sheet.createRow( rowIndex++ );
					
					addContentValue( contentRow.createCell( 0 ), extractQuestion(answerVar) );
					if (answerVar.getBooleanAnswer()) {
						addRedContentValue( contentRow.createCell( 1 ), extractBooleanAnswer(answerVar) );
					}else {
						addContentValue( contentRow.createCell( 1 ), extractBooleanAnswer(answerVar) );
					}
					addRedContentValue( contentRow.createCell( 2 ), extractTextAnswer(answerVar) );
				}
			}
			rowIndex = addWhiteLine(sheet, rowIndex);
			rowIndex = addWhiteLine(sheet, rowIndex);
		}
		return rowIndex;
	}


	private String extractBooleanAnswer(MedicalAnswer answerVar) {
		if (answerVar.getBooleanAnswer()) {
			return I18nUtil.getSimpleMessage("label_yes");
		} else {
			return I18nUtil.getSimpleMessage("label_no");
		}
	}

	private String extractTextAnswer(MedicalAnswer answerVar) {
		return answerVar.getTextAnswer();
	}


	private String extractQuestion(MedicalAnswer answerVar) {
		return answerVar.getQuestion().displayTextByLocale(idiom.getLocale());
	}


	
	private String extractCivilName(MedicalAnswerDTO dtoVar) {
		return dtoVar.getRegister().getContact().getCivilName();
	}

	private String extractNewName(MedicalAnswerDTO dtoVar) {
		String newName = dtoVar.getRegister().getContact().getName();
		return newName!=null ? newName : EMPTY_STRING;
	}

	private String extractRootSchool(MedicalAnswerDTO dtoVar) {
		School rootSchool = dtoVar.getRegister().getContact().getRootSchool();
		return rootSchool!=null ? I18nUtil.getSimpleMessage(rootSchool.getKey()) : EMPTY_STRING;
	}

	private String extractCountry(MedicalAnswerDTO dtoVar) {
		String country = dtoVar.getRegister().getContact().getCountry();
		return country!=null ? country : EMPTY_STRING;
	}

	private String extractCity(MedicalAnswerDTO dtoVar) {
		String city = dtoVar.getRegister().getContact().getCity();
		return city!=null ? city : EMPTY_STRING;
	}


	private String extractAge(MedicalAnswerDTO dtoVar) {
		return String.valueOf( dtoVar.getRegister().getContact().getAge() );
	}


	private String extractMaturity(MedicalAnswerDTO dtoVar) {
		ContactMaturity maturity = dtoVar.getRegister().getContact().getMaturity();
		return maturity!=null ? I18nUtil.getSimpleMessage(maturity.getKey()) : EMPTY_STRING;
	}


	private String extractRegisterStatus(MedicalAnswerDTO dtoVar) {
		return I18nUtil.getSimpleMessage( dtoVar.getRegister().getStatus().getKey() );
	}

	private String extractFlagPhysicalLimitation(MedicalAnswerDTO dtoVar) {
		if (dtoVar.getFlagPhysicalLimitation()) {
			return I18nUtil.getSimpleMessage("label_yes");
		} else {
			return I18nUtil.getSimpleMessage("label_no");
		}
	}

}

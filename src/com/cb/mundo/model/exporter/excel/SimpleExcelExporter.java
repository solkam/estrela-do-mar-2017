package com.cb.mundo.model.exporter.excel;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cb.mundo.model.exporter.HttpResponseConfigurator;

/**
 * Template Classe Exportador para Excel.
 * Trabalha com planilha com unica aba.
 * @author Solkam
 * @since 21 jun 2013
 */
public abstract class SimpleExcelExporter extends AbstractExcelExporter {
	
	public void export(String[] reportTitles, String filenameWithoutExtension) {
		try {
			//1.pre.processamento
			filenameWithoutExtension = filenameWithoutExtension + ".xls";
			HttpServletResponse response = HttpResponseConfigurator.configResponseForAttachmentDisposition(filenameWithoutExtension);
			
			//2.processamento
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			
			addContentToSheet(sheet, reportTitles);
			
			//3.pos.processamento
			OutputStream os = response.getOutputStream(); 
			workbook.write(os);
			
			response.setContentType("application/vnd.ms-excel");
			
			os.flush();
			os.close();
			
		} catch (IOException e) {
			throw new RuntimeException( e );
		}
	}
	
	/**
	 * Template Method
	 * @param workbook
	 * @param reportTitles
	 */
	public abstract void addContentToSheet(HSSFSheet sheet, String[] reportTitles);


}

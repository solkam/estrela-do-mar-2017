package com.cb.mundo.model.exporter.excel;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.cb.mundo.model.exporter.HttpResponseConfigurator;

/**
 * Template para Exporter de Excel com planilha de multiplas abas
 * @author Solkam
 * @since 17 MAI 2015
 */
public abstract class MultipleExcelExporter extends AbstractExcelExporter {

	public void export(String filename) {
		try {
			//1.pre.processamento
			HttpServletResponse response = HttpResponseConfigurator.configResponseForAttachmentDisposition(filename);
			
			//2.processamento
			HSSFWorkbook workbook = new HSSFWorkbook();
			
			addContentToWorkbook(workbook);
			
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
	public abstract void addContentToWorkbook(HSSFWorkbook workbook);
	
	
}

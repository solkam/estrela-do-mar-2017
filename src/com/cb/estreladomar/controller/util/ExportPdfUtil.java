package com.cb.estreladomar.controller.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.cb.mundo.model.dto.EventWeekRegisterSummary;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Utilitario para exportar tabela para arquivos PDF
 * 
 * @author Solkam
 * @since 25 JUL 2013
 */
@ManagedBean(name="exportPdfUtil")
public class ExportPdfUtil {
	
	private static final String imageCBLogo = "logo_cb_principal.png";

	public void exportReportRegisterByWeek(List<EventWeekRegisterSummary> summaries) {
		final String reportName = "Informe Personas por Semana";
		final String filename = "InformePersonasPorSemana.pdf";
		
		buildPDF(summaries, reportName, filename);
	}
	

	private void buildPDF(List<EventWeekRegisterSummary> summaries, String reportName, String filename) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

			response.setHeader("Content-disposition", "inline=filename=" + filename);
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",	"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");

			Document document = new Document();
			document.setPageSize(PageSize.A4.rotate());
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			PdfWriter.getInstance(document, baos);
			document.open();

				
			PdfPTable tabelaCabecalho = getHeaderTable(reportName);

			//colocar cabecalho na primeira pagina
			document.add(tabelaCabecalho);

			//tabela de OMs
			PdfPTable summaryTable = new PdfPTable(1); // 5 columns.
			summaryTable.setWidthPercentage(100);
			summaryTable.setWidths( new int[]{100} );
			summaryTable.getDefaultCell().setPadding(10);

			//valores padr�o para cabecalho
			final Font  HEADER_FONT_DEFAULT = FontFactory.getFont("VERDANA", 12, Font.BOLD);
			final float HEADER_PADDING_DEFAULT = 5;
			
			
			//cabe�alho da tabela:
			PdfPCell cellHeaderWeek   = new PdfPCell( new Phrase("Semana" ,HEADER_FONT_DEFAULT ));
			
			//configura o padding
			cellHeaderWeek.setPadding(  HEADER_PADDING_DEFAULT );
			
			
			summaryTable.addCell( cellHeaderWeek );
			
			summaryTable.getDefaultCell().setBorder( 0 );
			
			//valores padroes para o conteudo;
			final Font  CONTEUDO_FONT_PADRAO = FontFactory.getFont("HELVETICA", 10, Font.NORMAL );
			final float CONTEUDO_PADDING_PADRAO = 5;
			
			Color conteudoBackgroundColor = null;
			
			int contadorLinha = 0;
			for (EventWeekRegisterSummary omLinha: summaries) {

				if (++contadorLinha % 2 == 0) {
					conteudoBackgroundColor = new Color(0.95F, 0.95F, 0.95F);
				} else {
					conteudoBackgroundColor = new Color(0.75F, 0.75F, 0.75F);
				}
				
				//conteudo das colunas
				PdfPCell celulaSigla  = new PdfPCell(new Phrase( omLinha.getEventWeek().getName(),           CONTEUDO_FONT_PADRAO ));
				
				celulaSigla.setPadding(  CONTEUDO_PADDING_PADRAO );
				
				celulaSigla.setBackgroundColor( conteudoBackgroundColor );
				
				summaryTable.addCell( celulaSigla );
			}

			document.add(summaryTable);

			document.close();
			
			OutputStream os = response.getOutputStream();
			baos.writeTo(os);
			
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			// write ByteArrayOutputStream to the ServletOutputStream

			os.flush();
			os.close();

			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		
		} catch (DocumentException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
//	private Document openDocument(String filename) throws DocumentException {
//		FacesContext context = FacesContext.getCurrentInstance();
//		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//
//		response.setHeader("Content-disposition", "inline=filename=" + filename);
//		response.setHeader("Expires", "0");
//		response.setHeader("Cache-Control",	"must-revalidate, post-check=0, pre-check=0");
//		response.setHeader("Pragma", "public");
//
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//		Document document = new Document();
//		document.setPageSize(PageSize.A4.rotate());
//		
//		PdfWriter.getInstance(document, baos);
//		document.open();
//		
//		return document;
//	}
//	
//	private void closeDocument(Document document, ServletResponse response, ByteArrayOutputStream baos) throws IOException {
//		document.close();
//		
//		OutputStream os = response.getOutputStream();
//		baos.writeTo(os);
//		
//		response.setContentType("application/pdf");
////		response.setContentLength(baos.size());
//		// write ByteArrayOutputStream to the ServletOutputStream
//
//		os.flush();
//		os.close();
//	
//		
//	}

	
	private PdfPTable getHeaderTable(String reportName) throws DocumentException, MalformedURLException, IOException {
		// Monta uma tabela com 3 colunas
		PdfPTable tabelaCabecalho = new PdfPTable( 2 );
		
		tabelaCabecalho.setWidthPercentage(100);
		tabelaCabecalho.setWidths(new int[] { 10, 90 });

		tabelaCabecalho.getDefaultCell().setPadding(2);
		
		tabelaCabecalho.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		tabelaCabecalho.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
		tabelaCabecalho.setSpacingAfter(15);

		// celula 1 (imagem CB)
		tabelaCabecalho.addCell( Image.getInstance( getLogoCBPath() ));

		// celula 2 (Texto)
		tabelaCabecalho.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable centralText = new PdfPTable(1);
		centralText.getDefaultCell().setPadding(2);
		centralText.getDefaultCell().setBorder(0);
		centralText.addCell( new Phrase( reportName ) );
		tabelaCabecalho.addCell(centralText);

		return tabelaCabecalho;
	}

	
	
	private String getLogoCBPath() {
		return getDiretorioImagens() + imageCBLogo;
	}

	private String getDiretorioImagens() {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

		String diretorioImagens = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "img" + File.separator;
		return diretorioImagens;
	}


}

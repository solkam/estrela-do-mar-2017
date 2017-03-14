package com.cb.mundo.model.exporter.pdf;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Date;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.cb.mundo.model.exporter.HttpResponseConfigurator;
import com.cb.mundo.model.util.CalendarUtil;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Template Exportador para PDF
 * @author Solkam
 * @since 20 jun 2013
 */
public abstract class PdfExporter {

	private static final Font FONT_6_NORMAL      = FontFactory.getFont(FontFactory.HELVETICA, 6,  Font.NORMAL );
	private static final Font FONT_8_NORMAL      = FontFactory.getFont(FontFactory.HELVETICA, 8,  Font.NORMAL );
	private static final Font FONT_10_BOLD       = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD );
	private static final Font FONT_14_BOLDITALIC = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLDITALIC);


	private static final String IMAGE_CB_LOGO = "logo_cb_principal.png";
	
	protected static final float PADDING_DEFAULT = 5F;
	protected static final float MARGIN_DEFAULT = 15F;
	
	protected static final String EMPTY_STRING = "";
	
	protected static final Color COLOR_WHTIE          = new Color(1.00F, 1.00F, 1.00F);	
	protected static final Color COLOR_ZEBRA_BRIGHTER = new Color(0.99F, 0.99F, 0.99F);	
	protected static final Color COLOR_ZEBRA_DARKER   = new Color(0.90F, 0.90F, 0.90F);	
	

	
	public void export(String[] reportTitles, String filename) {
		try {
			//0.configura response com cabecalhos para export
			HttpServletResponse response = HttpResponseConfigurator.configResponseForAttachmentDisposition(filename);

			//1.open document
			Document document = new Document();
			
			//ajustes...
			document.setPageSize( PageSize.A4.rotate() );
			document.setMargins(MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_DEFAULT);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			writer.setPageEvent( new FooterPaginatorEvent(reportTitles) );
			
			document.open();

			//2.adiciona conteido a primeira pagina:
			Paragraph firstPageParagragh = createEmptyParagraph();
			firstPageParagragh.setSpacingBefore( 10.0F ); //adiciona espaco antes
			firstPageParagragh.setSpacingAfter(  10.0F ); //adiciona espaco apes

			addContentToFirstPage( firstPageParagragh );//template
			document.add( firstPageParagragh );

			//3.adiciona o conteudo do informa em tabela
			PdfPTable contentTable = createEmptyContentTable();
			addContentToTable( contentTable );//template
			document.add( contentTable );

			//4.close document
			document.close();
			
			// write ByteArrayOutputStream to the ServletOutputStream
			OutputStream os = response.getOutputStream();
			baos.writeTo(os);
			
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());

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


	/**
	 * Metodo de Template 1:
	 * Solicita o numero de colunas que a tabela de conteudo tera.
	 * Isso e necessario pois o numero de colunas e parametro do construtor.
	 * devera ter.
	 * @return
	 */
	protected abstract int getTableContentNumberOfColumns();

	/**
	 * Metodo de Template 2:
	 * Adiciona conteudo a tabela.
	 * @param contentTable
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	protected abstract void addContentToTable(PdfPTable contentTable) 
			throws DocumentException, MalformedURLException, IOException;

	/**
	 * Metoto de Template 3:
	 * Adiciona um paragrafo na primeira pagina.
	 * Na verdade, a primeira pagina esta vindo em branco e entao
	 * sera usada para colocar algum texto.
	 * @param paragraph
	 */
	protected abstract void addContentToFirstPage(Paragraph paragraph);

	
//metodos privados...
	
	private Paragraph createEmptyParagraph() {
		Paragraph emptyParagraph = new Paragraph();
		emptyParagraph.setAlignment( Element.ALIGN_LEFT );
		return emptyParagraph;
	}
	
	
	private PdfPTable createEmptyContentTable() throws DocumentException {
		PdfPTable emptyTable = new PdfPTable( getTableContentNumberOfColumns() );
		emptyTable.setSpacingBefore( 0F );
		emptyTable.setWidthPercentage( 100 );
		emptyTable.getDefaultCell().setPadding( PADDING_DEFAULT );
		return emptyTable;
	}
	
	protected PdfPTable createEmptyTable(float[] widths) {
		PdfPTable emptyTable = new PdfPTable( widths );
		emptyTable.setWidthPercentage( 100 );
		emptyTable.getDefaultCell().setPadding( PADDING_DEFAULT );
		return emptyTable;
	}
	
	
//metodo utilitarios...
	
	protected PdfPCell createDestakCell(String txt) {
		Phrase phrase = new Phrase( txt, FONT_10_BOLD);
		return new PdfPCell(phrase);
	}

	protected void addHeaderCell(PdfPTable table, Color backgroundColor, String txt) {
		PdfPCell cell = createDestakCell(txt);
		cell.setBackgroundColor( backgroundColor );
		table.addCell( cell );
	}

	protected void addTitleCell(PdfPTable table, String txt, int colspan) {
		Phrase phrase = new Phrase( txt, FONT_14_BOLDITALIC );
		PdfPCell titleCell = new PdfPCell( phrase );
		titleCell.setColspan( colspan );
		table.addCell( titleCell );
	}
	
	
	protected void addContentCell(PdfPTable contentTable, Color background, String txt) {
		PdfPCell cell = createCommonCell(txt);
		cell.setBackgroundColor(background);
		contentTable.addCell( cell );
	}

	protected void addContentCell(PdfPTable contentTable, Color background, Long number) {
		PdfPCell cell = createCommonCell( number );
		cell.setBackgroundColor(background);
		contentTable.addCell( cell );
	}

	protected void addContentCell(PdfPTable contentTable, Color background, Date date) {
		PdfPCell cell = createCommonCell( date );
		cell.setBackgroundColor(background);
		contentTable.addCell( cell );
	}

	protected void addContentCell(PdfPTable contentTable, Color backgroundColor, BigDecimal value) {
		PdfPCell cell = createCommonCell( value.toPlainString() );
		cell.setBackgroundColor(backgroundColor);
		contentTable.addCell( cell );
	}

//	protected void addContentCell(PdfPTable contentTable, Color backgroundColor, EventPresence presence) {
//		String presenceDesc = I18nUtil.getSimpleMessage( presence.getKey() );
//		addContentCell(contentTable, backgroundColor, presenceDesc );
//	}
	
//	protected void addContentCell(PdfPTable contentTable, Color backgroundColor, MegaEventPaymentMethod method) {
//		String presenceDesc = I18nUtil.getSimpleMessage( method.getKey() );
//		addContentCell(contentTable, backgroundColor, presenceDesc );
//	}
	

	protected void addCommentCell(PdfPTable contentTable, Color background, String txt) {
		PdfPCell cell = createCommentCell(txt);
		cell.setBackgroundColor(background);
		contentTable.addCell( cell );
	}
	
	
	
	/**
	 * Cria uma celula com as caracteristicas:
	 * - Font Verdana Norma 8pt
	 * - Alinhamento vertical no meio
	 * @param txt
	 * @return
	 */
	protected PdfPCell createCommonCell(String txt) {
		Phrase phrase = new Phrase( txt, FONT_8_NORMAL);
		PdfPCell cell = new PdfPCell(phrase);
		cell.setVerticalAlignment( Element.ALIGN_MIDDLE );
		return cell;
	}

	protected PdfPCell createCommonCell(Date date) {
		return createCommonCell( CalendarUtil.formatDateToFilename( date ));
	}

	protected PdfPCell createCommonCell( Long number) {
		return createCommonCell( number.toString() );
	}

	protected PdfPCell createCommonCell( Integer number) {
		return createCommonCell( number.toString() );
	}
	
	protected PdfPCell createCommentCell(String txt) {
		Phrase phrase = new Phrase( txt, FONT_6_NORMAL);
		return new PdfPCell(phrase);
	}
	
	
	
	protected void addCentralizedCell(PdfPTable table, String txt) {
		PdfPCell centralizedCell = createCommonCell( txt );
		centralizedCell.setHorizontalAlignment( Element.ALIGN_CENTER );
		table.addCell( centralizedCell );
	}
	
	protected void addCentralizedCell(PdfPTable table, Integer number) {
		PdfPCell centralizedCell = createCommonCell( number );
		centralizedCell.setHorizontalAlignment( Element.ALIGN_CENTER );
		table.addCell( centralizedCell );
	}
	
	
	protected void addDateTodayOnParagraph(Paragraph paragraph) {
		Paragraph dateParagraph = new Paragraph("Data: " + CalendarUtil.getTodayAsFilename() );
		paragraph.add( dateParagraph );
	}
	
	protected void addTextOnParagraph(Paragraph paragraph, String txt) {
		Paragraph auxParagraph = new Paragraph( txt );
		paragraph.add( auxParagraph );
	}

	
	
	private PdfPTable getHeaderTable(String[] reportTitles) throws DocumentException, MalformedURLException, IOException {
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

		for (String reportTitle : reportTitles) {
			centralText.addCell( new Phrase( reportTitle ) );
		}
		tabelaCabecalho.addCell(centralText);

		return tabelaCabecalho;
	}

	
	
	private String getLogoCBPath() {
		return getDiretorioImagens() + IMAGE_CB_LOGO;
	}

	private String getDiretorioImagens() {
		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String diretorioImagens = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "img" + File.separator;
		return diretorioImagens;
	}
	

	/**
	 * Paginador no cabecalho e rodapo
	 * @author Solkam
	 * @since 26 JUL 2013
	 */
	private class FooterPaginatorEvent extends PdfPageEventHelper {

		private String[] reportTitles;

		public FooterPaginatorEvent(String[] reportTitles) {
			this.reportTitles = reportTitles;
		}

		@Override
		public void onStartPage(PdfWriter writer, Document document) {
			try {
				PdfPTable headerTable = getHeaderTable(reportTitles);
				document.add(headerTable);
				
			} catch (MalformedURLException e) {
				throw new RuntimeException( e );
			} catch (DocumentException e) {
				throw new RuntimeException( e );
			} catch (IOException e) {
				throw new RuntimeException( e );
			}
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			PdfContentByte cb = writer.getDirectContent();

			Phrase phraseFooter = new Phrase(String.format(" %d ", writer.getPageNumber() ));
	        float rightPosition  = document.right()  - 2;
			float bottomPosition = document.bottom() - 10;
			float topPosition    = 0;
			ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, phraseFooter, rightPosition , bottomPosition, topPosition);		
	      }
	}
	
	
}

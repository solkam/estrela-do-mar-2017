package com.cb.mundo.model.exporter;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 * Configurador de response de acordo com a necessidade
 * de exportacao de relatarios
 * 
 * @author Solkam
 * @since 27 JUL 2013
 */
public class HttpResponseConfigurator {
	
	public static HttpServletResponse configResponseForInlineDisposition(String filename) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

		response.setHeader("Content-disposition", "inline=filename=" + filename);
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control",	"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		return response;
	}

	public static HttpServletResponse configResponseForAttachmentDisposition(String filename) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		response.setHeader("Pragma", "public");
		return response;
	}
	
	
}

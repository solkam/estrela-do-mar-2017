package com.cb.estreladomar.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.reportexporter.ReportComisionExcelExporter;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.dto.ComissionDTO;
import com.cb.mundo.model.dto.ComissionDetailDTO;
import com.cb.mundo.model.entity.Contact;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.exporter.excel.SimpleExcelExporter;
import com.cb.mundo.model.service.ContactService;
import com.cb.mundo.model.service.ReportMegaEventService;
import com.cb.mundo.model.util.CalendarUtil;
import com.cb.mundo.model.util.NumberUtil;

/**
 * Controller para visualizar relatorio de comissao de produtores
 * 
 * @author Solkam
 * @since 18 AGO 2013
 */
@ManagedBean(name="rComissionBean")
public class ReportComissionBean implements Serializable {

	@EJB ReportMegaEventService reportService;
	
	@EJB ContactService contactService;

	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	private MegaEvent currentMegaEvent;
	
	
	private Integer percentualInteger = 10;
	private BigDecimal subtractValue = NumberUtil.newBigDecimal(2000);
	
	private List<ComissionDTO> comissionDtos;
	
	private List<ComissionDetailDTO> detailDtos;
	
	
	//inits...
	@PostConstruct void init() {
		populateCurrenteMegaEvent();
		search();
	}
	
	private void populateCurrenteMegaEvent() {
		this.currentMegaEvent = sessionHolder.getCurrentMegaEvent();
	}


	//actions...	
	public void search() {
		//pre-processamento: limpeza dos auto-produtores
		preprocessamento();
		
		//pesquisa em si
		detailDtos = reportService.searchComissionDetailDTOByMegaEvent(currentMegaEvent, percentualInteger, subtractValue);
		buildSummary();
		JSFUtil.addMessageAboutResult(detailDtos);
	}

	/**
	 * Remove os auto-produtores para evitar comissões indevidas
	 */
	private void preprocessamento() {
		Integer nroAutoProductors = contactService.removeAutoProductor();
		if (nroAutoProductors>0) {
			Object[] params = {nroAutoProductors};
			JSFUtil.addInfoMessage("msg_autoproductors_remove_sucess", params );
		}
	}

	private void buildSummary() {
		//1.usa um map aux para agrega as comissao de cada produtor
		Map<Contact, List<ComissionDetailDTO>> mapProductor = new TreeMap<>();
		for (ComissionDetailDTO detailVar : detailDtos) {
			//p1
			Contact productor = detailVar.getParticipantContact().getProductorContact();
			summarize(mapProductor, detailVar, productor);
			//p2 (compartilhando de comissao)
			Contact productor2 = detailVar.getParticipantContact().getProductorContact2();
			summarize(mapProductor, detailVar, productor2);
		}
		
		//2.monta lista de commissionDTO
		comissionDtos = new ArrayList<ComissionDTO>();
		for (Entry<Contact, List<ComissionDetailDTO>> entryVar : mapProductor.entrySet()) {
			ComissionDTO dto = new ComissionDTO(entryVar.getKey(), entryVar.getValue() );
			comissionDtos.add( dto );
		}
		//3.ordena pelo nome civil do produtor
		Collections.sort(comissionDtos);
	}

	private void summarize(Map<Contact, List<ComissionDetailDTO>> mapProductor,	ComissionDetailDTO detailVar, Contact productor) {
		if (productor==null) {
			return;
		}
		List<ComissionDetailDTO> commisionDetailsOfProductor = mapProductor.get(productor);
		if (commisionDetailsOfProductor==null) {
			commisionDetailsOfProductor = new ArrayList<ComissionDetailDTO>();
		}
		commisionDetailsOfProductor.add( detailVar );
		mapProductor.put(productor, commisionDetailsOfProductor);
	}
	
	
	//export...
	
	public void exportToExcel() {
		SimpleExcelExporter exporter = new ReportComisionExcelExporter(comissionDtos);
		exporter.export(getReportTitles(), getFilenameViewOfProductors() );
	}
	
	private String[] getReportTitles() {
		return new String[] {
				"Informe de Comissão de Produtores",
				"Mega Evento "+currentMegaEvent.getName(),
		};
	}

	public String getFilenameViewOfParticipants() {
		return String.format("InformeComissao_VisaoParticipante_%s", CalendarUtil.getTodayAsFilename() );
	}

	public String getFilenameViewOfProductors() {
		return String.format("InformeComissao_VisaoProdutor_%s", CalendarUtil.getTodayAsFilename() );
	}

	
	//acessores...	
	private static final long serialVersionUID = -9061392524698164024L;
	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

	public List<ComissionDetailDTO> getDetailDtos() {
		return detailDtos;
	}
	public List<ComissionDTO> getComissionDtos() {
		return comissionDtos;
	}

	public Integer getPercentualInteger() {
		return percentualInteger;
	}

	public void setPercentualInteger(Integer percentualInteger) {
		this.percentualInteger = percentualInteger;
	}

	public BigDecimal getSubtractValue() {
		return subtractValue;
	}

	public void setSubtractValue(BigDecimal subtractValue) {
		this.subtractValue = subtractValue;
	}
	
	
}

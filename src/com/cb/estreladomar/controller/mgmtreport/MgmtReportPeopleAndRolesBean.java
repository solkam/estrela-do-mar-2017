package com.cb.estreladomar.controller.mgmtreport;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;

import com.cb.estreladomar.controller.holder.SessionHolder;
import com.cb.estreladomar.controller.util.JSFUtil;
import com.cb.mundo.model.dto.ReportPeopleAndRoleDetailDTO;
import com.cb.mundo.model.dto.comparator.ReportPeopleAndRoleDetailDTOComparator;
import com.cb.mundo.model.entity.MegaEvent;
import com.cb.mundo.model.entity.enumeration.MegaEventRole;
import com.cb.mundo.model.service.EventService;
import com.cb.mundo.model.service.MgmtReportService;
import com.cb.mundo.model.service.email.EmailService;
import com.cb.mundo.model.util.NumberUtil;

/**
 * Controller para Relatorio de Pessoas e Papeis
 * @author Solkam
 * @since 05 ABR 2015
 */
@ManagedBean(name="mrepPeopleAndRoleBean")
@ViewScoped
public class MgmtReportPeopleAndRolesBean implements Serializable {

	@EJB EventService eventService;
	
	@EJB MgmtReportService service;
	
	@EJB EmailService emailService;
	
	@ManagedProperty("#{sessionHolder}")
	private SessionHolder sessionHolder;
	
	
	//filter
	private Date filterDate;
	
	//resultados
	private List<MegaEvent> activeMegaEvents;
	private List<ReportPeopleAndRoleDetailDTO> dtos;
	private Map<MegaEventRole, Integer> summaryMap;
	private Map<MegaEventRole, BigDecimal> percentMap;
	private Integer totalPeople;
	private PieChartModel chartModel;
	
	
	@PostConstruct void init() {
		initFilterDate();
	}
	
	
	private void initFilterDate() {
		filterDate = new Date();
	}
	
	/**
	 * Realiza a pesquisa e dispara as operacoes de
	 * totolizacoes e construcoes.
	 */
	public void search() {
		activeMegaEvents = eventService.searchActiveMegaEventByDate(filterDate);
		if (activeMegaEvents.isEmpty()) {
			JSFUtil.addErrorMessage("msg_no_megaevent_active_by_date", null);
		
		}else {
			dtos = service.searchReportPeopleAndRoleDetailDTOByDayAndMegaEventList(filterDate, activeMegaEvents);
			orderByRole();
			totalizePeople();
			buildSummaryMap();
			buildPercentMap();
			buildChartModel();
		}
	}



	/**
	 * Ordena a lista de DTOs pelo role
	 */
	private void orderByRole() {
		Collections.sort(dtos, new ReportPeopleAndRoleDetailDTOComparator() );
	}


	/**
	 * Totaliza as pessaos que estão
	 * nos Mega Eventos no dia
	 */
	private void totalizePeople() {
		if (dtos!=null) {
			totalPeople = dtos.size();
		}
	}


	/**
	 * Constroi o mapa com os totais
	 * por roles
	 */
	private void buildSummaryMap() {
		summaryMap = new TreeMap<MegaEventRole, Integer>();
		
		for (ReportPeopleAndRoleDetailDTO dtoVar : dtos) {
			MegaEventRole roleVar = dtoVar.getRole();
			
			Integer totalByRole = summaryMap.get( roleVar );
			
			if (totalByRole==null) {//inicializa entrada
				summaryMap.put(roleVar, 1);
			} else {//incrementa
				summaryMap.put(roleVar, ++totalByRole);
			}
		}
	}
	
	
	/**
	 * Constroi o mapa de percentuais
	 */
	private void buildPercentMap() {
		percentMap = new HashMap<MegaEventRole, BigDecimal>();
		for (Entry<MegaEventRole, Integer> entryVar : summaryMap.entrySet()) {
			BigDecimal percentual = NumberUtil.percent(entryVar.getValue(), totalPeople, 0); 
			
			percentMap.put(entryVar.getKey(), percentual);
		}
	}
	

	/**
	 * Constroi o chart model para o grafico de pizza
	 */
	private void buildChartModel() {
		chartModel = new PieChartModel();
		for (Entry<MegaEventRole, Integer> entryVar : summaryMap.entrySet()) {
			chartModel.set(entryVar.getKey().getDescription(), entryVar.getValue() );
		}
	}
	
	
	



	//acessores
	private static final long serialVersionUID = 2403621157392459390L;
	public Date getFilterDate() {
		return filterDate;
	}
	public void setSessionHolder(SessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}
	public void setFilterDate(Date filterDate) {
		this.filterDate = filterDate;
	}
	public List<ReportPeopleAndRoleDetailDTO> getDtos() {
		return dtos;
	}
	public Map<MegaEventRole, Integer> getSummaryMap() {
		return summaryMap;
	}
	public Integer getTotalPeople() {
		return totalPeople;
	}
	public Map<MegaEventRole, BigDecimal> getPercentMap() {
		return percentMap;
	}
	public PieChartModel getChartModel() {
		return chartModel;
	}
	public List<MegaEvent> getActiveMegaEvents() {
		return activeMegaEvents;
	}
	
}

package com.cb.mundo.controller.helper;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;

/**
 * ManagedBean para auxiliar com datas
 * 
 * @author Solkam
 * @since 19 NOV 2014
 */
@ManagedBean(name="dateHelper")
@ApplicationScoped
public class DateHelper {
	
	private static final SelectItem[] MONTH_ARRAY = new SelectItem[12];
	
	static {
		MONTH_ARRAY[0]  = new SelectItem("1" , "Janeiro");
		MONTH_ARRAY[1]  = new SelectItem("2" , "Fevereiro");
		MONTH_ARRAY[2]  = new SelectItem("3" , "Março");
		MONTH_ARRAY[3]  = new SelectItem("4" , "Abril");
		MONTH_ARRAY[4]  = new SelectItem("5" , "Maio");
		MONTH_ARRAY[5]  = new SelectItem("6" , "Junho");
		MONTH_ARRAY[6]  = new SelectItem("7" , "Julho");
		MONTH_ARRAY[7]  = new SelectItem("8" , "Agosto");
		MONTH_ARRAY[8]  = new SelectItem("9" , "Setembro");
		MONTH_ARRAY[9]  = new SelectItem("10", "Outubro");
		MONTH_ARRAY[10] = new SelectItem("11", "Novembro");
		MONTH_ARRAY[11] = new SelectItem("12", "Dezembro");
	}
	
	
	public SelectItem[] getMonths() {
		return MONTH_ARRAY;
	}

	
}

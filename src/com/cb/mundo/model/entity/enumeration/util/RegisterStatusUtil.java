package com.cb.mundo.model.entity.enumeration.util;

import java.util.ArrayList;
import java.util.List;

import com.cb.mundo.model.entity.enumeration.RegisterStatus;

/**
 * Utilitatorio para enum Status de Register
 * @author Solkam
 * @since 05 ABR 2015
 */
public class RegisterStatusUtil {
	
	/**
	 * Retorna a lista de status que representam a presenca no mega evento
	 * @return
	 */
	public static List<RegisterStatus> getRegisterStatusListInsideMegaEvent() {
		List<RegisterStatus> statusList = new ArrayList<RegisterStatus>();
		
		for (RegisterStatus statusVar : RegisterStatus.values()) {
			if (statusVar.getFlagInsideMegaEvent()) {
				statusList.add( statusVar );
			}
		}
		return statusList;
	}
	
	
	/**
	 * Monta uma lista de status que representam o fluxo basico da inscricao
	 * @return
	 */
	public static List<RegisterStatus> getRegisterStatusListBasicFlow() {
		List<RegisterStatus> statusList = new ArrayList<RegisterStatus>();
		
		for (RegisterStatus statusVar : RegisterStatus.values()) {
			if (statusVar.getFlagBasicFlow()) {
				statusList.add( statusVar );
			}
		}
		
		return statusList;
	}

}

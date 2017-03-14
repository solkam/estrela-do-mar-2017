package com.cb.mundo.model.dto.comparator;

import java.util.Comparator;

import com.cb.mundo.model.dto.ReportPeopleAndRoleDetailDTO;

/**
 * Comparator para DTO de Pessoa e Roles.
 * @author Solkam
 * @since 28 ABR 2015
 */
public class ReportPeopleAndRoleDetailDTOComparator implements Comparator<ReportPeopleAndRoleDetailDTO> {

	/**
	 * Usa o Role para ordernar
	 */
	@Override
	public int compare(ReportPeopleAndRoleDetailDTO dto1,ReportPeopleAndRoleDetailDTO dto2) {
		return dto1.getRole().name().compareTo( dto2.getRole().name() );
	}
	
	

}

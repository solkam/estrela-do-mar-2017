package com.cb.mundo.model.exception;

import com.cb.mundo.model.entity.Facilitator;

/**
 * 
 * @author Solkam
 * @since 02 MAR 2013
 */
public class FacilitatorReferencedByProduction extends BusinessException {
	private static final long serialVersionUID = -3059033247830525986L;

	public FacilitatorReferencedByProduction(Facilitator f) {
		super("msg_facilitator_referenced_by_production", new Object[]{f.getContact().getName()} );
	}

}

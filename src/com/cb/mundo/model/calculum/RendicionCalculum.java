package com.cb.mundo.model.calculum;

import java.math.BigDecimal;

/**
 * Strategy para calcular os percentuais de rendicao 
 * 
 * @author Solkam
 * @since 22 NOV 2014
 */
public interface RendicionCalculum {
	
	public BigDecimal getValueToMountain(double balance);
	
	public BigDecimal getValueToFoundation(double balance);
	
	public BigDecimal getValueToProductors(double balance);
	
	public BigDecimal getValueToFacilitators(double balance);
	
	public BigDecimal getValueToMarketing(double balance);
	

}

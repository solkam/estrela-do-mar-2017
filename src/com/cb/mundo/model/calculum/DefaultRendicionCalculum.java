package com.cb.mundo.model.calculum;

import java.math.BigDecimal;

import static com.cb.mundo.model.util.NumberUtil.*;

/**
 * Realizacao da strategy para rendicion ate 2014.
 * [Refactor2015_01_03] considera scale de 3
 * 
 * @author Solkam
 * @since 22 NOV 2014
 */
public class DefaultRendicionCalculum implements RendicionCalculum {

	private static final BigDecimal PERCENT_MOUNTAIN    = newBigDecimal(0.15,  3);
	private static final BigDecimal PERCENT_FOUNDATION  = newBigDecimal(0.05,  3);
	private static final BigDecimal PERCENT_PRODUCTOR   = newBigDecimal(0.375, 3);
	private static final BigDecimal PERCENT_FACILITATOR = newBigDecimal(0.375, 3);
	private static final BigDecimal PERCENT_MARKETING   = newBigDecimal(0.05,  3);
	
	
	@Override
	public BigDecimal getValueToMountain(double balance) {
		return multiply( newBigDecimal(balance, 3), PERCENT_MOUNTAIN);
	}

	@Override
	public BigDecimal getValueToFoundation(double balance) {
		return multiply( newBigDecimal(balance, 3), PERCENT_FOUNDATION);
	}

	@Override
	public BigDecimal getValueToProductors(double balance) {
		return multiply( newBigDecimal(balance, 3), PERCENT_PRODUCTOR);
	}

	@Override
	public BigDecimal getValueToFacilitators(double balance) {
		return multiply( newBigDecimal(balance, 3), PERCENT_FACILITATOR);
	}

	@Override
	public BigDecimal getValueToMarketing(double balance) {
		return multiply( newBigDecimal(balance, 3), PERCENT_MARKETING);
	}
}

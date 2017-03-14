package com.cb.mundo.model.calculum;

import static com.cb.mundo.model.util.NumberUtil.multiply;
import static com.cb.mundo.model.util.NumberUtil.newBigDecimal;

import java.math.BigDecimal;

public class CoachingRendicionCalculum implements RendicionCalculum {

	private static final BigDecimal PERCENT_MOUNTAIN    = newBigDecimal(0.15, 3);
	private static final BigDecimal PERCENT_FOUNDATION  = newBigDecimal(0.05, 3);
	private static final BigDecimal PERCENT_PRODUCTOR   = newBigDecimal(0.35, 3);
	private static final BigDecimal PERCENT_FACILITATOR = newBigDecimal(0.35, 3);
	private static final BigDecimal PERCENT_MARKETING   = newBigDecimal(0.10, 3);
	

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

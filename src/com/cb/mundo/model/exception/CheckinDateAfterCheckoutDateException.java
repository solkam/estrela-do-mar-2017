package com.cb.mundo.model.exception;

/**
 * Representa que a data de check-in está após a data de check-out.
 * @author Solkam
 * @since 15 ABR 2015
 */
public class CheckinDateAfterCheckoutDateException extends BusinessException {

	public CheckinDateAfterCheckoutDateException() {
		super("msg_checkin_date_after_checkout_date", null);
	}

	
	private static final long serialVersionUID = -7607041290423661143L;
}

package com.cb.mundo.model.exception;

public class FirstDateAfterLastDateException extends BusinessException {
	private static final long serialVersionUID = 4478584424914167679L;

	public FirstDateAfterLastDateException() {
		super("msg_first_date_after_last_date", null);
	}

}

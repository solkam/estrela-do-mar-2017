package com.cb.mundo.model.exception;


/**
 * Exception para representa que de todos certificados a serem gerados, parte ja esta
 * com numero e parte esta sem.
 * 
 * @author Solkam
 * @since 02 JUL 2012
 */
public class CertificateNumberingInvalidException extends BusinessException {
	private static final long serialVersionUID = 1996247387124518L;

	public CertificateNumberingInvalidException() {
		super("msg_certificate_numbering_invalid", null);
	}
	
	

}

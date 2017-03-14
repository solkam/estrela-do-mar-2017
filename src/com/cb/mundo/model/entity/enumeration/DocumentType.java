package com.cb.mundo.model.entity.enumeration;


/**
 * Tipo de documento de identidade
 * 
 * @author Solkam
 * @since 27 dez 2012
 */
public enum DocumentType {
	
/* script para regulariza a base (rodado em 31 dez 2012)	
update contact set identityDocumentType='PASSPORT' where identityDocumentType='Pasaporte';
update contact set identityDocumentType='PASSPORT' where identityDocumentType='C.I';
update contact set identityDocumentType='PASSPORT' where identityDocumentType='CI';
update contact set identityDocumentType='PASSPORT' where identityDocumentType='Passaporte';
update contact set identityDocumentType='RUT' where identityDocumentType='Identidad';
update contact set identityDocumentType='RG' where identityDocumentType='Identidade';
update contact set identityDocumentType='OTHER' where identityDocumentType='';
*/
	
	CPF("DOCUMENT_TYPE_CPF"),
	PASSPORT("DOCUMENT_TYPE_PASSPORT"),
	RG("DOCUMENT_TYPE_RG"),
	RUT("DOCUMENT_TYPE_RUT"),
	OTHER("DOCUMENT_TYPE_OTHER")
	;
	
	private final String key;
	
	private DocumentType(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}
	
	
	
	

}

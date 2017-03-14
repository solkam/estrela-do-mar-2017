package com.cb.estreladomar.controller.exceptionhandler;

import javax.faces.context.ExceptionHandler;

import javax.faces.context.ExceptionHandlerFactory;

/**
 * Fabrica do Manipulador de Exceptions do JSF.
 * (vai configurado em faces-config.xml)
 * 
 * @author vitor
 * @24 JUN 2013
 */
public class EstrelaDoMarExceptionHandlerFactory extends ExceptionHandlerFactory {
	
	private ExceptionHandlerFactory parent;
	
	public EstrelaDoMarExceptionHandlerFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}


	@Override
	public ExceptionHandler getExceptionHandler() {
		return new EstrelaDoMarExceptionHandler( parent.getExceptionHandler() );
	}
	
}

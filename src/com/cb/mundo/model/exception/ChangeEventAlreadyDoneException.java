/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cb.mundo.model.exception;

import java.io.Serializable;

/**
 *
 * @author Lancelot
 */
public class ChangeEventAlreadyDoneException extends BusinessException implements Serializable{
	private static final long serialVersionUID = -2299103623583666666L;

	public ChangeEventAlreadyDoneException() {
		super("msg_change_event_already_done", null);
	}
}

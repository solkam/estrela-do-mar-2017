package com.cb.mundo.model.util;

/**
 * Gerador randomico de numero (para senhas resetadas)
 * 
 * @author Solkam
 * @since 25 set 2011
 */
public class Randomizer {
	
	public String randomString() {
		Long aux = System.nanoTime();
		return aux.toString().substring(8, 12);
	}

}

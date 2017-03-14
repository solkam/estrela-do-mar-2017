package com.cb.mundo.model.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utilitario para Numeros
 * 
 * @author Solkam
 * @since 14 nov 2011
 */
public class NumberUtil {

	private static final RoundingMode DEFAULT_ROUDING_MODE = RoundingMode.HALF_EVEN;

	public static final BigDecimal VALUE_ZERO      = newBigDecimal(0.00);
	public static final BigDecimal VALUE_HUNDRED   = newBigDecimal(100.00);
	public static final BigDecimal UNDEFINED_VALUE = newBigDecimal(-1);
	
	

	/* ********
	 * BUILDERS 
	 **********/
	
	public static BigDecimal newBigDecimal(Integer intValue) {
		BigDecimal newobj = new BigDecimal(intValue, MathContext.DECIMAL32);
		newobj = newobj.setScale(2, DEFAULT_ROUDING_MODE);
		return newobj;
	}
	
	public static BigDecimal newBigDecimal(Long longValue) {
		BigDecimal newobj = new BigDecimal(longValue, MathContext.DECIMAL32);
		newobj = newobj.setScale(2, DEFAULT_ROUDING_MODE);
		return newobj;
	}
	
	public static BigDecimal newBigDecimal(double doubleValue) {
		BigDecimal newobj = new BigDecimal(doubleValue, MathContext.DECIMAL32);
		newobj = newobj.setScale(2, DEFAULT_ROUDING_MODE);
		return newobj;
	}

	public static BigDecimal newBigDecimal(double doubleValue, int scale) {
		BigDecimal newobj = new BigDecimal(doubleValue, MathContext.DECIMAL32);
		newobj = newobj.setScale(scale, DEFAULT_ROUDING_MODE);
		return newobj;
	}

	
	
	/* ****************
	 * CALCULOS BASICOS
	 ******************/

	/**
	 * Divide um BigDecimal por um inteiro
	 * @param value
	 * @param qtd
	 * @return
	 */
	public static BigDecimal divide(BigDecimal value, int qtd) {
		avoidNPE(value);
		avoidDivisionByZero(qtd);
		BigDecimal result = value.divide( newBigDecimal(qtd), DEFAULT_ROUDING_MODE );
		return toTwoDecimals( result );
	}

	/**
	 * Divide um double por um bigdecimal
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal divide(double v1, BigDecimal v2) {
		avoidDivisionByZero(v2);
		BigDecimal result = newBigDecimal(v1).divide(v2, DEFAULT_ROUDING_MODE );
		return toTwoDecimals( result );
	}

	
	/**
	 * Divide dois BigDecimals
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal divide(BigDecimal v1, BigDecimal v2) {
		avoidNPE(v1);
		avoidDivisionByZero(v2);
		BigDecimal result = v1.divide(v2, DEFAULT_ROUDING_MODE );
		return toTwoDecimals( result );
	}
	
	
	/**
	 * Divide dois longs
	 * @param dividendo
	 * @param divisor
	 * @return
	 */
	public static BigDecimal divide(long dividendo, long divisor) {
		avoidDivisionByZero(divisor);
		BigDecimal dividentoBD = newBigDecimal(dividendo);
		BigDecimal divisorBD = newBigDecimal(divisor);
		return divide( dividentoBD, divisorBD );
	}
	

	/**
	 * Calcula a porcentagem entre dois inteiros e retorna big decimal
	 * @param value
	 * @param totalPeople
	 */
	public static BigDecimal percent(Integer int1, Integer int2, int decimals) {
		avoidNPE(int1);
		avoidNPE(int2);
		avoidDivisionByZero(int2);
		
		BigDecimal d1 = newBigDecimal(int1);
		BigDecimal d2 = newBigDecimal(int2);
		BigDecimal division = d1.divide( d2, DEFAULT_ROUDING_MODE );
		
		BigDecimal percent = division.multiply( VALUE_HUNDRED );
		
		return toManyDecimals( percent, decimals );
	}

	
	/**
	 * Multiplica um BigDecimal e um double
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal v1, double v2) {
		avoidNPE(v1);
		BigDecimal result = v1.multiply( newBigDecimal(v2) );
		return toTwoDecimals( result );
	}
	
	/**
	 * Multiplica um BigDecimal por um double
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal multiply(double v1, double v2) {
		avoidDivisionByZero(v2);
		BigDecimal result = newBigDecimal(v1).multiply( newBigDecimal(v2) );
		return toTwoDecimals( result );
	}
	
	/**
	 * Multiplica dois BigDecimals
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal v1, BigDecimal v2) {
		avoidNPE(v1);
		avoidNPE(v2);
		BigDecimal result = v1.multiply(v2);
		return toTwoDecimals( result );
	}
	
	
	/**
	 * Soma dois big decimais
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
		avoidNPE(v1);
		avoidNPE(v2);
		BigDecimal result = v1.add( v2 );
		return toTwoDecimals( result );
	}
	
	/**
	 * Soma um big decimal e um double
	 * @param v1
	 * @param d2
	 * @return
	 */
	public static BigDecimal add(BigDecimal v1, double d2) {
		avoidNPE(v1);
		return add( v1, newBigDecimal(d2) );
	}
	
	
	/**
	 * Subtrai dois big decimais 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal v1, BigDecimal v2) {
		avoidNPE(v1);
		avoidNPE(v2);
		BigDecimal result = v1.subtract(v2);
		return toTwoDecimals(result);
	}
	
	
	
	//antigos
	
	public static String roundTwoDecimalsAsString(double d) {
        DecimalFormat formatter = new DecimalFormat("#.00");

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance( Locale.US );
        formatter.setDecimalFormatSymbols(symbols);
        
        String roundedTxt = formatter.format(d);
        return roundedTxt;
    }
    
    public static Double roundTwoDecimals(double d) {
    	return new Double( roundTwoDecimalsAsString(d) );
    }
    
    public static boolean isDifferenceThanZero(BigDecimal value) {
    	return !VALUE_ZERO.equals(value);
    }
    
    public static boolean isEqualToZero(BigDecimal value) {
    	return VALUE_ZERO.equals(value);
    }
    

    
    
    
	/* **********
	 * COMPARACAO 
	 ************/
	
	/**
	 * Compara dois BigDecimal retorna se o primeiro parametro
	 * eh menor que o segundo
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Boolean isLowerThan(BigDecimal v1, BigDecimal v2) {
		if (v1==null || v2==null) {
			return false;
		}
		return v1.compareTo(v2) < 0 ? true : false;
	}
	
	/**
	 * Versao sobrecarregada onde o segundo paramento eh integer
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Boolean isLowerThan(BigDecimal v1, Integer v2) {
		return isLowerThan( v1, newBigDecimal(v2) );
	}
	

	/**
	 * Define se o primeiro param eh menor ou igual ao segundo 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Boolean isLowerOrEqualThan(BigDecimal v1, BigDecimal v2) {
		if (v1==null || v2==null) {
			return false;
		}
		return v1.compareTo(v2) <= 0 ? true : false;
	}
	
	/**
	 * Versao sobrecarregada com segundo param inteiro
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Boolean isLowerOrEqualThan(BigDecimal v1, Integer v2) {
		return isLowerOrEqualThan( v1, newBigDecimal(v2) );
	}

	
	/**
	 * Verifica se um BigDecimal eh maior que outro
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Boolean isGreaterThan(BigDecimal v1, BigDecimal v2) {
		if (v1==null || v2==null) {
			return false;
		}
		return v1.compareTo(v2) > 0 ? true : false;
	}

	/**
	 * Versao sobrecarregada com 2o. param inteiro
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Boolean isGreaterThan(BigDecimal v1, Integer v2) {
		return isGreaterThan( v1, newBigDecimal(v2) );
	}
	
	
	/**
	 * Verifica se primeiro parametro eh maior OU IGUAL que o segundo 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Boolean isGreaterOrEqualThan(BigDecimal v1, BigDecimal v2) {
		if (v1==null || v2==null) {
			return false;
		}
		return v1.compareTo(v2) >= 0 ? true : false;
	}
	
	/**
	 * Versao sobrecarregada com segundo parametro inteiro
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Boolean isGreaterOrEqualThan(BigDecimal v1, Integer v2) {
		return isGreaterOrEqualThan( v1, newBigDecimal(v2) );
	}

	
	/**
	 * Verifica se um valor esta dentro de uma faixa de inteiros
	 * @param beginRange
	 * @param endRange
	 * @param value
	 * @return
	 */
	public static Boolean isInsideRange(int beginRange, int endRange, BigDecimal value, boolean inclusiveMode) {
		avoidNPE(value);
		if (inclusiveMode) {
			return isGreaterOrEqualThan(value, beginRange) & isLowerOrEqualThan(value, endRange);
		} else {
			return isGreaterThan(value, beginRange) & isLowerThan(value, endRange);
		}
	}
    
    

	/* **********
	 * FORMATACAO
	 ************/
	
	/**
	 * Ajusta o big decimal a uma casa decimal.
	 * @param value
	 */
	public static BigDecimal toOneDecimals(BigDecimal value) {
		return toManyDecimals(value, 1);
	}
	
	/**
	 * Ajusta o big decimal a duas decimais.
	 * @param value
	 */
	public static BigDecimal toTwoDecimals(BigDecimal value) {
		return toManyDecimals(value, 2);
	}
	
	/**
	 * Ajusta o big decimal a duas decimais.
	 * @param value
	 */
	public static BigDecimal toThreeDecimals(BigDecimal value) {
		return toManyDecimals(value, 3);
	}

	/**
	 * Ajusta a uma valor configuravel de casas decimais.
	 * Eh delegado por outros metodos mais especificos e publicos.
	 * @param value
	 * @param decimals
	 * @return
	 */
	private static BigDecimal toManyDecimals(BigDecimal value, int decimals) {
		if (value==null) {
			return null;
		} else {
			value = value.setScale(decimals, DEFAULT_ROUDING_MODE);
			return value;
		}
	}
	
    
    
	/* ***********************
	 * VERIFICACOS PRE-ATIVAS
	 *************************/
	
	/**
	 * Evita divisao por zero, lanca exception.
	 * @param value
	 */
	private static void avoidDivisionByZero(int value) {
		if (value==0) {
			throw new IllegalArgumentException("Erro: divisao por zero detectada");
		}
	}
	
	/**
	 * Evita previamente diviso por zero lancado exception
	 * @param value
	 */
	private static void avoidDivisionByZero(long value) {
		if (value==0) {
			throw new IllegalArgumentException("Erro: divisao por zero detectada");
		}
	}

	
	/**
	 * Evita divisao por zero lancando exception.
	 * @param value
	 */
	private static void avoidDivisionByZero(BigDecimal value) {
		if (value==null || BigDecimal.ZERO.equals(value)) {
			throw new IllegalArgumentException("Erro: divisao por zero detectada");
		}
	}
	
	/**
	 * Evita divisao por zero
	 * @param value
	 */
	private static void avoidDivisionByZero(double value) {
		if (value==0.0) {
			throw new IllegalArgumentException("Erro: divisao por zero detectada");
		}
	}
	
	/**
	 * Evita NullPointerException lancando exception recebendo um big decimal
	 * @param value
	 */
	private static void avoidNPE(BigDecimal value) {
		if (value==null) {
			throw new IllegalArgumentException("Erro de null pointer detectado");
		}
	}

	/**
	 * Evitar NullPointerExcetpion recebendo um inteiro
	 * @param value
	 */
	private static void avoidNPE(Integer value) {
		if (value==null) {
			throw new IllegalArgumentException("Erro de null pointer detectado");
		}
	}
	
	
	
	/* **********
	 * Conversoes 
	 ************/
	
	public static Long asLongOrZero(String str) {
		try {
			return Long.parseLong(str);
		} catch (NumberFormatException e) {
			return 0L;
		} 
	}

    
    
}

package com.cb.mundo.model.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.cb.mundo.model.entity.ConfigParam;
import com.cb.mundo.model.entity.enumeration.ConfigParamName;

/**
 * Service para os parametros de configuracao
 * @author Solkam
 * @since 20 ABR 2015
 */
@Stateless
public class ConfigParamService {
	
	@PersistenceContext EntityManager manager;
	
	
	/**
	 * Pesquisa por todos os parametros de configuracao
	 * @return lista de parametros de configuracao encontrados
	 */
	public List<ConfigParam> searchConfigParam() {
		return manager.createNamedQuery("searchConfigParam", ConfigParam.class)
				.getResultList();
	}
	
	
	/**
	 * Salva uma lista de parametros de configuracao
	 * @param lista de parametros de configuracao
	 */
	public void saveConfigParamList(List<ConfigParam> params) {
		for (ConfigParam paramVar : params) {
			manager.merge( paramVar );
		}
	}
	
	
	/**
	 * Busca o parametro de configuracao pelo nome do parametro
	 * @param paramName
	 * @return
	 */
	public ConfigParam findConfigParamValueByName(ConfigParamName paramName) {
		try {
			return manager.find(ConfigParam.class, paramName);
		}catch(NoResultException e) {
			return null;
		}
	}
}

package com.cb.mundo.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * DAO implementado para JPA
 * 
 * @author Solkam
 * @since 25 ago 2011
 * @param <T>
 */
public class JavaxPersistenceDAO<T> implements DAO<T> {
	
	private EntityManager manager;
	private Class<T> entityClass;

	public JavaxPersistenceDAO(EntityManager manager, Class<T> entityClass) {
		super();
		this.manager = manager;
		this.entityClass = entityClass;
	}
	
	@Override
	public T save(T entity) {
		return manager.merge( entity );
	}
	
	@Override
	public void remove(T entity) {
		manager.remove( manager.merge(entity));
	}
	
	@Override
	public T findById(Serializable id) {
		return manager.find(entityClass, id);
	}
	
	@Override
	public List<T> searchAll() {
		String queryStr = String.format("SELECT e FROM %s e", entityClass.getName() );
		return manager.createQuery(queryStr, entityClass).getResultList();
	}
	
	
	

}

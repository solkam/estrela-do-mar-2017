package com.cb.mundo.model.dao;

import java.io.Serializable;
import java.util.List;

/**
 * DAO generico
 * 
 * @author Solkam
 * @since 25 ago 2011
 * @param <T>
 */
public interface DAO<T> {

	public T save(T entity);

	public void remove(T entity);

	public T findById(Serializable id);

	public List<T> searchAll();

}
package com.bonocorp.app.service;

import java.util.List;
import java.util.Optional;

public interface CrudService <T,ID>{
	T create(T entity) throws Exception;
	List<T> readAll() throws Exception;
	Optional<T> findById(ID id) throws Exception;
	T update(T entity) throws Exception;
	void deleteById( ID id ) throws Exception;
	void deleteAll() throws Exception;
}

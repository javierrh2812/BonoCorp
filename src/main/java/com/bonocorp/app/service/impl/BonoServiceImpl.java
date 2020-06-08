package com.bonocorp.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bonocorp.app.model.Bono;
import com.bonocorp.app.repository.BonoRepository;
import com.bonocorp.app.service.BonoService;

@Service
public class BonoServiceImpl implements BonoService{

	@Autowired
	BonoRepository bonoRepo;
	
	@Override
	public Bono create(Bono entity) throws Exception {
		return bonoRepo.save(entity);
	}

	@Override
	public List<Bono> readAll() throws Exception {
		return bonoRepo.findAll();
	}

	@Override
	public Optional<Bono> findById(Integer id) throws Exception {
		return bonoRepo.findById(id);
	}

	@Override
	public Bono update(Bono entity) throws Exception {
		return bonoRepo.save(entity);
	}

	@Override
	public void deleteById(Integer id) throws Exception {
		bonoRepo.deleteById(id);
	}

	@Override
	public void deleteAll() throws Exception {
		bonoRepo.deleteAll();
	}


	

}

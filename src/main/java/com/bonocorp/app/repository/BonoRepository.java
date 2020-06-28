package com.bonocorp.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bonocorp.app.model.Bono;

@Repository
public interface BonoRepository extends JpaRepository<Bono, Integer>{
	
}

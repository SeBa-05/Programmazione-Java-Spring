package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Dipendente;
import com.example.demo.repositories.IPersonaRepository;

@Service
public class PersonaService {
	
	private final IPersonaRepository personaRepo;
	
	public PersonaService(IPersonaRepository personaRepo) {
		this.personaRepo = personaRepo;
	}
	
	public Dipendente inserimentoPersona(Dipendente d) {
		return personaRepo.save(d);
	}
	
	public List<Dipendente> selectAll() {
		return personaRepo.findAll();
	}
	
	public Dipendente findById(int id) {
		Optional<Dipendente> opt = personaRepo.findById(id);
		return opt.orElse(null);
	}
	
	public Dipendente updatePersona(Dipendente d) {
		return personaRepo.save(d);
	}
	
	public void deletePersona(int id) {
		personaRepo.deleteById(id);
	}
}
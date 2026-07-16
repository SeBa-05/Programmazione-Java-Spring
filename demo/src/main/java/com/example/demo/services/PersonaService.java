package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entities.Persona;
import com.example.demo.repositories.IPersonaRepository;

@Service
public class PersonaService {
	
	private final IPersonaRepository personaRepo;
	
	public PersonaService(IPersonaRepository personaRepo)
	{
		this.personaRepo=personaRepo;
	}
	
	public Persona inserimentoPersona(Persona p)
	{
		return personaRepo.save(p);
		
		
	}

	public List<Persona> selectAll() {
		
		return personaRepo.findAll();
		
	}
	
	public Persona findById(int id) {
		Optional<Persona> opt = personaRepo.findById(id);
		return opt.orElse(null); // o lancia eccezione se preferisci
	}
	
	public Persona updatePersona(Persona p) {
		return personaRepo.save(p);
	}
	
	public void deletePersona(int id) {
		personaRepo.deleteById(id);
	}

}

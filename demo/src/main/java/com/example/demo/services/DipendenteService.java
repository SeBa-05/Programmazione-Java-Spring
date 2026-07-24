package com.example.demo.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Dipendente;
import com.example.demo.repositories.IDipendenteRepository;

@Service
public class DipendenteService {

    private final IDipendenteRepository dipendenteRepo;

    public DipendenteService(IDipendenteRepository dipendenteRepo) {
        this.dipendenteRepo = dipendenteRepo;
    }

    public Dipendente inserimentoDipendente(Dipendente d) {
        return dipendenteRepo.save(d);
    }

    // Paginazione
    public Page<Dipendente> selectAll(Pageable pageable) {
        return dipendenteRepo.findAll(pageable);
    }

    // Lista completa (senza paginazione)
    public List<Dipendente> selectAll() {
        return dipendenteRepo.findAll();
    }

    public void deleteById(Integer id) {
        dipendenteRepo.deleteById(id);
    }

    public Dipendente selectById(Integer id) {
        return dipendenteRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dipendente non trovato!"));
    }

    public Dipendente editPersona(Dipendente d) {
        return dipendenteRepo.save(d);
    }
}
package com.example.demo.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Column(name = "nome")
    private String nome;

    @Column(name = "cognome")
    private String cognome;

    @Column(name = "codice_fiscale", unique = true)
    private String cf;

    @NotNull(message = "La data di nascita è obbligatoria!")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;
}
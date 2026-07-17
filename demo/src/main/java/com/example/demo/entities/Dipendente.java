package com.example.demo.entities;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "dipendenti")
@Data
@EqualsAndHashCode(callSuper = false)
public class Dipendente extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Stipendio obbligatorio!")
    @Positive(message = "Lo stipendio deve essere maggiore di zero")
    @Column(nullable = false)
    private double stipendio;

    @NotBlank(message = "Email obbligatoria!")
    @Email(message = "Email non valida (es. nome@dominio.com)")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "Data di assunzione obbligatoria!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "data_di_assunzione", nullable = false)
    private LocalDate dataDiAssunzione;

    @NotNull(message = "Ruolo obbligatorio!")
    @Enumerated(EnumType.STRING)
    @Column(name = "ruolo", nullable = false)
    private Ruolo tipoRuolo;
}
package com.example.demo.entities;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@MappedSuperclass
@Data
public class Persona {

    @NotBlank(message = "Il nome è obbligatorio!")
    @Size(min = 3, max = 20, message = "Il nome deve avere tra 3 e 20 caratteri")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio!")
    @Size(min = 3, max = 20, message = "Il cognome deve avere tra 3 e 20 caratteri")
    @Column(nullable = false)
    private String cognome;

    @NotBlank(message = "Il codice fiscale è obbligatorio!")
    @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z]{1}[0-9]{2}[A-Z]{1}[0-9]{3}[A-Z]{1}$",
             message = "Codice fiscale non valido (es. RSSMRA85M10A562S)")
    @Column(name = "codice_fiscale", nullable = false, unique = true)
    private String cf;

    @NotNull(message = "La data di nascita è obbligatoria!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "data_di_nascita", nullable = false)
    private LocalDate dataDiNascita;

    @AssertTrue(message = "Devi essere maggiorenne (almeno 18 anni)!")
    public boolean isMaggiorenne() {
        if (dataDiNascita == null) return false;
        int eta = Period.between(dataDiNascita, LocalDate.now()).getYears();
        return eta >= 18;
    }
}
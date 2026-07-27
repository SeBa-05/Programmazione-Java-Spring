package com.example.demo.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dipendente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Dipendente extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Il nome è obbligatorio!")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio!")
    @Column(nullable = false)
    private String cognome;

    @NotBlank(message = "Il codice fiscale è obbligatorio!")
    @Column(nullable = false, unique = true, length = 16)
    private String cf;

    // dataNascita è ereditato da Persona

    @NotNull(message = "Lo stipendio è obbligatorio!")
    @Positive(message = "Lo stipendio deve essere maggiore di zero")
    @Column(nullable = false)
    private double stipendio;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "id_user", foreignKey = @ForeignKey(name = "fk_dipendente_user"))
    private User user;
}
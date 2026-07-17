package com.example.demo.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Dipendente;
import com.example.demo.services.PersonaService;

import jakarta.validation.Valid;

@Controller
public class PageController {

    private final PersonaService perService;

    public PageController(PersonaService perService) {
        this.perService = perService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:home";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/new")
    public String create(Model model) {
        model.addAttribute("p", new Dipendente());
        return "form";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("p") Dipendente d,
                         BindingResult result,
                         Model model) {

        // Controllo manuale per tipoRuolo
        if (d.getTipoRuolo() == null) {
            result.rejectValue("tipoRuolo", "NotNull", "Il ruolo è obbligatorio!");
        }

        // Controllo manuale per data di nascita e maggiore età
        if (d.getDataDiNascita() == null) {
            result.rejectValue("dataDiNascita", "NotNull", "La data di nascita è obbligatoria!");
        } else {
            int eta = Period.between(d.getDataDiNascita(), LocalDate.now()).getYears();
            if (eta < 18) {
                result.rejectValue("dataDiNascita", "Minorenne", "Devi essere maggiorenne (almeno 18 anni)!");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("p", d);
            return "form";
        }

        Dipendente saved = perService.inserimentoPersona(d);
        model.addAttribute("msg", "✅ Dipendente registrato con successo: ");
        model.addAttribute("pNew", saved);
        return "home";
    }
    
    @GetMapping("/gestione")
    public String selectAll(Model model) {
        model.addAttribute("items", perService.selectAll());
        return "gestione";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        Dipendente dipendente = perService.findById(id);
        if (dipendente == null) {
            model.addAttribute("msg", "Dipendente non trovato");
            return "redirect:/gestione";
        }
        model.addAttribute("p", dipendente);
        return "form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") int id,
                         @Valid @ModelAttribute("p") Dipendente dForm,
                         BindingResult result,
                         Model model) {

        // 1. Controllo manuale per tipoRuolo
        if (dForm.getTipoRuolo() == null) {
            result.rejectValue("tipoRuolo", "NotNull", "Il ruolo è obbligatorio!");
        }

        // 2. Controllo manuale per data di nascita e maggiore età
        if (dForm.getDataDiNascita() == null) {
            result.rejectValue("dataDiNascita", "NotNull", "La data di nascita è obbligatoria!");
        } else {
            int eta = Period.between(dForm.getDataDiNascita(), LocalDate.now()).getYears();
            if (eta < 18) {
                result.rejectValue("dataDiNascita", "Minorenne", "Devi essere maggiorenne (almeno 18 anni)!");
            }
        }

        // 3. Se ci sono errori di validazione, ritorno al form
        if (result.hasErrors()) {
            model.addAttribute("p", dForm);
            return "form";
        }

        // 4. Recupero l'originale dal database
        Dipendente originale = perService.findById(id);
        if (originale == null) {
            model.addAttribute("msg", "Dipendente non trovato");
            return "redirect:/gestione";
        }

        // 5. Controllo se ci sono modifiche (utile per mostrare il messaggio "Nessuna modifica")
        boolean modificato = false;

        if (!Objects.equals(originale.getNome(), dForm.getNome())) modificato = true;
        else if (!Objects.equals(originale.getCognome(), dForm.getCognome())) modificato = true;
        else if (!Objects.equals(originale.getCf(), dForm.getCf())) modificato = true;
        else if (!Objects.equals(originale.getDataDiNascita(), dForm.getDataDiNascita())) modificato = true;
        else if (originale.getStipendio() != dForm.getStipendio()) modificato = true;
        else if (!Objects.equals(originale.getEmail(), dForm.getEmail())) modificato = true;
        else if (!Objects.equals(originale.getDataDiAssunzione(), dForm.getDataDiAssunzione())) modificato = true;
        else if (originale.getTipoRuolo() != dForm.getTipoRuolo()) modificato = true;

        if (!modificato) {
            model.addAttribute("p", originale);
            model.addAttribute("msg", "Nessuna modifica effettuata.");
            return "form";
        }

        // 6. Aggiorno tutti i campi
        originale.setNome(dForm.getNome());
        originale.setCognome(dForm.getCognome());
        originale.setCf(dForm.getCf());
        originale.setDataDiNascita(dForm.getDataDiNascita());
        originale.setStipendio(dForm.getStipendio());
        originale.setEmail(dForm.getEmail());
        originale.setDataDiAssunzione(dForm.getDataDiAssunzione());
        originale.setTipoRuolo(dForm.getTipoRuolo());

        perService.updatePersona(originale);

        model.addAttribute("msg", "✅ Dipendente aggiornato con successo: ");
        model.addAttribute("pNew", originale);
        return "home";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model) {
        perService.deletePersona(id);
        model.addAttribute("items", perService.selectAll());
        model.addAttribute("msg", "🗑️ Dipendente eliminato con successo (ID: " + id + ")");
        return "gestione";
    }
}
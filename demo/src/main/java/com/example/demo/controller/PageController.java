package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Persona;
import com.example.demo.services.PersonaService;

@Controller
public class PageController {

	
	private final PersonaService perService;
	
	public PageController(PersonaService perService)
	{
		this.perService=perService;
	}
	
	@GetMapping("/")
	public String root()
	{
		return "redirect:home";
		
	}
	
	@GetMapping("/home")
	public String home()
	{
		return "home"; //richiama la view la pag. html
		
	}
	
	@GetMapping("/new")
	public String create(Model model)
	{
		
		model.addAttribute("p", new Persona());
		
		
		return "form"; 
		
	}
	
	@PostMapping("/new")
	public String create(@ModelAttribute("p") Persona p,Model model)
	{
		
		//System.out.println("--->"+p);
		
		Persona pNew=perService.inserimentoPersona(p);
		
		model.addAttribute("msg", "Benvenuto: ");
		model.addAttribute("pNew", pNew);
		
		return "home"; 
		
	}
	
	@GetMapping("/gestione")
	public String selectAll(Model model)
	{
		
		model.addAttribute("items", perService.selectAll());
		
		
		return "gestione"; 
		
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") int id, Model model) {
	    Persona persona = perService.findById(id);
	    if (persona == null) {
	        // se non trovata, reindirizza a gestione con messaggio
	        model.addAttribute("msg", "Persona non trovata");
	        return "redirect:/gestione";
	    }
	    model.addAttribute("p", persona);
	    return "form";
	}
	
	@PostMapping("/edit/{id}")
	public String update(@PathVariable("id") int id, 
	                     @ModelAttribute("p") Persona p, 
	                     Model model) {
	    // L'ID deve corrispondere a quello del path (per sicurezza lo imposto)
	    p.setId(id);
	    perService.updatePersona(p);
	    model.addAttribute("msg", "Aggiornato con successo: ");
	    model.addAttribute("pNew", p);
	    return "home";   // torna alla home con messaggio di conferma
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") int id, Model model) {
	    perService.deletePersona(id);
	    // Dopo l'eliminazione, ricarico la lista e mostro un messaggio in gestione
	    model.addAttribute("items", perService.selectAll());
	    model.addAttribute("msg", "Persona eliminata con successo");
	    return "gestione";
	}
	
}

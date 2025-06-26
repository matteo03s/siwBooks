package it.uniroma3.siw.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.CredenzialiService;

@Controller
public class AutoreController {

	@Autowired
	private AutoreService autoreService;
	@Autowired
	private CredenzialiService credenzialiService;
	
	@Transactional
	@GetMapping ("/autori")
	public String getAutori (Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("autori", this.autoreService.getAllAutori());
//			model.addAttribute("tipologia", new String ("prodotti"));
			return "autore/autori.html";
		}
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Credenziali credentials = credenzialiService.getCredenziali(userDetails.getUsername());
		if (credentials.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
			return "redirect:/admin/modificaAutori";
		}
		model.addAttribute("autori", this.autoreService.getAllAutori());
//		model.addAttribute("tipologia", new String ("prodotti"));
		return "autore/autori.html";
	}

	@GetMapping ("/autore/{id}")
	public String getAutore (@PathVariable("id") Long id, Model model) {
		Autore a;
		try {
			a = this.autoreService.getAutoreById(id);
		} catch (NoSuchElementException e) {
			model.addAttribute ("errorMessage", e.getMessage());
			return "error/500.html";
		}
		model.addAttribute("autore", a);
		return "autore/autore.html";
	}
	
	@Transactional
	@GetMapping ("/cercaAutori")
    public String filtraAutori (@RequestParam String filtro, @RequestParam String tipoRicerca, Model model) {
    	List <Autore> autori = new LinkedList<Autore>();
    	List <Autore> tutti = new LinkedList<Autore>();
    	tutti = (List<Autore>) this.autoreService.getAllAutori();
    	
        	for (Autore a: tutti) {
        		if (tipoRicerca.equals("nome") && 
   					a.getNome().toLowerCase().contains(filtro.toLowerCase()))
   					autori.add(a);
    				
   				else if (tipoRicerca.equals("cognome") &&
   						a.getCognome().toLowerCase().contains(filtro.toLowerCase()))
   					autori.add(a);	
   				else if (tipoRicerca.equals("nazione") &&
   						a.getNazione().toLowerCase().contains(filtro.toLowerCase()))
   					autori.add(a);
        	}
    	model.addAttribute ("numero", autori.size());
//    	model.addAttribute ("tipologia", tipologia);
    	model.addAttribute ("autori", autori);
    	return "autore/autori.html"; 
    }
}

package it.uniroma3.siw.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.RecensioneService;
import jakarta.validation.Valid;

@Controller
public class RecensioneController {

	@Autowired
	private RecensioneService recensioneService;
	@Autowired
	private LibroService libroService;
	@Autowired
	private CredenzialiService credenzialiService;
	
	@GetMapping ("/recensione/recensioni")
	public String getRecensioni (Model model, Principal principal) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Credenziali credentials = credenzialiService.getCredenziali(userDetails.getUsername());
		if (credentials.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
			return "redirect:/admin/recensioni";
		}
		model.addAttribute("recensioni", this.recensioneService.getRecensioniUsername(principal.getName()));
		return "recensione/recensioni.html";
	}
	


	@GetMapping("/recensione/ordinaRecensioni")
	public String ordinaRecensioni (@RequestParam String ordine, @RequestParam String tipoRicerca, Model model, Principal principal) {
	    List<Recensione> recensioni = new ArrayList<Recensione>();
	    if (ordine.equals("voto")) {
	    	if (tipoRicerca.equals("ascendente"))
	    		recensioni.addAll(this.recensioneService.getOrdinatiVotoAsc());
	    	else
	    		recensioni.addAll(this.recensioneService.getOrdinatiVotoDesc());
	    }
	    else {
	    	if (tipoRicerca.equals("ascendente"))
	    		recensioni.addAll(this.recensioneService.getOrdinatiDataAsc());
	    	else
	    		recensioni.addAll(this.recensioneService.getOrdinatiDataDesc());
	    }
	    List<Recensione> giuste = new ArrayList<Recensione>();
	    for(Recensione r: recensioni) {
	    	if (r.getUtente().getCredenziali().getUsername().equals(principal.getName()))
	    		giuste.add(r);
	    }
	    model.addAttribute("recensioni", giuste);
	    return "recensione/recensioni.html";
	}
	
	
	/* arriva alla pagina per scegliere su che libro scrivere una nuova recensione */
	@GetMapping ("/recensione/new")
	public String getLibriRecensione (Model model, Principal principal) {
		List<Libro> libriNonRecensiti = this.getLibriNonRecensiti (principal.getName());
		model.addAttribute("libri", libriNonRecensiti);
		return "recensione/libriRecensione.html";
	}
	/* trova i libri che non sono stati recensiti dall'utente corrente */
	private List<Libro> getLibriNonRecensiti(String username) {
		List<Libro> libri = (List<Libro>) libroService.getAllLibri();
		Utente u = credenzialiService.getCredenziali(username).getUtente();
		for (Recensione r: u.getRecensioni()) {
			libri.remove(r.getLibro());
		}
		return libri;
	}
	
	@GetMapping ("/recensione/cercaLibri")
    public String filtraLibri (@RequestParam String filtro, @RequestParam String tipoRicerca, Model model, Principal principal) {
    	List <Libro> libri = new LinkedList<Libro>();
    	List <Libro> totali = new LinkedList<Libro>();
    	totali = (List<Libro>) this.getLibriNonRecensiti(principal.getName());
    	
        	for (Libro l: totali) {
        		if (tipoRicerca.equals("titolo")) {
        			if (l.getTitolo().toLowerCase().contains(filtro.toLowerCase()))
        				libri.add(l);
        		}
        		else {
        			for (Autore a: l.getAutori()) {
       					if (tipoRicerca.equals("nome") && 
       						a.getNome().toLowerCase().contains(filtro.toLowerCase())) {
       						if (!libri.contains(l))
       							libri.add(l);
       					}
        				
       					else if (tipoRicerca.equals("cognome") &&
        					a.getCognome().toLowerCase().contains(filtro.toLowerCase())) {
        					if (!libri.contains(l))
        						libri.add(l);
        				}
       					if (tipoRicerca.equals("nazione") &&
       						a.getNazione().toLowerCase().contains(filtro.toLowerCase())) {
       						if (!libri.contains(l))
       							libri.add(l);
       					}
        			}
        		}
        	}
    	model.addAttribute ("numero", libri.size());
//    	model.addAttribute ("tipologia", tipologia);
    	model.addAttribute ("libri", libri);
    	return "recensione/libriRecensione.html"; 
    }

	
	/* arriva alla pagina per scrivere una nuova recensione */
	@GetMapping ("/recensione/new/{libroId}")
	public String getNuovaRecensione (@PathVariable("libroId") Long libroId, Model model, Principal principal) {
		Libro libro = libroService.getLibroById(libroId);
//		model.addAttribute("libro", libro);
		Recensione r = new Recensione();
		r.setLibro(libro);
		model.addAttribute("recensione", r);
		return "recensione/nuovaRecensione.html";
	}
	
	/* creazione nuova recensione (con controllo se ce ne sono altre su quel libro) */
	@PostMapping("/recensione/new")
    public String newRecensione(@Valid @ModelAttribute("recensione") Recensione recensione,
    							@RequestParam("libroId") Long libroId,
    							BindingResult bindingResult, Model model, Principal principal) {
       if (bindingResult.hasErrors()) {
    	   return "recensione/nuovaRecensione.html";
       }
       try {
           // Recupera il libro dal database
           Libro libro = libroService.getLibroById(libroId);
           // Associa il libro alla recensione
           recensione.setLibro(libro);
    	   recensioneService.saveRecensione(recensione, principal.getName());
    	   return "redirect:/recensione/recensioni";
       } catch (RuntimeException e) {
    	   model.addAttribute("errorMessage", e.getMessage());
    	   return "recensione/nuovaRecensione.html";
       }
    }
}

package it.uniroma3.siw.controller;

import java.util.ArrayList;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.LibroService;
import jakarta.validation.Valid;

@Controller
public class LibroController {
	
	@Autowired
	private LibroService libroService;
	
	@Autowired
	private CredenzialiService credenzialiService;

	@Transactional
	@GetMapping ("/libro/{id}")
	public String getLibro (@PathVariable("id") Long id, Model model) {
		Libro l;
		try {
			l = this.libroService.getLibroById(id);
		} catch (NoSuchElementException e) {
			model.addAttribute ("errorMessage", e.getMessage());
			return "error/500.html";
		}
		model.addAttribute("libro", l);
		return "libro/libro.html";
	}
	@Transactional
	@GetMapping ("/libri")
	public String getLibri (Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("libri", this.libroService.getAllLibri());
			return "libro/libri.html";
		}
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Credenziali credentials = credenzialiService.getCredenziali(userDetails.getUsername());
		if (credentials.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
			return "redirect:/admin/modificaLibri";
		}
		model.addAttribute("libri", this.libroService.getAllLibri());
		return "libro/libri.html";
	}
	@Transactional
	@GetMapping("/ordinaLibri")
	public String ordinaProdotti (@RequestParam String ordine, @RequestParam String tipoRicerca, Model model) {
	    List<Libro> libri = new ArrayList<Libro>();
	    if (ordine.equals("titolo")) {
	    	if (tipoRicerca.equals("ascendente"))
	    		libri.addAll(this.libroService.getOrdinatiTitoloAsc());
	    	else
	    		libri.addAll(this.libroService.getOrdinatiTitoloDesc());
	    }
	    else {
	    	if (tipoRicerca.equals("ascendente"))
	    		libri.addAll(this.libroService.getOrdinatiAnnoAsc());
	    	else
	    		libri.addAll(this.libroService.getOrdinatiAnnoDesc());
	    }
	    model.addAttribute("libri", libri);
	    return "libro/libri.html";
	}
	@Transactional
	@GetMapping ("/cercaLibri")
    public String filtraLibri (@RequestParam String filtro, @RequestParam String tipoRicerca, Model model) {
    	List <Libro> libri = new LinkedList<Libro>();
    	List <Libro> totali = new LinkedList<Libro>();
    	totali = (List<Libro>) this.libroService.getAllLibri();
    	
        	for (Libro l: totali) {
        		if (tipoRicerca.equals("titolo")) {
        			if (l.getTitolo().toLowerCase().contains(filtro.toLowerCase()))
        				libri.add(l);
        		}
        		
        		else if (tipoRicerca.equals("genere")) {
        			if (l.getGenere().toLowerCase().contains(filtro.toLowerCase()))
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
    	return "libro/libri.html"; 
    }

	
	

}

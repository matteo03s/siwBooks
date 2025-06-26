package it.uniroma3.siw.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.AutoreRepository;
import it.uniroma3.siw.repository.LibroRepository;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.RecensioneService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.validation.Valid;

@Controller
public class AdminController {

	@Autowired
	private LibroService libroService;
	@Autowired
	private AutoreService autoreService;
	@Autowired
	private RecensioneService recensioneService;
	@Autowired
	private UtenteService utenteService;
	@Autowired
	private CredenzialiService credenzialiService;
	

/***********************************************************************************
 ***********************************SEZIONE LIBRO***********************************
 **********************************************************************************/

	/* lista libri */
	@GetMapping ("/admin/modificaLibri")
	public String modificaLibreria(Model model) {
		model.addAttribute("libri", this.libroService.getAllLibri());
		return "admin/libro/modificaLibri.html";
	}
	
	/* cancellazione di un libro */
	@GetMapping ("/admin/cancellaLibro/{id}")
	public String cancellaLibro (@PathVariable ("id") Long id, Model model) {
		this.libroService.removeLibroId(id);
		return "redirect:/admin/modificaLibri";
	}

	/* modifica di un libro */
	@GetMapping ("/admin/modificaLibro/{id}")
	public String modificaLibro(@PathVariable ("id") Long id, Model model) {
		Libro libro = this.libroService.getLibroById(id);
		model.addAttribute("libro", libro);
		return "admin/libro/modificaLibro.html";
	}
	@Transactional
	@GetMapping("/admin/ordinaLibri")
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
	    return "admin/libro/modificaLibri.html";
	}
	@Transactional
	@GetMapping ("/admin/cercaLibri")
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
       					else if (tipoRicerca.equals("nazione") &&
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
    	return "admin/libro/modificaLibri.html"; 
    }
	
	@GetMapping ("/admin/newLibro")
	public String getNewLibro (Model model) {
		model.addAttribute("libro", new Libro());
		return "/admin/libro/nuovoLibro.html";
	}
	@PostMapping ("/admin/libro")
	public String newLibro (@Valid @ModelAttribute("libro") Libro libro, BindingResult bindingResult, Model model){
		if (bindingResult.hasErrors()) {
			return "/admin/libro/nuovoLibro.html";
		}
		this.libroService.saveLibro(libro);
		model.addAttribute("libro", libro);
		return "redirect:/admin/modificaLibro/" + libro.getId();
	}
	
	@PostMapping("/admin/libri/{id}")
    public String modificaLibro(@PathVariable("id") Long id, 
                                  @ModelAttribute("libro") Libro libroForm, 
                                  BindingResult bindingResult) {
        // Validazione manuale per consentire campi vuoti
        if (libroForm.getTitolo() != null && libroForm.getTitolo().trim().isEmpty()) {
            bindingResult.rejectValue("titolo", "error.titolo", "Il titolo non può essere vuoto");
        }

        if (bindingResult.hasErrors()) {
            return "admin/libro/modificaLibro.html";
        }

        // Carica il libro esistente dal database
        Libro libroEsistente = libroService.getLibroById(id);
        if (libroEsistente == null) {
            return "redirect:/admin/modificaLibri";
        }

        // Aggiorna solo i campi modificati
        if (libroForm.getTitolo() != null && !libroForm.getTitolo().trim().isEmpty()) {
            libroEsistente.setTitolo(libroForm.getTitolo());
        }
        if (libroForm.getTrama() != null && !libroForm.getTrama().trim().isEmpty()) {
            libroEsistente.setTrama(libroForm.getTrama());
        } else if (libroForm.getTrama() != null && libroForm.getTrama().trim().isEmpty()) {
            libroEsistente.setTrama(null); // Permette di azzerare la trama
        }
        
        if (libroForm.getGenere() != null && !libroForm.getGenere().trim().isEmpty()) {
            libroEsistente.setGenere(libroForm.getGenere());
        } else if (libroForm.getGenere() != null && libroForm.getGenere().trim().isEmpty()) {
            libroEsistente.setGenere(null); // Permette di azzerare il genere
        }
        
        if (libroForm.getAnno() != null) {
            libroEsistente.setAnno(libroForm.getAnno());
        }

        // Salva l'entità esistente (aggiorna il record esistente)
        libroService.saveLibro(libroEsistente);
        return "redirect:/admin/modificaLibri";
    }
	
	/* pagina di modifica lista autori di un libro */
	@GetMapping("/admin/modificaAutori/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		List<Autore> autoriAgg = this.autoriDaAggiungere(id);
		model.addAttribute("autoriAgg", autoriAgg);
		model.addAttribute("libro", this.libroService.getLibroById(id));

		return "admin/libro/autoriDaAggiungere.html";
	}
	
	/* autori non di un libro */
	private List<Autore> autoriDaAggiungere(Long libroId) {
		List<Autore> autori = new ArrayList<>();
		for (Autore a : autoreService.getAutoriNonLibro(libroId)) {
			autori.add(a);
		}
		return autori;
	}
	
	

	/* aggiungi un autore a un libro */	
	@GetMapping("/admin/aggiungiAutoreLibro/{autoreId}/{libroId}")
	public String aggiungiAutoreALibro(@PathVariable("autoreId") Long autoreId, @PathVariable("libroId") Long libroId, Model model) {
		this.libroService.aggiungiAutoreLibro(libroId, autoreId);
		Libro libro = this.libroService.getLibroById(libroId);

		return "redirect:/admin/modificaAutori/" + libroId;

	}
	
	/* rimuovi un autore da un libro */
	@GetMapping("/admin/rimuoviAutoreLibro/{autoreId}/{libroId}")
	public String rimuoviAutoreLibro(@PathVariable("autoreId") Long autoreId, @PathVariable("libroId") Long libroId, Model model) {
		this.libroService.rimuoviAutoreLibro(libroId, autoreId);
		Libro libro = this.libroService.getLibroById(libroId);

		return "redirect:/admin/modificaAutori/" + libroId;

	}
	
/***********************************************************************************
*********************************SEZIONE AUTORI*************************************
***********************************************************************************/	
	
	
	@GetMapping ("/admin/newAutore")
	public String getNewAutore (Model model) {
		model.addAttribute("autore", new Autore());
		return "/admin/autore/nuovoAutore.html";
	}
	@PostMapping ("/admin/autore")
	public String newAutore (@Valid @ModelAttribute("autore") Autore autore, BindingResult bindingResult, Model model){
		if (bindingResult.hasErrors()) {
			return "/admin/autore/nuovoAutore.html";
		}
		this.autoreService.saveAutore(autore);
		model.addAttribute("autore", autore);
		return "redirect:/admin/modificaAutore/" + autore.getId();
	}
		
	/* lista tutti autori (con modifiche accessibili solo dall'admin) */
	@GetMapping ("/admin/modificaAutori")
	public String modificaAutori(Model model) {
		model.addAttribute("autori", autoreService.getAllAutori());
		return "/admin/autore/modificaAutori.html";
	}
	
	/* cancellazione di un autore*/
	@GetMapping ("/admin/cancellaAutore/{id}")
	public String cancellaAutore (@PathVariable ("id") Long id, Model model) {
		this.autoreService.removeAutoreId(id);
		return "redirect:/admin/modificaAutori";
	}

	/* modifica di un autore */
	@GetMapping ("/admin/modificaAutore/{id}")
	public String modificaAutore(@PathVariable ("id") Long id, Model model) {
		Autore autore = this.autoreService.getAutoreById(id);
		model.addAttribute("autore", autore);
		return "admin/autore/modificaAutore.html";
	}
	
	@GetMapping ("/admin/cercaAutori")
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
    	return "admin/autore/modificaAutori.html"; 
    }
	
	
	
	@PostMapping("/admin/autori/{id}")
    public String modificaAutore(@PathVariable("id") Long id, 
                                  @ModelAttribute("autore") Autore autoreForm, 
                                  BindingResult bindingResult) {

        // Validazione manuale per consentire campi vuoti
//        if (autoreForm.getNome() != null && autoreForm.getNome().trim().isEmpty()) {
//            bindingResult.rejectValue("nome", "error.nome", "Il nome non può essere vuoto");
//        }
//        if (autoreForm.getCognome() != null && autoreForm.getCognome().trim().isEmpty()) {
//            bindingResult.rejectValue("nome", "error.cognome", "Il cognome non può essere vuoto");
//        }
//        
        if (bindingResult.hasErrors()) {
            return "admin/autore/modificaAutore.html";
        }

        // Carica l'autore esistente dal database
        Autore autoreEsistente = autoreService.getAutoreById(id);
        if (autoreEsistente == null) {
            return "redirect:/admin/modificaAutori";
        }

        // Aggiorna solo i campi modificati
        if (autoreForm.getNome() != null && !autoreForm.getNome().trim().isEmpty()) {
            autoreEsistente.setNome(autoreForm.getNome());
        }
        if (autoreForm.getCognome() != null && !autoreForm.getCognome().trim().isEmpty()) {
            autoreEsistente.setCognome(autoreForm.getCognome());
        }
        if (autoreForm.getNazione() != null && !autoreForm.getNazione().trim().isEmpty()) {
            autoreEsistente.setNazione(autoreForm.getNazione());
        }
        if (autoreForm.getDataNascita() != null){
            autoreEsistente.setDataNascita(autoreForm.getDataNascita());
        }
        if (autoreForm.getDataMorte() != null){
            autoreEsistente.setDataMorte(autoreForm.getDataMorte());
        } 

        // Salva l'entità esistente (aggiorna il record esistente)
        autoreService.saveAutore(autoreEsistente);
        return "redirect:/admin/modificaAutori";
    }
	
	
	/* libri non di un autore */
	private List<Libro> libriDaAggiungere(Long autoreId) {
		List<Libro> libri = new ArrayList<>();
		for (Libro l: libroService.getLibriNonAutore(autoreId)) {
			libri.add(l);
		}
		return libri;
	}
	
	/* pagina di modifica lista libri di un autore */
	@GetMapping("/admin/modificaLibri/{id}")
	public String updateLibri(@PathVariable("id") Long id, Model model) {

		List<Libro> libriAgg = this.libriDaAggiungere(id);
		model.addAttribute("libriAgg", libriAgg);
		model.addAttribute("autore", this.autoreService.getAutoreById(id));

		return "admin/autore/libriDaAggiungere.html";
	}
	
	/* aggiungi un libro ad un autore */	
	@GetMapping("/admin/aggiungiLibroAutore/{autoreId}/{libroId}")
	public String aggiungiLibroAdAutore(@PathVariable("autoreId") Long autoreId, @PathVariable("libroId") Long libroId, Model model) {
		this.libroService.aggiungiAutoreLibro(libroId, autoreId);
		Libro libro = this.libroService.getLibroById(libroId);

		return "redirect:/admin/modificaLibri/" + autoreId;

	}
	
	/* rimuovi un libro da un autore */
	@GetMapping("/admin/rimuoviLibroAutore/{autoreId}/{libroId}")
	public String rimuoviLibroAdAutore(@PathVariable("autoreId") Long autoreId, @PathVariable("libroId") Long libroId, Model model) {
		this.libroService.rimuoviAutoreLibro(libroId, autoreId);
		Libro libro = this.libroService.getLibroById(libroId);

		return "redirect:/admin/modificaLibri/" + autoreId;
	}
/***********************************************************************************
*********************************SEZIONE RECENSIONE*********************************
***********************************************************************************/	
	
	
	/* lista tutte le recensioni (accessibile solo dall'admin) */
	@GetMapping ("/admin/recensioni")
	public String tutteRecensioni(Model model) {
		model.addAttribute("recensioni", recensioneService.getAllRecensioni());
		return "/admin/recensioni.html";
	}
	@Transactional
	@GetMapping("/admin/ordinaRecensioni")
	public String ordinaRecensioni (@RequestParam String ordine, @RequestParam String tipoRicerca, Model model) {
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
	    model.addAttribute("recensioni", recensioni);
	    return "admin/recensioni.html";
	}
	
	@GetMapping ("/admin/cancellaRecensione/{recensioneId}")
	public String cancellaRecensione (@PathVariable ("id") Long id, Model model) {
		this.recensioneService.removeRecensioneById(id);
		return "redirect:/admin/recensioni";
	}
	
	@GetMapping("/admin/recensioni/{id}") 
	public String getRecensioniUtente (@PathVariable ("id") Long id,
										Model model) {
		Utente utente = utenteService.getUtente(id);
		
		model.addAttribute("recensioni", utente.getRecensioni());
		return "/admin/recensioni.html";
	}
	
	
/***********************************************************************************
***********************************SEZIONE UTENTE***********************************
***********************************************************************************/
	@GetMapping("/admin/ordinaUtenti")
	public String ordinaUtenti (@RequestParam String ordine, @RequestParam String tipoRicerca, Model model) {
	    List<Credenziali> tutti = new ArrayList<Credenziali>();
	    if (ordine.equals("username")) {
	    	if (tipoRicerca.equals("ascendente"))
	    		tutti.addAll(this.credenzialiService.getOrdinatiUsernameAsc());
	    	else
	    		tutti.addAll(this.credenzialiService.getOrdinatiUsernameDesc());
	    }
	    List<Credenziali> giusti = credenzialiService.getAllNormali();
	    List<Credenziali> utenti = new ArrayList<Credenziali>();
	    for (Credenziali c: tutti) {
	    	if (giusti.contains(c)) {
	    		utenti.add(c);
	    	}
	    }
	    model.addAttribute("utenti", utenti);
	    return "admin/utenti.html";
	}
}

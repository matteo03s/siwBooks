package it.uniroma3.siw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

	@Autowired
	private CredenzialiService credenzialiService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private LibroService libroService;
	@Autowired
	private AutoreService autoreService;
	
	@Autowired 
	protected PasswordEncoder passwordEncoder; 

	
	@GetMapping("/login")
	public String showLogin(Model model) {
		return "login.html";
	}
	
	@GetMapping(value = "/login?error=true")
	public String showErrorLogin(Model model) {
		model.addAttribute("errorMessage", "utentename o password errati");
		return "login.html";
	}
	
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("utente", new Utente());
		model.addAttribute("credenziali", new Credenziali());
		return "register.html";
	}
	
	@GetMapping(value = "/")
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("libri", libroService.getPrimoPiano());
			model.addAttribute("autori", autoreService.getPrimoPiano());
			return "index.html";
		} else {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());
			if (credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
				return "admin/indexAdmin.html";
			}
		}
		model.addAttribute("libri", libroService.getPrimoPiano());
		model.addAttribute("autori", autoreService.getPrimoPiano());
		return "index.html";
	}
	
	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {

		UserDetails utenteDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credenziali credenziali = credenzialiService.getCredenziali(utenteDetails.getUsername());
		if (credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
			return "admin/indexAdmin.html";	//se ho permessi speciali allora posso accedere ad un'altra area
		}
		model.addAttribute("libri", libroService.getPrimoPiano());
		model.addAttribute("autori", autoreService.getPrimoPiano());
		return "index.html"; //se mi sono autenticato e sono un utente normale torno alla homepage
	}

	@PostMapping(value = { "/register" })
	public String registerUtente(@Valid @ModelAttribute("utente") Utente utente,
								BindingResult utenteBindingResult,
								@Valid @ModelAttribute("credenziali") Credenziali credenziali,
								BindingResult credenzialiBindingResult,
								@RequestParam("confermaPassword") String conferma,
								Model model) {
		if (utenteBindingResult.hasErrors() || credenzialiBindingResult.hasErrors()) {
			return "register.html";
		}
		
		if (!credenziali.getPassword().equals(conferma)) {
			model.addAttribute("errorMessage", "le password non corrispondono");
			return "register.html";
		}
		try {
			this.utenteService.saveUtente(utente);
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "register.html";
		}
		try {
			credenziali.setUtente(utente);
			this.credenzialiService.save(credenziali);
			return "redirect:/login";
		} catch (RuntimeException e) {
			this.utenteService.remove (utente);
			model.addAttribute("errorMessage", e.getMessage());
			return "register.html";
		}
	}
		// se utente e credential hanno entrambi contenuti validi, memorizza Utente e the
		// Credenziali nel DB
/*		if (!utenteBindingResult.hasErrors() && !credenzialiBindingResult.hasErrors()) {
			utenteService.saveUtente(utente);
			credenziali.setUtente(utente);
			credenzialiService.save(credenziali);
			model.addAttribute("utente", utente);
			return "login.html";
		}
		return "register.html";
	}
*/
	

	
}

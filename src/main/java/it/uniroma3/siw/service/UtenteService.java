package it.uniroma3.siw.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;
import jakarta.validation.Valid;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Utente getUtente(Long id) {
        return utenteRepository.findById(id)
        		.orElseThrow(() -> new NoSuchElementException("Utente non trovato con id: " + id));
    }
    
    public Utente saveUtente(Utente utente) {
        // Verifica unicità email, escludendo l'utente corrente
        Utente existingUser = utenteRepository.findByEmail(utente.getEmail()).orElse(null);
        if (existingUser != null && !existingUser.getId().equals(utente.getId())) {
            throw new RuntimeException("Email già utilizzata da un altro utente, cambiala");
        }
        return utenteRepository.save(utente);
    }
    
	public void remove(@Valid Utente utente) {
		utenteRepository.delete(utente);	
	}
	
	public List <Utente> getOrdinatiUsernameAsc () {
		return utenteRepository.findAllByOrderByCredenzialiUsernameAsc();
	}
	public List <Utente> getOrdinatiUsernameDesc () {
		return utenteRepository.findAllByOrderByCredenzialiUsernameDesc();
	}
}

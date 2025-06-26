package it.uniroma3.siw.service;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.CredenzialiRepository;


@Service
public class CredenzialiService {

	@Autowired 
	protected PasswordEncoder passwordEncoder; 
	
	@Autowired 
	protected CredenzialiRepository credenzialiRepository;
	
	public List <Credenziali> getAllNormali () {
		List <Credenziali> utenti = new LinkedList <Credenziali> ();
		Iterable <Credenziali> tutte = credenzialiRepository.findAll();
		for (Credenziali c: tutte) {
			if (c.getRuolo().equals(Credenziali.DEFAULT_ROLE))
				utenti.add(c);
			}
		return utenti;
	}
	@Transactional 
	public Credenziali getCredenziali(Long id) {
		Optional<Credenziali> result = this.credenzialiRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Credenziali getCredenziali(String username) {
		Optional<Credenziali> result = this.credenzialiRepository.findByUsername(username);
		return result.orElse(null);
	}

	
	@Transactional
	public Credenziali save(Credenziali credenziali) {
		if (this.credenzialiRepository.findByUsername(credenziali.getUsername()).isPresent()) {
			if (!credenzialiRepository.findById(credenziali.getId()).isPresent())
//			if (!credentials.equals(this.credentialsRepository.findByUsername(credentials.getUsername())))
			throw new RuntimeException("Username già utilizza, cambialo");
		}
		credenziali.setRuolo(Credenziali.DEFAULT_ROLE);
		credenziali.setPassword(this.passwordEncoder.encode(credenziali.getPassword()));
		return this.credenzialiRepository.save(credenziali);
	}
	
	public List <Credenziali> getOrdinatiUsernameAsc () {
		return credenzialiRepository.findAllByOrderByUsernameAsc();
	}
	public List <Credenziali> getOrdinatiUsernameDesc () {
		return credenzialiRepository.findAllByOrderByUsernameDesc();
	}
}

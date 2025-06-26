package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.RecensioneRepository;

@Service
public class RecensioneService {
	
	@Autowired
	private RecensioneRepository recensioneRepository;

	@Autowired
	private CredenzialiService credenzialiSerivce;

	public Recensione getById (Long id) {
		return recensioneRepository.findById(id).orElse(null);
	}
	
	public List <Recensione> getAllRecensioni () {
		return (List<Recensione>)recensioneRepository.findAll();
	}
	public List <Recensione> getRecensioneData (LocalDate data) {
		return recensioneRepository.findByData(data);
	}
	
	public List <Recensione> getRecensioneVoto (Integer voto) {
		return recensioneRepository.findByVoto(voto);
	}
	
	public List <Recensione> getRecensioneTitoloLibro (String titolo) {
		return recensioneRepository.findByLibroTitolo(titolo);
	}
	
	public List <Recensione> getRecensioneLibro(Libro libro) {
		return recensioneRepository.findByLibro(libro);
	}
	
	public List <Recensione> getRecensioneUtente (String email) {
		return recensioneRepository.findByUtenteEmail(email);
	}
	
	public List <Recensione> getRecensioniUsername (String username) {
		return recensioneRepository.findByUtenteCredenzialiUsername(username);
	}
	
	public Recensione saveRecensione (Recensione recensione, String username) {
		if (recensione.getVoto() > 5 ) {
            throw new RuntimeException("una recensione non può avere un voto superiore a 5");
        }		
		Utente utente = credenzialiSerivce.getCredenziali(username).getUtente();
		List <Recensione> recensioni = utente.getRecensioni();
		for (Recensione r: recensioni) {
			if (recensione.getLibro().getId().equals(r.getLibro().getId()) && !r.getId().equals(recensione.getId()))
	            throw new RuntimeException("un libro non può avere più di una recensione dallo stesso utente");	
		}
		
		recensione.setData(LocalDate.now());
		
		recensione.setUtente(utente);
		return recensioneRepository.save(recensione);
	}
	
	public void removeRecensione (Recensione r) {
		recensioneRepository.delete(r);
	}
	public void removeRecensioneById (Long id) {
		recensioneRepository.deleteById(id);
	}
	
	
	/*************************************************
	****************ordinamento recensioni*****************
	**************************************************/
		public List <Recensione> getOrdinatiVotoAsc () {
			return recensioneRepository.findAllByOrderByVotoAsc();
		}
		public List <Recensione> getOrdinatiVotoDesc () {
			return recensioneRepository.findAllByOrderByVotoDesc();
		}
		public List <Recensione> getOrdinatiDataAsc () {
			return recensioneRepository.findAllByOrderByDataAsc();
		}
		public List <Recensione> getOrdinatiDataDesc () {
			return recensioneRepository.findAllByOrderByDataDesc();
		}
	

}

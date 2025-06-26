package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.AutoreRepository;
import jakarta.validation.Valid;

@Service
public class AutoreService {

	@Autowired
	private AutoreRepository autoreRepository;
	
	public Iterable <Autore> getAllAutori() {
		return autoreRepository.findAll();
	}
	
	public Autore getAutoreById (Long id) {
		return autoreRepository.findById(id)
		.orElseThrow(() -> new NoSuchElementException("Autore non trovato con id: " + id));
	}
	
	/* trova tutti gli autori con un determinato nome */
	public List <Autore> getAutoreNome (String nome)  {
		return autoreRepository.findByNome(nome);
	}
	
	/* trova tutti gli autori con un determinato cognome */
	public List <Autore> getAutoreCognome (String cognome) {
		return autoreRepository.findByCognome(cognome);
	}
	
	/* trova tutti gli autori che hanno una certa nazionalita */
	public List <Autore> getAutoreNazione (String nazione) {
		return autoreRepository.findByNazione(nazione);
	}
	
	/* trova tutti gli autori che hanno una certa data di nascita */
	public List <Autore> getAutoreNazionalita (LocalDate nascita) {
		return autoreRepository.findByDataNascita(nascita);
	}
	
	public List <Autore> getPrimoPiano () {
		List <Autore> primo = new ArrayList<>();
		List <Autore> temp = (List<Autore>)this.getAllAutori();
		Collections.shuffle(temp);
		primo.addAll(temp.subList(0, 3));		
		return primo;
	}
/***************************************************************
****trova tutti gli autori che hanno scritto un certo libro*****
****************************************************************/
	//usa oggetto libro
	public List <Autore> getLibriAutore (Libro libro) {
		return autoreRepository.findByLibri(libro);
	}
	
	//usa titolo
	public List <Autore> getAutoriNomeLibro (String libro) {
		return autoreRepository.findByLibriTitolo(libro);
	}
	
	public Iterable<Autore> getAutoriNonLibro (Long libroId) {
		return autoreRepository.findAutoriNonLibro(libroId);
	}

/********************************************************/
	
	/* salva un autore */
	public Autore saveAutore (Autore autore) {
		return autoreRepository.save(autore);
	}
	
	public void removeAutore(@Valid Autore autore) {
		autoreRepository.delete(autore);	
	}

	public void removeAutoreId(Long id) {
		autoreRepository.deleteById(id);		
	}
}

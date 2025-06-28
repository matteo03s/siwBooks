package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.LibroRepository;
import jakarta.validation.Valid;

@Service
public class LibroService {
	
	@Autowired
	private LibroRepository libroRepository;
	@Autowired
	private ImmagineService immagineService;
	
	
	public Iterable <Libro> getAllLibri() {
		return libroRepository.findAll();
	}
	
	public Libro getLibroById (Long id) {
		return libroRepository.findById(id)
		.orElseThrow(() -> new NoSuchElementException("Libro non trovato con id: " + id));
	}
	
//	public Libro getLibroByIdConImmagini(Long id) {
//	    return libroRepository.findByIdWithImmagini(id);
//	}
	
	/* trova il libro con un determinato titolo */
	public Libro getLibroTitolo (String titolo)  {
		return libroRepository.findByTitolo(titolo).orElse(null);
	}
	
	/* trova tutti i libri scritti durante un certo anno */
	public List <Libro> getLibriAnno (Integer anno) {
		return libroRepository.findByAnno(anno);
	}
	
	/* trova tutti i libri che hanno un certo voto */
	public List <Libro> getLibriVoto (Integer voto) {
		return libroRepository.findByRecensioniVoto(voto);
	}

/*************************************************
****************ordinamento libri*****************
**************************************************/
	public List <Libro> getOrdinatiTitoloAsc () {
		return libroRepository.findAllByOrderByTitoloAsc();
	}
	public List <Libro> getOrdinatiTitoloDesc () {
		return libroRepository.findAllByOrderByTitoloDesc();
	}
	public List <Libro> getOrdinatiAnnoAsc () {
		return libroRepository.findAllByOrderByAnnoAsc();
	}
	public List <Libro> getOrdinatiAnnoDesc () {
		return libroRepository.findAllByOrderByAnnoDesc();
	}
	
	
/*********************************************************************
****trova tutti i libri che sono stati scritti da un certo autore*****
**********************************************************************/
	//usa oggetto autore
	public List <Libro> getLibriAutore (Autore autore) {
		return libroRepository.findByAutori(autore);
	}
	
	//usa nome
	public List <Libro> getLibriNomeAutore (String autore) {
		return libroRepository.findByAutoriNome(autore);
	}
	
	//usa cognome
	public List <Libro> getLibriCognomeAutore (String autore) {
		return libroRepository.findByAutoriCognome(autore);
	}
	
/****************************gestisci autori libro****************************/
	public Iterable<Libro> getLibriNonAutore (Long autoreId) {
		return libroRepository.findLibriNonAutore(autoreId);
	}
	
	public void aggiungiAutoreLibro(Long libroId, Long autoreId) {
		libroRepository.aggiungiAutoreLibro(libroId, autoreId);
	}

	@Transactional
	public void rimuoviAutoreLibro(Long libroId, Long autoreId) {
		libroRepository.rimuoviAutoreLibro(libroId, autoreId);
		libroRepository.save(this.getLibroById(libroId));
	}
	
	public void rimuoviRecensioneLibro(Long libroId, Long recensioneId) {
		libroRepository.rimuoviRecensioneLibro(libroId, recensioneId);
	}

/********************************************************/
	
	public List <Libro> getPrimoPiano () {
		List <Libro> primo = new ArrayList<>();
		List <Libro> temp = (List<Libro>)this.getAllLibri();
		Collections.shuffle(temp);
		primo.addAll(temp.subList(0, 4));		
		return primo;
	}
	
	/* salva un libro */
	public Libro saveLibro (Libro libro) {
		//verifica unicità nome libro
		Libro libroEsistente = libroRepository.findByTitolo(libro.getTitolo()).orElse(null);
        if (libroEsistente != null && !libroEsistente.getId().equals(libro.getId())) {
            throw new RuntimeException("esiste già un libro con questo titolo");
        }
		return libroRepository.save(libro);
	}
	
	public Libro saveLibroImmagine(Libro libro, MultipartFile file) throws IOException {
        Libro savedLibro = this.saveLibro(libro);

        // Save the Immagine if a file is provided
        if (file != null && !file.isEmpty()) {
            immagineService.saveImmagine(file);
        }

        return savedLibro;
    }
	
	public void removeLibro(@Valid Libro libro) {
		libroRepository.delete(libro);	
	}
	@Transactional
	public void removeLibroId (Long id) {
		libroRepository.deleteById(id);
	}

	public void rimuoviAutoriLibro(Long id) {
		libroRepository.rimuoviAutoriLibro(id);
		Libro libro = this.getLibroById(id);
		for (Autore a: libro.getAutori()) {
			a.getLibri().remove(libro);
		}
		libro.getAutori().clear();
	}
	
}

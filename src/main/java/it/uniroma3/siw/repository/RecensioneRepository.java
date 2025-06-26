package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;

@Repository
public interface RecensioneRepository extends CrudRepository <Recensione, Long> {
	
	public List <Recensione> findByData(LocalDate data);
	public List <Recensione> findByVoto(Integer voto);
	
	/* ricerca tramite libro */
	public List <Recensione> findByLibroTitolo (String titolo);
	public List <Recensione> findByLibro (Libro libro);
	
	/* ordinamento */
	public List <Recensione> findAllByOrderByVotoAsc ();
	public List <Recensione> findAllByOrderByVotoDesc ();
	public List <Recensione> findAllByOrderByDataAsc ();
	public List <Recensione> findAllByOrderByDataDesc ();
	public List <Recensione> findAllByOrderByTitoloAsc ();
	public List <Recensione> findAllByOrderByTitoloDesc ();

	public List <Recensione> findByUtenteCredenzialiUsername (String username);
	public List <Recensione> findByUtenteEmail(String email);

}

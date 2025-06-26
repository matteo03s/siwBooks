package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import jakarta.transaction.Transactional;



public interface AutoreRepository extends CrudRepository <Autore, Long> {
	public List <Autore> findByNome (String nome);
	public List <Autore> findByCognome (String cognome);
	public List <Autore> findByNazione (String nazione);
	public List <Autore> findByDataNascita (LocalDate dataNascita);
	
	/* ricerca tramite libri */
	public List <Autore> findByLibri (Libro libro);
	public List <Autore> findByLibriTitolo (String titolo);
	
	@Query(value="select * "
			+ "from autore a "
			+ "where a.id not in "
			+ "(select autori_id "
			+ "from autore_libri "
			+ "where autore_libri.libri_id = :libroId)", nativeQuery=true)
	public Iterable<Autore> findAutoriNonLibro(@Param("libroId") Long id);
	
	/* ordinamento */
	public List <Autore> findAllByOrderByDataNascitaDesc();
	public List <Autore> findAllByOrderByDataNascitaAsc();
	public List <Autore> findAllByOrderByNomeAsc ();
	public List <Autore> findAllByOrderByNomeDesc ();



}

package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import jakarta.transaction.Transactional;

public interface LibroRepository extends CrudRepository <Libro, Long>{
	
	public Optional <Libro> findByTitolo (String titolo);
	public List <Libro> findByAnno (Integer anno);
	
	public List <Libro> findByRecensioniVoto(Integer voto);
	
	/* ricerca libri per autore */
	public List <Libro> findByAutori (Autore autore);
	public List <Libro> findByAutoriNome (String nome);
	public List <Libro> findByAutoriCognome (String cognome);
	public List <Libro> findByAutoriNomeAndAutoriCognome (String nome, String cognome);

	/* ordinamento */
	public List <Libro> findAllByOrderByAnnoDesc();
	public List <Libro> findAllByOrderByAnnoAsc();
	public List <Libro> findAllByOrderByTitoloAsc ();
	public List <Libro> findAllByOrderByTitoloDesc ();
	
	@Query(value="select * "
			+ "from libro l "
			+ "where l.id not in "
			+ "(select libri_id "
			+ "from autore_libri "
			+ "where autore_libri.autori_id = :autoreId)", nativeQuery=true)
	public Iterable<Libro> findLibriNonAutore(@Param("autoreId") Long id);
	
	@Query("SELECT l FROM Libro l LEFT JOIN FETCH l.immagini WHERE l.id = :id")
	Libro findByIdWithImmagini(@Param("id") Long id);
	
	@Modifying
	@Transactional
	@Query(value = "INSERT INTO autore_libri (libri_id, autori_id) VALUES (:libroId, :autoreId)", nativeQuery = true)
	void aggiungiAutoreLibro(@Param("libroId") Long libroId, @Param("autoreId") Long autoreId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM autore_libri WHERE libri_id = :libroId AND autori_id = :autoreId", nativeQuery = true)
	void rimuoviAutoreLibro(@Param("libroId") Long libroId, @Param("autoreId") Long autoreId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM autore_libri WHERE libri_id = :libroId AND autori_id = :autoreId", nativeQuery = true)
	void rimuoviRecensioneLibro(@Param("libroId") Long libroId, @Param("autoreId") Long autoreId);
	
}

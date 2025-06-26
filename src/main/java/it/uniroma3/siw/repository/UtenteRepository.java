package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Utente;

public interface UtenteRepository extends CrudRepository <Utente, Long> {
	public Optional <Utente> findByEmail (String email);
	public Utente findByNome (String nome);
	public Utente findByCognome (String cognome);
	public Utente findByRecensioniId (Long id);
	
	/* ordinamento */
	public List <Utente> findAllByOrderByCredenzialiUsernameAsc ();
	public List <Utente> findAllByOrderByCredenzialiUsernameDesc ();
}

package it.uniroma3.siw.model;

import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Credenziali {
	
	//definisco i vari ruoli o le varie authorities per accedere al sito
	public static final String DEFAULT_ROLE = "UTENTE";
	public static final String ADMIN_ROLE = "ADMIN";
		
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(unique = true)
	private String username; 
	@NotBlank
	private String password; 
	private String ruolo;
	
		
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Utente utente;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getRuolo() {
		return ruolo;
	}


	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}


	public Utente getUtente() {
		return utente;
	}


	public void setUtente(Utente utente) {
		this.utente = utente;
	}


	public static String getDefaultRole() {
		return DEFAULT_ROLE;
	}


	public static String getAdminRole() {
		return ADMIN_ROLE;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id, password, ruolo, username, utente);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credenziali other = (Credenziali) obj;
		return Objects.equals(id, other.id) && Objects.equals(password, other.password)
				&& Objects.equals(ruolo, other.ruolo) && Objects.equals(username, other.username)
				&& Objects.equals(utente, other.utente);
	}


	@Override
	public String toString() {
		return "Credenziali [id=" + id + ", username=" + username + ", password=" + password + ", ruolo=" + ruolo
				+ ", utente=" + utente + "]";
	}

}

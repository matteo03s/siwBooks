package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Libro {
	
	@Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column (unique = true, nullable = false)
    @NotBlank
	private String titolo;
	
    @Column (nullable = false)
    @NotNull
	private Integer anno;

    private String genere;
    
	public String getGenere() {
		return genere;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	private String trama;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Immagine> immagini;

    @ManyToMany(mappedBy = "libri", fetch = FetchType.EAGER)
    private Set<Autore> autori;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Recensione> recensioni;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public Integer getAnno() {
		return anno;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	public String getTrama() {
		return trama;
	}

	public void setTrama(String trama) {
		this.trama = trama;
	}
	public List<Immagine> getImmagini() {
		return immagini;
	}

	public void setImmagini(List<Immagine> immagini) {
		this.immagini = immagini;
	}



	public Set<Autore> getAutori() {
		return autori;
	}

	public void setAutori(Set<Autore> autori) {
		this.autori = autori;
	}

	public List<Recensione> getRecensioni() {
		return recensioni;
	}

	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}

	@Override
	public int hashCode() {
		return Objects.hash(anno, id, titolo, trama);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Libro other = (Libro) obj;
		return Objects.equals(anno, other.anno) && Objects.equals(id, other.id) && Objects.equals(titolo, other.titolo)
				&& Objects.equals(trama, other.trama);
	}

	@Override
	public String toString() {
		return "Libro [id=" + id + ", titolo=" + titolo + ", anno=" + anno + ", trama=" + trama
				+ ", recensioni=" + recensioni + "]";
	}

}

package it.uniroma3.siw.model;


import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;

@Entity
public class Giocatore {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "giocatore_generator")
    @SequenceGenerator(name = "giocatore_generator", sequenceName = "giocatore_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String nome;

    @Column(nullable = false)
    @NotBlank
    private String cognome;

    @Column(nullable = false)
    @NotNull
    @Past(message = "La data di nascita deve essere nel passato")
    private LocalDate dataNascita;

    @Column(nullable = false)
    @NotBlank
    private String luogoNascita;

    @Column(nullable = false)
    @NotBlank
    private String ruolo;
    
    @PastOrPresent(message = "La data di inizio tesseramento non può essere nel futuro")
    private LocalDate inizioTesseramento;
    
    @Future(message = "La data di fine tesseramento non può essere nel passato")
    private LocalDate fineTesseramento;

    @ManyToOne
    @JoinColumn(name = "squadra_id")
    private Squadra squadra;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public LocalDate getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}

	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public String getRuolo() {
		return ruolo;
	}

	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}


	@Override
	public int hashCode() {
		return Objects.hash(nome, cognome, dataNascita, luogoNascita);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Giocatore other = (Giocatore) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(id, other.id)
				&& Objects.equals(ruolo, other.ruolo);
	}

	public LocalDate getInizioTesseramento() {
		return inizioTesseramento;
	}

	public void setInizioTesseramento(LocalDate inizioTesseramento) {
		this.inizioTesseramento = inizioTesseramento;
	}

	public LocalDate getFineTesseramento() {
		return fineTesseramento;
	}

	public void setFineTesseramento(LocalDate fineTesseramento) {
		this.fineTesseramento = fineTesseramento;
	}

	public Squadra getSquadra() {
		return squadra;
	}

	public void setSquadra(Squadra squadra) {
		this.squadra = squadra;
	}
	
	
}
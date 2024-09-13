package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Squadra;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {


    List<Giocatore> findBySquadra(Squadra squadra);
    
	// Trova tutti i giocatori attualmente tesserati (inizioTesseramento <= oggi e fineTesseramento >= oggi)
	@Query("SELECT g FROM Giocatore g WHERE g.inizioTesseramento <= :oggi AND  g.fineTesseramento >= :oggi")
	List<Giocatore> findTesserati(@Param("oggi") LocalDate oggi);

	// Trova i giocatori non tesserati o con tesseramento terminato
	@Query("SELECT g FROM Giocatore g WHERE g.inizioTesseramento IS NULL OR g.fineTesseramento < :oggi")
	List<Giocatore> findLiberi(@Param("oggi") LocalDate oggi);

	boolean existsByNomeAndCognome(String nome, String cognome);

	public Optional<Giocatore> findById(Long id);
	
	Giocatore findByNomeAndCognome(String nome, String cognome);

	List<Giocatore> findAll();

}
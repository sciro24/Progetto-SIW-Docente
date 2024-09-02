package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Giocatore;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {

    public Giocatore save(Giocatore giocatore);

    public boolean existsByNomeAndCognomeAndDataNascitaAndLuogoNascita(String nome, String cognome, LocalDate dataNAscita, String luogoNascita);

    public Giocatore findByNomeAndCognomeAndDataNascitaAndLuogoNascita(String nome, String cognome, LocalDate dataNAscita, String luogoNascita);

    public Optional<Giocatore> findById(Long id);
}
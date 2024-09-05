package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Giocatore;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {

    public Giocatore save(Giocatore giocatore);

    public boolean existsByNomeAndCognomeAndDataNascita(String nome, String cognome, LocalDate dataNascita);

    public Giocatore findByNomeAndCognomeAndDataNascita(String nome, String cognome, LocalDate dataNascita);

    public Optional<Giocatore> findById(Long id);

    public List<Giocatore> findAll();
}
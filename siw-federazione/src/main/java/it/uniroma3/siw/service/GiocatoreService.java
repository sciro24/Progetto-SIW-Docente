package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.repository.GiocatoreRepository;
import jakarta.transaction.Transactional;

@Service
public class GiocatoreService {

	@Autowired
    private GiocatoreRepository giocatoreRepository;

    public List<Giocatore> findAll() {
        return giocatoreRepository.findAll();
    }

    public Giocatore findById(Long id) {
        Optional<Giocatore> giocatore = this.giocatoreRepository.findById(id);
        return giocatore.orElse(null);
    }

    // Trova tutti i giocatori di una specifica squadra
    public List<Giocatore> findBySquadra(Squadra squadra) {
        return giocatoreRepository.findBySquadra(squadra);
    }

    // Trova i giocatori attualmente tesserati
    public List<Giocatore> findTesserati() {
        return giocatoreRepository.findTesserati(LocalDate.now());
    }

    public Giocatore findByNomeAndCognome(String nome, String cognome) {
        return giocatoreRepository.findByNomeAndCognome(nome, cognome);
    }

    // Trova i giocatori liberi (non tesserati o con tesseramento terminato)
    public List<Giocatore> findLiberi() {
        return giocatoreRepository.findLiberi(LocalDate.now());
    }

    public boolean existsByNomeAndCognome(String nome, String cognome) {
        return giocatoreRepository.existsByNomeAndCognome(nome, cognome);
    }

    @Transactional
    public Giocatore save(Giocatore giocatore) {
        return giocatoreRepository.save(giocatore);
    }
}

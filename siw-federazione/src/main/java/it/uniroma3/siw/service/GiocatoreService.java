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

    // Restituisce tutti i giocatori
    public List<Giocatore> findAll() {
        return giocatoreRepository.findAll();
    }

    // Trova un giocatore per ID
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

    // Trova un giocatore per nome e cognome
    public Giocatore findByNomeAndCognome(String nome, String cognome) {
        return giocatoreRepository.findByNomeAndCognome(nome, cognome);
    }

    // Trova i giocatori liberi (non tesserati o con tesseramento terminato)
    public List<Giocatore> findLiberi() {
        return giocatoreRepository.findLiberi(LocalDate.now());
    }

    // Verifica se esiste un giocatore con un determinato nome e cognome
    public boolean existsByNomeAndCognome(String nome, String cognome) {
        return giocatoreRepository.existsByNomeAndCognome(nome, cognome);
    }

    // Salva un nuovo giocatore o aggiorna un giocatore esistente
    @Transactional
    public Giocatore save(Giocatore giocatore) {
        return giocatoreRepository.save(giocatore);
    }

    // Elimina un giocatore per ID
    @Transactional
    public void deleteById(Long id) {
        giocatoreRepository.deleteById(id);
    }



}

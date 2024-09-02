package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Tesseramento;
import it.uniroma3.siw.repository.GiocatoreRepository;
import jakarta.transaction.Transactional;

@Service
public class GiocatoreService {

    @Autowired GiocatoreRepository giocatoreRepository;
    @Autowired TesseramentoService tesseramentoService;

    @Transactional
    public Giocatore save(Giocatore giocatore) {
        return this.giocatoreRepository.save(giocatore);
    }

    public boolean existsByNomeAndCognomeAndDataNascitaAndLuogoNascita(String nome, String cognome, LocalDate dataNAscita, String luogoNascita) {
        return this.giocatoreRepository.existsByNomeAndCognomeAndDataNascitaAndLuogoNascita(nome, cognome, dataNAscita, luogoNascita);
    }

    public List<Giocatore> findBySquadra(Squadra squadra) {
        List<Tesseramento> tesseramenti = this.tesseramentoService.findBySquadra(squadra);
        List<Giocatore> ris = new ArrayList<>();
        for (Tesseramento tesseramento : tesseramenti) {
            ris.add(tesseramento.getGiocatore());
        }
        return ris;
    }

 
    public Giocatore findById(Long id) {
        Optional<Giocatore> giocatore = this.giocatoreRepository.findById(id);
        return giocatore.orElse(null);
    }
    public Giocatore findByNomeAndCognomeAndDataNascitaAndLuogoNascita(String nome, String cognome, LocalDate dataNAscita, String luogoNascita) {
        return this.giocatoreRepository.findByNomeAndCognomeAndDataNascitaAndLuogoNascita(nome, cognome, dataNAscita, luogoNascita);
    }

}
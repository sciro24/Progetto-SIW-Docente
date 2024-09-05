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

    public boolean existsByNomeAndCognomeAndDataNascita(String nome, String cognome, LocalDate dataNascita) {
        return this.giocatoreRepository.existsByNomeAndCognomeAndDataNascita(nome, cognome, dataNascita);
    }
    
    public boolean isTesserato(Giocatore giocatore, Squadra squadra) {
    	
    	if (giocatore == null || squadra == null) {
    		return false;
    	}
    	
    	LocalDate oggi = LocalDate.now();
    	Tesseramento tesseramento = this.tesseramentoService.getTesseramentoByGiocatore(giocatore).get();
    	
 
    	if (tesseramento == null) {
    		return false;
    	}
    	
    	
    	if (tesseramento.getSquadra().equals(squadra) && tesseramento.getDataInizio().isBefore(oggi) && tesseramento.getDataFine().isAfter(oggi)) {
    		return true;
    	}
    	
    	return false;
    	
    }

    public List<Giocatore> findAll() {
    	return this.giocatoreRepository.findAll();
    }
    
    public List<Giocatore> findBySquadra(Squadra squadra) {
        // Inizializza una lista vuota per i giocatori
        List<Giocatore> lista = new ArrayList<>();
        List<Giocatore> tutti = this.findAll();
       
        
        // Se il tesseramento esiste, aggiungi il giocatore alla lista
        for (Giocatore giocatore : tutti) {
        	if (this.isTesserato(giocatore, squadra)) {
        		lista.add(giocatore);
        	} 
        }
        
        // Restituisci la lista (vuota se non c'è nessun tesseramento)
        return lista;
    }
    
    


 
    public Giocatore findById(Long id) {
        Optional<Giocatore> giocatore = this.giocatoreRepository.findById(id);
        return giocatore.orElse(null);
    }
    public Giocatore findByNomeAndCognomeAndDataNascita(String nome, String cognome, LocalDate dataNascita) {
        return this.giocatoreRepository.findByNomeAndCognomeAndDataNascita(nome, cognome, dataNascita);
    }

}
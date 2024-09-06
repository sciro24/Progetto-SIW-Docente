package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Tesseramento;
import it.uniroma3.siw.repository.GiocatoreRepository;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.repository.TesseramentoRepository;

@Service
public class TesseramentoService {

	@Autowired
    private TesseramentoRepository tesseramentoRepository;

    @Autowired
    private GiocatoreRepository giocatoreRepository;

    @Autowired
    private SquadraRepository squadraRepository;

    public boolean tesseraGiocatore(Long giocatoreId, Long squadraId, LocalDate dataInizio, LocalDate dataFine) {
        Optional<Giocatore> giocatoreOpt = giocatoreRepository.findById(giocatoreId);
        Optional<Squadra> squadraOpt = squadraRepository.findById(squadraId);

        if (giocatoreOpt.isPresent() && squadraOpt.isPresent()) {
            Giocatore giocatore = giocatoreOpt.get();
            Squadra squadra = squadraOpt.get();

            // Verifica se il giocatore è già tesserato
            Optional<Tesseramento> tesseramentoEsistente = tesseramentoRepository.findByGiocatore(giocatore);
            if (tesseramentoEsistente.isPresent()) {
                return false; // Giocatore già tesserato
            }

            // Verifica se la squadra è la stessa a cui il presidente è associato
            // Se la squadra del presidente è associata al presidente stesso, puoi fare questa verifica
            if (!squadra.equals(squadraOpt.get())) {
                return false; // Il giocatore può essere tesserato solo con la squadra del presidente
            }

            // Crea un nuovo tesseramento
            Tesseramento nuovoTesseramento = new Tesseramento();
            nuovoTesseramento.setGiocatore(giocatore);
            nuovoTesseramento.setSquadra(squadra);
            nuovoTesseramento.setDataInizio(dataInizio);
            nuovoTesseramento.setDataFine(dataFine);

            tesseramentoRepository.save(nuovoTesseramento);
            return true;
        }
        return false;
    }

    public boolean terminaTesseramento(Long giocatoreId) {
        Optional<Giocatore> giocatoreOpt = giocatoreRepository.findById(giocatoreId);

        if (giocatoreOpt.isPresent()) {
            Giocatore giocatore = giocatoreOpt.get();

            // Trova il tesseramento attivo per il giocatore
            Optional<Tesseramento> tesseramentoOpt = tesseramentoRepository.findByGiocatore(giocatore);
            if (tesseramentoOpt.isPresent()) {
                tesseramentoRepository.delete(tesseramentoOpt.get());
                return true;
            }
        }
        return false;
    }
    
    
    public Tesseramento save(Tesseramento tesseramento) {
        return tesseramentoRepository.save(tesseramento);
    }

    // Verifica se due intervalli di date si sovrappongono
    public boolean dateOverlap(LocalDate newStart, LocalDate newEnd, LocalDate existingStart, LocalDate existingEnd) {
        return !(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd));
    }

    // Trova il tesseramento di un giocatore (versione aggiornata)
    public Optional<Tesseramento> getTesseramentoByGiocatore(Giocatore giocatore) {
        Optional<Tesseramento> tesseramento= tesseramentoRepository.findByGiocatore(giocatore);
        if (!tesseramento.isEmpty()) {
            return Optional.of(tesseramento.get());
        } else {
            return Optional.empty();
        }
    }

    // Trova il tesseramento per una squadra (versione aggiornata)
    public Optional<Tesseramento> getTesseramentoBySquadra(Squadra squadra) {
        List<Tesseramento> tesseramenti = tesseramentoRepository.findBySquadra(squadra);
        if (!tesseramenti.isEmpty()) {
            return Optional.of(tesseramenti.get(0));
        } else {
            return Optional.empty();
        }
    }

    // Trova un tesseramento per ID
    public Optional<Tesseramento> getTesseramentoById(Long id) {
        return tesseramentoRepository.findById(id);
    }

    // Cancella un tesseramento per ID
    public void deleteTesseramento(Long id) {
        tesseramentoRepository.deleteById(id);
    }
}

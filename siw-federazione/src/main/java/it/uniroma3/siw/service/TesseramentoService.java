package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Tesseramento;
import it.uniroma3.siw.repository.TesseramentoRepository;

@Service
public class TesseramentoService {

    @Autowired
    private TesseramentoRepository tesseramentoRepository;

    // Salva un nuovo tesseramento solo se non esiste un altro tesseramento attivo per lo stesso giocatore
    public Tesseramento save(Tesseramento tesseramento) {
        // Verifica se esiste già un tesseramento attivo per lo stesso giocatore in un altro periodo
        List<Tesseramento> tesseramentiEsistenti = tesseramentoRepository.findByGiocatore(tesseramento.getGiocatore());
        
        for (Tesseramento esistente : tesseramentiEsistenti) {
            // Controlliamo se le date del nuovo tesseramento si sovrappongono con quelle di uno esistente
            if (dateOverlap(tesseramento.getDataInizio(), tesseramento.getDataFine(), esistente.getDataInizio(), esistente.getDataFine())) {
                throw new IllegalArgumentException("Il giocatore è già tesserato in questo periodo con un'altra squadra.");
            }
        }

        // Se non ci sono sovrapposizioni di date, salviamo il tesseramento
        return tesseramentoRepository.save(tesseramento);
    }

    // Verifica se due intervalli di date si sovrappongono
    public boolean dateOverlap(LocalDate newStart, LocalDate newEnd, LocalDate existingStart, LocalDate existingEnd) {
        return !(newEnd.isBefore(existingStart) || newStart.isAfter(existingEnd));
    }

    // Trova il tesseramento di un giocatore (versione aggiornata)
    public Optional<Tesseramento> getTesseramentoByGiocatore(Giocatore giocatore) {
        List<Tesseramento> tesseramenti = tesseramentoRepository.findByGiocatore(giocatore);
        if (!tesseramenti.isEmpty()) {
            return Optional.of(tesseramenti.get(0));
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

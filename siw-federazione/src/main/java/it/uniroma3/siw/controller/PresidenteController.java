package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Tesseramento;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.service.TesseramentoService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.persistence.EntityManager;

@Controller
public class PresidenteController {

    @Autowired PresidenteService presidenteService;
    @Autowired GiocatoreService giocatoreService;
    @Autowired SquadraService squadraService;
    @Autowired UtenteService utenteService;
    @Autowired CredenzialiService credenzialiService;
    @Autowired TesseramentoService tesseramentoService;
    @Autowired EntityManager entityManager;


    private boolean controllaPresidente(Squadra squadra) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Utente utente = this.utenteService.findByUsername(userDetails.getUsername());
        Presidente presidente = this.presidenteService.findByUtente(utente);
        return squadra.getPresidente().equals(presidente);
    }

    @GetMapping("/presidente/{id}/gestisciSquadra")
    public String gestisciSquadra(@PathVariable("id")Long id, Model model)  {
        Squadra squadra = this.squadraService.findById(id);
        model.addAttribute("squadra", squadra);
        if (!this.controllaPresidente(squadra)) {
            return "presidente/permessoNegato.html";
        }
        return "presidente/gestisciSquadra.html";
    }

    @GetMapping("/presidente/{id}/formAggiungiGiocatore")
    public String formNewGiocatore(@PathVariable("id")Long id, Model model)  {
        Squadra squadra = this.squadraService.findById(id);
        model.addAttribute("squadra", squadra);
        if (!this.controllaPresidente(squadra)) {
            return "presidente/permessoNegato.html";
        }
        return "presidente/formAggiungiGiocatore.html";
    }

    
    
    @PostMapping("/presidente/{id}/giocatore")
    public String newGiocatore(@PathVariable("id")Long id, @RequestParam("nome") String nome, @RequestParam("cognome") String cognome, @RequestParam("dataNascita") LocalDate dataNascita, @RequestParam("luogoNascita") String luogoNascita, @RequestParam("ruolo") String ruolo, @RequestParam("dataInizio") String inizio, @RequestParam("dataFine") String fine, Model model)  {
        Squadra squadra = this.squadraService.findById(id);
        if (!this.controllaPresidente(squadra)) {
            return "presidente/permessoNegato.html";
        }
        
        Giocatore giocatore = new Giocatore();
        giocatore.setNome(nome);
        giocatore.setCognome(cognome);
        //LocalDate nascita = giocatore.getDataNascita();
        giocatore.setDataNascita(dataNascita);
        giocatore.setLuogoNascita(luogoNascita);
        giocatore.setRuolo(ruolo);
        LocalDate dataInizio = LocalDate.parse(inizio);
        LocalDate dataFine = LocalDate.parse(fine);
        if (dataInizio.isAfter(dataFine)) {
            LocalDate temp = dataInizio;
            dataInizio = dataFine;
            dataFine = temp;
        }
     // Verifica se il giocatore esiste già
        if (this.giocatoreService.existsByNomeAndCognomeAndDataNascita(nome, cognome, dataNascita)) {
            // Trova il giocatore esistente
            giocatore = this.giocatoreService.findByNomeAndCognomeAndDataNascita(nome, cognome, dataNascita);

            // Trova il tesseramento del giocatore, se esiste
            Optional<Tesseramento> tesseramentoOptional = this.tesseramentoService.getTesseramentoByGiocatore(giocatore);

            // Se esiste un tesseramento, controlla se si sovrappone con il periodo specificato
            if (tesseramentoOptional.isPresent()) {
                Tesseramento tesseramento = tesseramentoOptional.get();
                if (tesseramentoService.dateOverlap(dataInizio, dataFine, tesseramento.getDataInizio(), tesseramento.getDataFine())) {
                    // Se c'è sovrapposizione, mostra un messaggio di errore
                    model.addAttribute("squadra", squadra);
                    model.addAttribute("errore", "Il giocatore è già tesserato!");
                    return "presidente/giocatoreNonInserito.html";
                }
            }
        } else {
        	model.addAttribute("giocatore", giocatore);
            this.giocatoreService.save(giocatore);
            model.addAttribute("squadra", squadra);
        }
        Tesseramento nuovo = new Tesseramento();
        nuovo.setSquadra(squadra);
        nuovo.setDataInizio(dataInizio);
        nuovo.setDataFine(dataFine);
        nuovo.setGiocatore(giocatore);
        this.tesseramentoService.save(nuovo);
        model.addAttribute("tesseramento", nuovo);
        return "presidente/giocatoreAggiunto.html";
    }

    @GetMapping("/presidente/{id}/formRimuoviGiocatore")
    public String formRimuoviGiocatore(@PathVariable("id")Long id, Model model)  {
        Squadra squadra = this.squadraService.findById(id);
        model.addAttribute("squadra", squadra);
        if (!this.controllaPresidente(squadra)) {
            return "presidente/permessoNegato.html";
        }
        List<Giocatore> giocatori = this.giocatoreService.findBySquadra(squadra);
        model.addAttribute("giocatori", giocatori);
        return "presidente/formRimuoviGiocatore.html";
    }

    @PostMapping("/presidente/{id}/rimuovi")
    public String rimuoviGiocatore(@PathVariable("id") Long id, @ModelAttribute("giocatore") Giocatore giocatore, Model model) {
        // Trova la squadra tramite l'ID
        Squadra squadra = this.squadraService.findById(id);
        model.addAttribute("squadra", squadra);

        // Controlla se l'utente ha i permessi necessari
        if (!this.controllaPresidente(squadra)) {
            return "presidente/permessoNegato.html";
        }
        //model.addAttribute("giocatore", giocatore);
        Tesseramento tesseramento = this.tesseramentoService.getTesseramentoByGiocatore(giocatore).get();
        tesseramento.setDataFine(LocalDate.now());
        this.tesseramentoService.save(tesseramento);
        this.giocatoreService.save(giocatore);
        this.squadraService.save(squadra);
        
        return "presidente/giocatoreRimosso.html";
    }

}
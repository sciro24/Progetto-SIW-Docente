package it.uniroma3.siw.controller;

import java.time.LocalDate;

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
        Utente utente = this.utenteService.findByEmail(userDetails.getUsername());
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
    public String newGiocatore(@PathVariable("id")Long id, @RequestParam("nome") String nome, @RequestParam("cognome") String cognome, @RequestParam("dataNascita") String dataNascita, @RequestParam("luogoNascita") String luogoNascita, @RequestParam("ruolo") String ruolo, @RequestParam("dataInizio") String inizio, @RequestParam("dataFine") String fine, Model model)  {
        Squadra squadra = this.squadraService.findById(id);
        if (!this.controllaPresidente(squadra)) {
            return "presidente/permessoNegato.html";
        }
        if(nome.trim().isEmpty() || cognome.trim().isEmpty() || ruolo.trim().isEmpty() || luogoNascita.trim().isEmpty() || dataNascita.trim().isEmpty() || inizio.trim().isEmpty() || fine.trim().isEmpty()) {
            model.addAttribute("squadra", squadra);
            model.addAttribute("errore", "Non hai inserito tutti i campi!");
            return "presidente/giocatoreNonInserito.html";
        }
        Giocatore giocatore = new Giocatore();
        giocatore.setNome(nome);
        giocatore.setCognome(cognome);
        LocalDate nascita = LocalDate.parse(dataNascita);
        giocatore.setDataNascita(nascita);
        giocatore.setLuogoNascita(luogoNascita);
        giocatore.setRuolo(ruolo);
        LocalDate dataInizio = LocalDate.parse(inizio);
        LocalDate dataFine = LocalDate.parse(fine);
        if (dataInizio.isAfter(dataFine)) {
            LocalDate temp = dataInizio;
            dataInizio = dataFine;
            dataFine = temp;
        }
        if (this.giocatoreService.existsByNomeAndCognomeAndDataNascitaAndLuogoNascita(nome, cognome, nascita, luogoNascita)) {
            giocatore = this.giocatoreService.findByNomeAndCognomeAndDataNascitaAndLuogoNascita(nome, cognome, nascita, luogoNascita);
            if (!this.tesseramentoService.checkTesseramenti(dataInizio, dataFine, giocatore.getId())) {
                model.addAttribute("squadra", squadra);
                model.addAttribute("errore", "Il giocatore è già tesserato nel periodo selezionato!");
                return "presidente/giocatoreNonInserito.html";
            }
        } else {
            model.addAttribute("giocatore", giocatore);
            model.addAttribute("squadra", squadra);
        }
        Tesseramento nuovo = new Tesseramento();
        nuovo.setSquadra(squadra);
        nuovo.setDataInizio(dataInizio);
        nuovo.setDataFine(dataFine);
        this.giocatoreService.save(giocatore);
        nuovo.setGiocatore(giocatore);
        this.tesseramentoService.save(nuovo);
        model.addAttribute("tesseramento", nuovo);
        return "presidente/giocatoreAggiunto.html";
    }

    @GetMapping("/presidente/{id}/formRimuoviGiocatore")
    public String formEsoneraGiocatore(@PathVariable("id")Long id, Model model)  {
        Squadra squadra = this.squadraService.findById(id);
        model.addAttribute("squadra", squadra);
        if (!this.controllaPresidente(squadra)) {
            return "presidente/permessoNegato.html";
        }
        model.addAttribute("giocatori", this.giocatoreService.findBySquadra(squadra));
        return "presidente/formRimuoviGiocatore.html";
    }

    @PostMapping("/presidente/{id}/rimuovi")
    public String esoneraGiocatore(@PathVariable("id")Long id, @ModelAttribute("giocatore") Giocatore giocatore, Model model) {
        Squadra squadra = this.squadraService.findById(id);
        model.addAttribute("squadra", squadra);
        if (!this.controllaPresidente(squadra)) {
            return "presidente/permessoNegato.html";
        }
        if (giocatore.getId() == null) {
            model.addAttribute("errore", "Giocatore è un campo obbligatorio!");
            return "presidente/giocatoreNonEsonerato.html";
        }
        Tesseramento trovato = this.tesseramentoService.findTesseramentoGiusto(squadra, giocatore);
        trovato.setDataFine(LocalDate.now());
        this.tesseramentoService.save(trovato);
        return "presidente/giocatoreRimosso.html";
    }
}
package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Giocatore;
import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.GiocatoreService;
import it.uniroma3.siw.service.PresidenteService;
import it.uniroma3.siw.service.SquadraService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.persistence.EntityManager;

@Controller
public class UtenteController {

    @Autowired PresidenteService presidenteService;
    @Autowired GiocatoreService giocatoreService;
    @Autowired SquadraService squadraService;
    @Autowired UtenteService utenteService;
    @Autowired CredenzialiService credenzialiService;
    @Autowired EntityManager entityManager;

    @GetMapping("/index")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Credenziali credenziali = this.credenzialiService.getCredenziali(userDetails.getUsername());
            model.addAttribute("credenziali", credenziali);
        }
        return "index.html";
    }

    @GetMapping("/squadra/{id}")
    public String getSquadra(@PathVariable("id")Long id, Model model) {
        model.addAttribute("squadra", this.squadraService.findById(id));
        return "squadra.html";
    }

    @GetMapping("/squadra/{id}/giocatori")
    public String getGiocatori(@PathVariable("id")Long id, Model model) {
        Squadra squadra = this.squadraService.findById(id);
        model.addAttribute("squadra", squadra);
        List<Giocatore> giocatori = this.giocatoreService.findBySquadra(squadra);
        model.addAttribute("giocatori", giocatori);
        return "giocatori.html";
    }
    
    @GetMapping("/squadra/{id}/presidente")
    public String getPresidente(@PathVariable("id")Long id, Model model) {
        Squadra squadra = this.squadraService.findById(id);
        Presidente presidente = squadra.getPresidente();
        model.addAttribute("squadra", squadra);
        model.addAttribute("presidente", presidente);
        return "presidente.html";
    }

    @GetMapping("/squadra/{id}/giocatori/{id2}")
    public String getGiocatori(@PathVariable("id")Long id, @PathVariable("id2") Long id2,Model model) {
        Squadra squadra = this.squadraService.findById(id);
        Giocatore giocatore = this.giocatoreService.findById(id2);
        model.addAttribute("squadra", squadra);
        model.addAttribute("giocatore", giocatore);
        return "giocatore.html";
    }
}
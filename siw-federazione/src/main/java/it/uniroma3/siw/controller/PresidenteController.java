package it.uniroma3.siw.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Presidente;
import it.uniroma3.siw.model.Squadra;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.GiocatoreRepository;
import it.uniroma3.siw.repository.SquadraRepository;
import it.uniroma3.siw.repository.TesseramentoRepository;
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
	@Autowired SquadraRepository squadraRepository;
	@Autowired GiocatoreRepository giocatoreRepository;
	@Autowired UtenteService utenteService;
	@Autowired CredenzialiService credenzialiService;
	@Autowired TesseramentoService tesseramentoService;
	@Autowired TesseramentoRepository tesseramentoRepository;
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
			return "presidente/accessoNegato.html";
		}
		return "presidente/gestisciSquadra.html";
	}

	@GetMapping("/presidente/{id}/formTesseraGiocatore")
	public String formTesseraGiocatore(@PathVariable("id") Long id, Model model) {
	    // Trova la squadra con l'ID fornito
	    Squadra squadra = squadraService.findById(id);
	    
	    
	    // Verifica se il presidente ha accesso alla squadra
	    if (!controllaPresidente(squadra)) {
	        return "presidente/accessoNegato.html";
	    }
	    
	    // Aggiungi la squadra e i giocatori al modello
	    model.addAttribute("squadra", squadra);
	    model.addAttribute("squadre", squadraRepository.findAll());
	    model.addAttribute("giocatori", giocatoreRepository.findAll());
	    
	    // Restituisce il nome del template
	    return "presidente/formTesseraGiocatore.html";
	}

	@PostMapping("/presidente/{id}/tesseraGiocatore")
	public String tesseraGiocatore(@RequestParam("giocatoreId") Long giocatoreId,@RequestParam("squadraId") Long squadraId,@RequestParam("dataInizio") String dataInizioStr,@RequestParam("dataFine") String dataFineStr, Model model) {

		LocalDate dataInizio = LocalDate.parse(dataInizioStr);
		LocalDate dataFine = LocalDate.parse(dataFineStr);

		boolean successo = tesseramentoService.tesseraGiocatore(giocatoreId, squadraId, dataInizio, dataFine);

		if (successo) {
			return "presidente/giocatoreTesserato.html"; // Pagina di successo
		} else {
			model.addAttribute("error", "Il giocatore e' gia' tesserato con un'altra squadra o i dati forniti sono errati.");
			model.addAttribute("squadre", squadraRepository.findAll());
			model.addAttribute("giocatori", giocatoreRepository.findAll());
			return "presidente/formTesseraGiocatore.html";
		}
	}

	@GetMapping("/presidente/{id}/formSvincolaGiocatore")
	public String formSvincolaGiocatore(@RequestParam("giocatoreId") Long giocatoreId,@RequestParam("squadraId") Long squadraId,Model model) {
		model.addAttribute("squadre", squadraRepository.findAll());
		model.addAttribute("giocatori", giocatoreRepository.findAll());
		model.addAttribute("tesseramenti", tesseramentoRepository.findAll());

		return "presidente/formSvincolaGiocatore.html";
	}

	@PostMapping("/presidente/{id}/svincolaGiocatore")
	public String svincolaGiocatore(@RequestParam("giocatoreId") Long giocatoreId,@RequestParam("squadraId") Long squadraId,Model model) {

		boolean successo = tesseramentoService.terminaTesseramento(giocatoreId);

		if (successo) {
			return "/presidente/giocatoreSvincolato.html"; // Pagina di successo
		} else {
			model.addAttribute("error", "Il giocatore non è tesserato con nessuna squadra o non esiste.");
			model.addAttribute("squadre", squadraRepository.findAll());
			model.addAttribute("giocatori", giocatoreRepository.findAll());
			return "presidente/formSvincolaGiocatore.html";
		}
	}
























	//    @GetMapping("/presidente/{id}/formSvincolaGiocatore")
	//    public String formSvincolaGiocatore(@PathVariable("id")Long id, Model model)  {
	//        Squadra squadra = this.squadraService.findById(id);
	//        model.addAttribute("squadra", squadra);
	//        if (!this.controllaPresidente(squadra)) {
	//            return "presidente/accessoNegato.html";
	//        }
	//        List<Giocatore> giocatori = this.giocatoreService.findBySquadra(squadra);
	//        model.addAttribute("giocatori", giocatori);
	//        return "presidente/formSvincolaGiocatore.html";
	//    }
	//
	//    @PostMapping("/presidente/{id}/svincola")
	//    public String svincolaGiocatore(@PathVariable("id") Long id, @ModelAttribute("giocatore") Giocatore giocatore, Model model) {
	//        // Trova la squadra tramite l'ID
	//        Squadra squadra = this.squadraService.findById(id);
	//        model.addAttribute("squadra", squadra);
	//
	//        // Controlla se l'utente ha i permessi necessari
	//        if (!this.controllaPresidente(squadra)) {
	//            return "presidente/accessoNegato.html";
	//        }
	//        
	//        Tesseramento tesseramento = this.tesseramentoService.getTesseramentoByGiocatore(giocatore).get();
	//        tesseramento.setDataFine(LocalDate.now());
	//        this.tesseramentoService.save(tesseramento);
	//        this.giocatoreService.save(giocatore);
	//        this.squadraService.save(squadra);
	//        
	//        return "presidente/giocatoreRimosso.html";
	//    }

}